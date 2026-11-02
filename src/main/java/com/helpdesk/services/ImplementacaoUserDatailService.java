package com.helpdesk.services;

import com.helpdesk.repository.UsuarioRepository;
import com.helpdesk.models.Usuario;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service 
public class ImplementacaoUserDatailService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public ImplementacaoUserDatailService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepository.buscarPorLogin(username);
        
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        
        return new User(usuario.getEmail(), usuario.getSenha(), usuario.getAuthorities());
    }
    
}
