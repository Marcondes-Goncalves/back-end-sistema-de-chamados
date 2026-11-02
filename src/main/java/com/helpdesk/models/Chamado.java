package com.helpdesk.models;

import com.helpdesk.enuns.PrioridadeChamado;
import com.helpdesk.enuns.StatusChamado;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor 
@Entity 
@SequenceGenerator(name = "chamado_seq", sequenceName = "chamado_seq", allocationSize = 1, initialValue = 1)
public class Chamado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chamado_seq")
    private long id;

    @NotNull(message = "O título do chamado é obrigatório.")
    private String titulo;

    @NotNull(message = "A descrição do chamado é obrigatória.")
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusChamado status;

    @NotNull(message = "A prioridade do chamado é obrigatória.")
    @Enumerated(EnumType.STRING)
    private PrioridadeChamado prioridade;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull(message = "O cliente do chamado é obrigatório.")
    private Usuario cliente;

    @ManyToOne(cascade = CascadeType.ALL)
    private Usuario tecnico;

    @NotNull(message = "A data de abertura do chamado é obrigatória.")
    private LocalDateTime dataAbertura;

    private LocalDateTime dataFechamento;

    @Column(columnDefinition = "TEXT")
    private String observacoes;

}
