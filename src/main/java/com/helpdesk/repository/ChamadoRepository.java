package com.helpdesk.repository;

import com.helpdesk.models.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    @Query("SELECT c FROM Chamado c WHERE c.status = 'ABERTO'")
    List<Chamado> chamadosAbertos();

    @Query("SELECT c FROM Chamado c WHERE c.status = 'CANCELADO'")
    List<Chamado> chamadosCancelados();

    @Query("SELECT c FROM Chamado c WHERE c.status = 'ANDAMENTO'")
    List<Chamado> chamadosEmAndamento();

    @Query("SELECT c FROM Chamado c WHERE c.status = 'RESOLVIDO'")
    List<Chamado> chamadosResolvidos();

    @Query("SELECT c FROM Chamado c WHERE c.status = 'FECHADO'")
    List<Chamado> chamadosFechados();
}
