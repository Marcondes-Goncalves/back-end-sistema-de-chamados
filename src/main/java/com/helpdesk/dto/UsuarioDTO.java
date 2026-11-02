package com.helpdesk.dto;

import java.time.LocalDateTime;


public record UsuarioDTO(Long id, String nome, String email, Boolean ativo, LocalDateTime dataCriacao) {
    
}
