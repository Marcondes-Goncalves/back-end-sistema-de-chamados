package com.helpdesk.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.helpdesk.services.UsuarioService;

import jakarta.validation.Valid;

import com.helpdesk.dto.UsuarioDTO;
import com.helpdesk.models.Usuario;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController 
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping(value = "/criar-conta", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> criarConta(@Valid @RequestBody Usuario usuario) {
        UsuarioDTO usuarioCriado = usuarioService.criarConta(usuario);
        return new ResponseEntity<UsuarioDTO>(usuarioCriado, HttpStatus.CREATED);
    }
}
