package com.helpdesk.services;

import com.helpdesk.enuns.StatusChamado;
import com.helpdesk.exception.ExceptionMsg;
import com.helpdesk.models.Chamado;
import com.helpdesk.models.Usuario;
import com.helpdesk.repository.ChamadoRepository;
import com.helpdesk.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioRepository usuarioRepository;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioRepository usuarioRepository) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Chamado criarChamado(Chamado chamado) {
        chamado.setStatus(StatusChamado.ABERTO);
        return chamadoRepository.save(chamado);
    }

    public Chamado chamadoEmAndamento(Chamado chamado, Long idTecnico) {

        Usuario tecnico = usuarioRepository.findById(idTecnico)
                .orElseThrow(() -> new ExceptionMsg("Técnico não encontrado com o ID: " + idTecnico));

        chamado.setTecnico(tecnico);
        chamado.setStatus(StatusChamado.ANDAMENTO);

        return chamadoRepository.save(chamado);
    }

}
