package com.helpdesk.controllers;

import com.helpdesk.models.Chamado;
import com.helpdesk.services.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chamados")
class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping("/criar")
    public ResponseEntity<Chamado> criarChamado(@RequestBody Chamado chamado){
        Chamado novoChamado = chamadoService.criarChamado(chamado);
        return new ResponseEntity<>(novoChamado, HttpStatus.CREATED);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelarChamado(@PathVariable Long id) {
        String mensagem = chamadoService.cancelarChamado(id);
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    @PutMapping("/em-andamento/{idTecnico}")
    public ResponseEntity<Chamado> chamadoEmAndamento(@RequestBody Chamado chamado, @PathVariable Long idTecnico) {
        Chamado chamadoAtualizado = chamadoService.chamadoEmAndamento(chamado, idTecnico);
        return new ResponseEntity<>(chamadoAtualizado, HttpStatus.OK);
    }

    @PutMapping("/resolvido")
    public ResponseEntity<Chamado> chamadoResolvido(@RequestBody Chamado chamado) {
        Chamado chamadoAtualizado = chamadoService.chamadoResolvido(chamado);
        return new ResponseEntity<>(chamadoAtualizado, HttpStatus.OK);
    }

    @PutMapping("/fechar")
    public ResponseEntity<String> chamadoFechado(@RequestBody Chamado chamado) {
        String mensagem = chamadoService.chamadoFechado(chamado);
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

}
