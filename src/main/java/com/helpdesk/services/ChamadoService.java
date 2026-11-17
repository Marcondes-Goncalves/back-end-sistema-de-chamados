package com.helpdesk.services;

import com.helpdesk.enuns.StatusChamado;
import com.helpdesk.exception.ExceptionMsg;
import com.helpdesk.models.Chamado;
import com.helpdesk.models.Usuario;
import com.helpdesk.repository.ChamadoRepository;
import com.helpdesk.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // o cliente pode criar um chamado, mas ele sempre será criado com status "ABERTO"
    public Chamado criarChamado(Chamado chamado) {
        chamado.setStatus(StatusChamado.ABERTO);
        return chamadoRepository.save(chamado);
    }
    
    public Page<Chamado> ChamadosAbertos(Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.ABERTO,
            null,
            null,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados abertos no momento.");
        }
        
        return chamados;
    }
    
    public Page<Chamado> ChamadosAbertosUsuario(Long usuarioId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.ABERTO,
            usuarioId,
            null,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados abertos para este usuário no momento.");
        }
        
        return chamados;
    }

    // o usuário pode cancelar o chamado, mas apenas se ele estiver em aberto ou em andamento
    public String cancelarChamado(Long id) {
        
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ExceptionMsg("Chamado não encontrado com o ID: " + id));

        if (chamado.getStatus() == StatusChamado.ABERTO || chamado.getStatus() == StatusChamado.ANDAMENTO) {
            
            chamado.setStatus(StatusChamado.CANCELADO);
            chamadoRepository.save(chamado);
        }else {
            throw new ExceptionMsg("O chamado não pode ser cancelado porque já foi: " + chamado.getStatus());
        }

        return "Chamado cancelado com sucesso.";
    }
    
    public Page<Chamado> ChamadosCanceladosUsuario(Long usuarioId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.CANCELADO,
            usuarioId,
            null,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados cancelados para este usuário no momento.");
        }
        
        return chamados;
    }

    // o técnico pode colocar o chamado em andamento, mas apenas se ele estiver em aberto
    public Chamado chamadoEmAndamento(Chamado chamado, Long idTecnico) {

        if (chamado.getStatus() != StatusChamado.ABERTO) {
            throw new ExceptionMsg("O chamado não está em aberto e não pode ser colocado em andamento.");
        }

        Usuario tecnico = usuarioRepository.findById(idTecnico)
                .orElseThrow(() -> new ExceptionMsg("Técnico não encontrado com o ID: " + idTecnico));

        chamado.setTecnico(tecnico);
        chamado.setStatus(StatusChamado.ANDAMENTO);

        return chamadoRepository.save(chamado);
    }
    
    public Page<Chamado> ChamadosEmAndamentoUsuario(Long usuarioId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.ANDAMENTO,
            usuarioId,
            null,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados em andamento para este usuário no momento.");
        }
        
        return chamados;
    }
    
    public Page<Chamado> ChamadosEmAndamentoTecnico(Long tecnicoId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.ANDAMENTO,
            null,
            tecnicoId,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados em andamento para este técnico no momento.");
        }
        
        return chamados;
    }

    // o técnico pode resolver o chamado, mas apenas se ele estiver em andamento
    public Chamado chamadoResolvido(Chamado chamado) {
        
        if (chamado.getStatus() != StatusChamado.ANDAMENTO) {
            throw new ExceptionMsg("O chamado não está em andamento e não pode ser resolvido.");
        }

        chamado.setStatus(StatusChamado.RESOLVIDO);
        return chamadoRepository.save(chamado);
    }
    
    public Page<Chamado> ChamadosResolvidosUsuario(Long usuarioId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.RESOLVIDO,
            usuarioId,
            null,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados resolvidos para este usuário no momento.");
        }
        
        return chamados;
    }
    
    public Page<Chamado> ChamadosResolvidosTecnico(Long tecnicoId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.RESOLVIDO,
            null,
            tecnicoId,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados resolvidos para este técnico no momento.");
        }
        
        return chamados;
    }

    // o cliente pode fechar o chamado, mas apenas se ele estiver resolvido
    public String chamadoFechado(Chamado chamado) { 
        
        if (chamado.getStatus() != StatusChamado.RESOLVIDO) {
            throw new ExceptionMsg("O chamado não está resolvido e não pode ser fechado.");
        }

        chamado.setStatus(StatusChamado.FECHADO);
        chamado.setDataFechamento(LocalDateTime.now());
        chamadoRepository.save(chamado);

        return "Chamado fechado com sucesso.";
    }
    
    public Page<Chamado> ChamadosFechadosUsuario(Long usuarioId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.FECHADO,
            usuarioId,
            null,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados fechados para este usuário no momento.");
        }
        
        return chamados;
    }
    
    public Page<Chamado> ChamadosFechadosTecnico(Long tecnicoId, Pageable pageable) {
        
        Page<Chamado> chamados = chamadoRepository.chamados(
            StatusChamado.FECHADO,
            null,
            tecnicoId,
            pageable
        );
        
        if (chamados.isEmpty() || chamados.getTotalElements() == 0) {
            throw new ExceptionMsg("Não há chamados fechados para este técnico no momento.");
        }
        
        return chamados;
    }
    
    
}
