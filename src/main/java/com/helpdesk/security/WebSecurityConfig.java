package com.helpdesk.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    //PRODUÇÃO PEGA DAS VARIÁVEIS DE AMBIENTE, DESENVOLVIMENTO ASSUME O VALOR PADRÃO
    // Lê a origem configurada no application.properties, caso não exista, assume localhost:4200
    @Value("${CORS_ALLOWED_ORIGINS:http://localhost:4200}")
    private String allowedOrigins;

    private final JWTtokenAutenticacaoService jwtTokenService;
    private final HandlerExceptionResolver exceptionResolver;

    // Injete o serviço pelo construtor do Spring Core
    public WebSecurityConfig(JWTtokenAutenticacaoService jwtTokenService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver) {
        this.jwtTokenService = jwtTokenService;
        this.exceptionResolver = exceptionResolver;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
              // Para JWT base APIs, desativamos o CSRF 
              .csrf(csrf -> csrf.disable())
              
              // CORS configurado abaixo
              .cors(cors -> cors.configurationSource(corsConfigurationSource()))
              
              // Define o comportamento da api como Stateless, proibindo o Spring de gerar sessões na bateria
              .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
              
              .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/", "/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/usuarios/criar-conta").permitAll()
                    .requestMatchers(HttpMethod.POST, "/acessos").permitAll()
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                    .anyRequest().authenticated()
              )
              
              // Filtros de JWT sendo injetados
              .addFilterBefore(new JWTLoginFilter("/login", authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)), jwtTokenService), UsernamePasswordAuthenticationFilter.class)
              .addFilterBefore(new JWTApiAutenticacaoFilter(jwtTokenService, exceptionResolver), UsernamePasswordAuthenticationFilter.class)
              .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        
        CorsConfiguration configuration = new CorsConfiguration();
        // Usando a variável configurável
        configuration.setAllowedOrigins(List.of(allowedOrigins));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
