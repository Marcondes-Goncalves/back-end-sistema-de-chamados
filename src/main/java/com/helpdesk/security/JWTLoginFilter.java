package com.helpdesk.security;

import java.io.IOException;

import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.helpdesk.models.Usuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Este filtro intercepta a requisição de login, e tenta autenticar o usuário
 * com base nas credenciais enviadas. 
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    
    private final JWTtokenAutenticacaoService jwtTokenService;
    
    // Obrigamos a passar a URL de login e o AuthenticationManager para o construtor da superclasse
    protected JWTLoginFilter(String url, AuthenticationManager authManager, JWTtokenAutenticacaoService jwtTokenService) {

        this.jwtTokenService = jwtTokenService;

        // Configura o filtro para interceptar apenas requisições POST para a URL de login (evita capturar o preflight OPTIONS do CORS)
        // super(new AntPathRequestMatcher(url));
        super(request -> request.getServletPath().equals(url) && request.getMethod().equalsIgnoreCase("POST"));

        // Configura o AuthenticationManager que será usado para autenticar as credenciais do usuário
        setAuthenticationManager(authManager);

    }

    // Retorna o usuário ao processar a autenticação, ou lança uma exceção se a autenticação falhar
    @Override
    public @Nullable Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        
        // Converte o corpo da requisição em um objeto Usuario
        Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);

        // Cria um token de autenticação com as credenciais do usuário e delega a autenticação para o AuthenticationManager
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getSenha(),
                        user.getAuthorities()
                )
        );
    }

     // Se a autenticação for bem-sucedida, adiciona o token JWT à resposta HTTP
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        
        jwtTokenService.addAutenticacao(response, authResult.getName());
    }

    // Se a autenticação falhar, retorna uma resposta HTTP 401 com uma mensagem de erro em JSON
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        
        // Retorna um JSON de erro adequado
        String jsonError = "{\"status\": 401, \"error\": \"Não autorizado\", \"mensagem\": \"Login ou senha inválidos. Tente novamente.\"}";
        
        response.getWriter().write(jsonError);
        response.getWriter().flush();
        
    }
}
