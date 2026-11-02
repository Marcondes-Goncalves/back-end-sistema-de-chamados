package com.helpdesk.security;

import com.helpdesk.models.Usuario;
import com.helpdesk.repository.UsuarioRepository;
import com.helpdesk.utils.ApplicationContextLoad;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

/**
 * Classe de serviço responsável por gerar e validar tokens JWT para autenticação de usuários.
*/
@Service 
public class JWTtokenAutenticacaoService {

    //PRODUÇÃO PEGA DAS VARIÁVEIS DE AMBIENTE, DESENVOLVIMENTO ASSUME O VALOR PADRÃO
    // Lê a origem configurada no application.properties, caso não exista, assume 30 dias em milissegundos
    @Value("${EXPIRATION_TIME:2592000000}")
    private long EXPIRATION_TIME;

    //PRODUÇÃO PEGA DAS VARIÁVEIS DE AMBIENTE, DESENVOLVIMENTO ASSUME O VALOR PADRÃO
    // Lê a origem configurada no application.properties, caso não exista, assume uma senha extremamente secreta
    @Value("${SECRET:SenhaExtremamenteSecreta}")
    private String SECRET;

    // Prefixo do token JWT
    private static final String TOKEN_PREFIX = "Bearer";

    // Nome do cabeçalho HTTP onde o token JWT será enviado
    private static final String HEADER_STRING = "Authorization";

     // Adiciona o token JWT ao cabeçalho da resposta HTTP e atualiza o token do usuário no banco de dados
    public void addAutenticacao(HttpServletResponse response, String login) throws IOException {

        String JWT = Jwts.builder() // Cria o token JWT
                         .setSubject(login) // Define o assunto do token como o nome de usuário
                         .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Define a data de expiração do token como a data atual mais o tempo de expiração
                         .signWith(SignatureAlgorithm.HS512, SECRET) // Assina o token com o algoritmo HS512 e a chave secreta. O ideal é que a chave secreta seja armazenada na nuvem e recuperada pelas variáveis de ambiente.
                         .compact(); // Gera o token JWT como uma string compacta

        String token = TOKEN_PREFIX + " " + JWT; // Adiciona o prefixo ao token JWT
        
        response.addHeader(HEADER_STRING, token); // Adiciona o token JWT ao cabeçalho da resposta HTTP

         // Escreve o token JWT no corpo da resposta HTTP como um objeto JSON
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");

    }

     // Implementação do método para obter a autenticação a partir do token JWT
    // Este método deve validar o token e retornar um objeto Authentication correspondente
    public Authentication getAutenticacao(HttpServletRequest request, HttpServletResponse response) throws IOException, ExpiredJwtException, SignatureException {
        
        // Obtém o token JWT do cabeçalho da requisição HTTP
        String token = request.getHeader(HEADER_STRING);

        if (token !=null) {

            String tokenLimpo = token.replace(TOKEN_PREFIX, "").trim();

            // Ao dar erro aqui, ele simplesmente jogará a Exception para quem chamou
            String user = Jwts.parser() // Cria um parser para o token JWT
                            .setSigningKey(SECRET) // Define a chave secreta para validar a assinatura do token
                            .parseClaimsJws(tokenLimpo) // Remove o prefixo do token e faz o parse do token JWT
                            .getBody() // Obtém o corpo do token JWT
                            .getSubject(); // Obtém o assunto (nome de usuário) do token JWT

            if (user != null) {

                Usuario usuario = (Usuario) ApplicationContextLoad.getApplicationContext()
                                                        .getBean(UsuarioRepository.class)
                                                        .buscarPorLogin(user); // Obtém o usuário correspondente ao nome de usuário do token JWT

                if (usuario != null) {
                        
                    // Retorna um objeto Authentication com o login de usuário, senha e autoridades do usuário
                    return new UsernamePasswordAuthenticationToken(
                        usuario.getUsername(), 
                        usuario.getPassword(), 
                        usuario.getAuthorities());
                    
                }
            }
        }

        return null; // não há token, retorna null
    }
}
