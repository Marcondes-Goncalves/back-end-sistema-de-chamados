package com.helpdesk.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Este filtro intercepta todas as requisições para a API, e verifica se o token JWT enviado na requisição é válido. 
 * Se o token for válido, a requisição é autorizada a prosseguir. Caso contrário, a requisição é rejeitada.
 */
public class JWTApiAutenticacaoFilter extends GenericFilterBean {
    
    private final JWTtokenAutenticacaoService jwtTokenService;
    private final HandlerExceptionResolver exceptionResolver;

    // Construtor recebendo a injeção
    public JWTApiAutenticacaoFilter(JWTtokenAutenticacaoService jwtTokenService, HandlerExceptionResolver exceptionResolver) {
        this.jwtTokenService = jwtTokenService;
        this.exceptionResolver = exceptionResolver;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
        throws IOException, ServletException {
        
        try {
            // Tenta validar o token e obter a autenticação
            Authentication authentication = jwtTokenService
                .getAutenticacao((HttpServletRequest) request, (HttpServletResponse) response);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Sucesso! Pode prosseguir para  Controllers
            chain.doFilter(request, response);
            
        } catch (ExpiredJwtException | SignatureException e) {
            
            // Falhou! O token está inválido ou expirado. 
            // 1. Passamos o erro para a classe GlobalExceptionHandler (Advice) formatar
            exceptionResolver.resolveException(
                (HttpServletRequest) request, 
                (HttpServletResponse) response, 
                null, 
                e
            );
            
            // 2. Interrompemos o Fluxo: NÃO há chamada para chain.doFilter() aqui
            // Isso previne que o erro vire "Access Denied" mais para frente.
        }
    }
}
