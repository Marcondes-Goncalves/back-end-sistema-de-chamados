package com.helpdesk.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.helpdesk.models.Acesso;
import com.helpdesk.services.AcessoService;

@RestController
@RequestMapping("/acessos")
public class AcessoController {

    private final AcessoService acessoService;

    public AcessoController(AcessoService acessoService) {
        this.acessoService = acessoService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> criarAcesso(@RequestBody Acesso acesso) {
        return new ResponseEntity<String>(acessoService.criarAcesso(acesso), HttpStatus.CREATED);
    }
}
