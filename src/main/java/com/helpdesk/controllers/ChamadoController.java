package com.helpdesk.controllers;

import com.helpdesk.models.Chamado;
import org.springframework.data.domain.Page;
import com.helpdesk.services.ChamadoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

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
    
    @GetMapping("/abertos")
    public ResponseEntity<Page<Chamado>> chamadosAbertos(Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosAbertos(pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }
    
    @GetMapping("/chamados-abertos-usuario")
    public ResponseEntity<Page<Chamado>> meusChamados(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosAbertosUsuario(usuarioId, pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }
    
    @GetMapping("/chamados-em-andamento-usuario")
    public ResponseEntity<Page<Chamado>> chamadosEmAndamentoUsuario(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosEmAndamentoUsuario(usuarioId, pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }
    
    @GetMapping("/chamados-em-andamento-tecnico")
    public ResponseEntity<Page<Chamado>> chamadosEmAndamentoTecnico(@RequestParam Long tecnicoId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosEmAndamentoTecnico(tecnicoId, pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }
    
    @GetMapping("/chamados-resolvidos-usuario")
    public ResponseEntity<Page<Chamado>> chamadosResolvidosUsuario(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosResolvidosUsuario(usuarioId, pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }

    @GetMapping("/chamados-resolvidos-tecnico")
    public ResponseEntity<Page<Chamado>> chamadosResolvidosTecnico(@RequestParam Long tecnicoId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosResolvidosTecnico(tecnicoId, pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }

    @GetMapping("/chamados-fechados-usuario")
    public ResponseEntity<Page<Chamado>> chamadosFechadosUsuario(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosFechadosUsuario(usuarioId, pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }

    @GetMapping("/chamados-fechados-tecnico")
    public ResponseEntity<Page<Chamado>> chamadosFechadosTecnico(@RequestParam Long tecnicoId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosFechadosTecnico(tecnicoId, pageable);
        return new ResponseEntity<>(chamados, HttpStatus.OK);
    }

}
