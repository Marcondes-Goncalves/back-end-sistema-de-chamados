package com.helpdesk.controllers;

import com.helpdesk.dto.ChamadoResponseDTO;
import com.helpdesk.models.Chamado;
import com.helpdesk.services.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<ChamadoResponseDTO> criarChamado(@Valid @RequestBody Chamado chamado){
        Chamado novoChamado = chamadoService.criarChamado(chamado);
        return new ResponseEntity<>(new ChamadoResponseDTO(novoChamado), HttpStatus.CREATED);
        
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<String> cancelarChamado(@PathVariable Long id) {
        String mensagem = chamadoService.cancelarChamado(id);
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }

    @PutMapping("/em-andamento/{idTecnico}")
    public ResponseEntity<ChamadoResponseDTO> chamadoEmAndamento(@RequestBody Chamado chamado, @PathVariable Long idTecnico) {
        Chamado chamadoAtualizado = chamadoService.chamadoEmAndamento(chamado, idTecnico);
        return new ResponseEntity<>(new ChamadoResponseDTO(chamadoAtualizado), HttpStatus.OK);
    }

    @PutMapping("/resolvido")
    public ResponseEntity<ChamadoResponseDTO> chamadoResolvido(@RequestBody Chamado chamado) {
        Chamado chamadoAtualizado = chamadoService.chamadoResolvido(chamado);
        return new ResponseEntity<>(new ChamadoResponseDTO(chamadoAtualizado), HttpStatus.OK);
    }

    @PutMapping("/fechar")
    public ResponseEntity<String> chamadoFechado(@RequestBody Chamado chamado) {
        String mensagem = chamadoService.chamadoFechado(chamado);
        return new ResponseEntity<>(mensagem, HttpStatus.OK);
    }
    
    @GetMapping("/abertos")
    public ResponseEntity<Page<ChamadoResponseDTO>> chamadosAbertos(Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosAbertos(pageable);
        Page<ChamadoResponseDTO> chamadosDTO = chamados.map(ChamadoResponseDTO::new);
        return new ResponseEntity<>(chamadosDTO, HttpStatus.OK);
    }
    
    @GetMapping("/chamados-abertos-usuario")
    public ResponseEntity<Page<ChamadoResponseDTO>> meusChamados(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosAbertosUsuario(usuarioId, pageable);
        Page<ChamadoResponseDTO> chamadosDTO = chamados.map(ChamadoResponseDTO::new);
        return new ResponseEntity<>(chamadosDTO, HttpStatus.OK);
    }
    
    @GetMapping("/chamados-em-andamento")
    public ResponseEntity<Page<ChamadoResponseDTO>> chamadosEmAndamento(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosEmAndamento(usuarioId, pageable);
        Page<ChamadoResponseDTO> chamadosDTO = chamados.map(ChamadoResponseDTO::new);
        return new ResponseEntity<>(chamadosDTO, HttpStatus.OK);
    }
    
    @GetMapping("/chamados-resolvidos")
    public ResponseEntity<Page<ChamadoResponseDTO>> chamadosResolvidos(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosResolvidos(usuarioId, pageable);
        Page<ChamadoResponseDTO> chamadosDTO = chamados.map(ChamadoResponseDTO::new);
        return new ResponseEntity<>(chamadosDTO, HttpStatus.OK);
    }

    @GetMapping("/chamados-fechados")
    public ResponseEntity<Page<ChamadoResponseDTO>> chamadosFechados(@RequestParam Long usuarioId, Pageable pageable) {
        Page<Chamado> chamados = chamadoService.chamadosFechados(usuarioId, pageable);
        Page<ChamadoResponseDTO> chamadosDTO = chamados.map(ChamadoResponseDTO::new);
        return new ResponseEntity<>(chamadosDTO, HttpStatus.OK);
    }

}
