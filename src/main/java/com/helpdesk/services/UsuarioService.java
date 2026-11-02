package com.helpdesk.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.helpdesk.dto.UsuarioDTO;
import com.helpdesk.exception.ExceptionMsg;
import com.helpdesk.models.Usuario;
import com.helpdesk.repository.UsuarioRepository;

@Service 
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final JdbcTemplate jdbcTemplate;

    public UsuarioService(UsuarioRepository usuarioRepository, JdbcTemplate jdbcTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void inserirAcessoPadrao(Long idUsuario) {

        String constraintName = usuarioRepository.consultaConstraintRole();

        if (constraintName != null && !constraintName.isEmpty()) {
            String sql = "ALTER TABLE usuario_acessos DROP CONSTRAINT " + constraintName;
            jdbcTemplate.execute(sql);
        }

        usuarioRepository.insereAcessoRolePadrao(idUsuario);
    }
    
    @Transactional
    public UsuarioDTO criarConta(Usuario usuario) {

        Usuario usuarioExistente = (Usuario) usuarioRepository.buscarPorLogin(usuario.getUsername());

        if (usuarioExistente != null) {
            throw new ExceptionMsg("Já existe um usuário com o login: " + usuario.getUsername());
        }

        String senhaCriptografada = new BCryptPasswordEncoder().encode(usuario.getSenha());
        usuario.setSenha(senhaCriptografada);

        usuario.setCriadoEm(LocalDateTime.now());

        Usuario usuarioCriado = usuarioRepository.save(usuario);

        this.inserirAcessoPadrao(usuarioCriado.getId());

        UsuarioDTO registerDTO = new UsuarioDTO(usuarioCriado.getId(), usuarioCriado.getNome(), usuarioCriado.getEmail(), usuarioCriado.getAtivo(), usuarioCriado.getCriadoEm());

        return registerDTO;
    }
}
