package com.helpdesk.exception;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;


@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    /**
     * 1. Exceções de Regra de Negócio (400 Bad Request)
     */
    @ExceptionHandler(ExceptionMsg.class)
    public ResponseEntity<ApiError> handleBusinessException(ExceptionMsg ex, HttpServletRequest request) {

        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Regra de negócio",
            ex.getMessage(),
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * 2. Exceção de Validação de DTOs nas requisições (@Valid) (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        // Pega o detalhamento de qual campo do JSON deu erro de validação
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .findFirst()
                .orElse(ex.getMessage());
        
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validação",
            errorMessage,
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * 3. Exceções de Autenticação - JWT (401 Unauthorized)
     */
    @ExceptionHandler({ExpiredJwtException.class, SignatureException.class})
    public ResponseEntity<ApiError> handleJwtException(Exception ex, HttpServletRequest request) {

        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.UNAUTHORIZED.value(),
            "Não Autorizado",
            "Token inválido ou expirado. Realize o login novamente.",
            request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    /**
     * 4. Exceções de Autorização (403 Forbidden)
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    // Se existir Exception do pacote de Access também, faça: @ExceptionHandler({AuthorizationDeniedException.class, AccessDeniedException.class})
    public ResponseEntity<ApiError> handleAccessDeniedException(Exception ex, HttpServletRequest request) {
        
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.FORBIDDEN.value(),
            "Acesso Negado",
            "Você não possui os privilégios necessários para acessar este recurso.",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    /**
     * 4. Exceções Genéricas - Erros do Servidor (500 Internal Server Error)
     */
    @ExceptionHandler({Exception.class, IOException.class})
    public ResponseEntity<ApiError> handleGenericException(Exception ex, HttpServletRequest request) {

        // O response retorna apenas o erro blindado de detalhes pro usuário.
        ApiError error = new ApiError(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno",
            "Ocorreu um erro inesperado no sistema. Contate o suporte.",
            request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
