package com.helpdesk.dto;

import com.helpdesk.enuns.StatusChamado;
import com.helpdesk.enuns.PrioridadeChamado;
import com.helpdesk.models.Chamado;
import java.time.LocalDateTime;

public record ChamadoResponseDTO(
    long id,
    String titulo,
    String descricao,
    StatusChamado status,
    PrioridadeChamado prioridade,
    Long clienteId,
    String clienteNome,
    Long tecnicoId,
    String tecnicoNome,
    LocalDateTime dataAbertura,
    LocalDateTime dataFechamento,
    String observacoes
) {
    // Construtor de conversão a partir da entidade Chamado
    public ChamadoResponseDTO(Chamado chamado) {
        this(
            chamado.getId(),
            chamado.getTitulo(),
            chamado.getDescricao(),
            chamado.getStatus(),
            chamado.getPrioridade(),
            chamado.getCliente().getId(),
            chamado.getCliente().getNome(),
            chamado.getTecnico() != null ? chamado.getTecnico().getId() : null,
            chamado.getTecnico() != null ? chamado.getTecnico().getNome() : null,
            chamado.getDataAbertura(),
            chamado.getDataFechamento(),
            chamado.getObservacoes()
        );
    }
}
