package com.helpdesk.services;

import com.helpdesk.enuns.StatusChamado;
import com.helpdesk.exception.ExceptionMsg;
import com.helpdesk.models.Chamado;
import com.helpdesk.models.Usuario;
import com.helpdesk.repository.ChamadoRepository;
import com.helpdesk.repository.UsuarioRepository;
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

    // o usuário pode cancelar o chamado, mas apenas se ele estiver em aberto ou em andamento
    public String cancelarChamado(Long id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ExceptionMsg("Chamado não encontrado com o ID: " + id));

        if (chamado.getStatus() == StatusChamado.CANCELADO) {
            throw new ExceptionMsg("O chamado já está cancelado.");
        }else if (chamado.getStatus() == StatusChamado.FECHADO) {
            throw new ExceptionMsg("O chamado já está fechado e não pode ser cancelado.");
        } else if (chamado.getStatus() == StatusChamado.RESOLVIDO) {
            throw new ExceptionMsg("O chamado já está resolvido e não pode ser cancelado.");
        }

        chamado.setStatus(StatusChamado.CANCELADO);
        chamadoRepository.save(chamado);

        return "Chamado cancelado com sucesso.";
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

    // o técnico pode resolver o chamado, mas apenas se ele estiver em andamento
    public Chamado chamadoResolvido(Chamado chamado) {
        if (chamado.getStatus() != StatusChamado.ANDAMENTO) {
            throw new ExceptionMsg("O chamado não está em andamento e não pode ser resolvido.");
        }

        chamado.setStatus(StatusChamado.RESOLVIDO);
        return chamadoRepository.save(chamado);
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
}
