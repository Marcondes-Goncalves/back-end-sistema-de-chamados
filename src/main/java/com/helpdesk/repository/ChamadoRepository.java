package com.helpdesk.repository;

import com.helpdesk.enuns.StatusChamado;
import com.helpdesk.models.Chamado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {

    /**
     * Retorna uma página de chamados filtrados pelo status, ID do usuário e ID do técnico.
     * Se o ID do usuário ou do técnico for nulo, o filtro correspondente será ignorado.
     * OBS: aqui não é necessário utilizar o countQuery, pois o Spring Data JPA consegue 
     * gerar automaticamente a contagem de registros para a paginação.
     * Mas se a consultar tiver joins e group by será necessário fornecer o countQuery explicitamente.
     */
    @Query(value = "SELECT c FROM Chamado c WHERE c.status = :status AND " + 
         "(:usuarioId IS NULL OR c.cliente.id = :usuarioId) AND " + 
         "(:tecnicoId IS NULL OR c.tecnico.id = :tecnicoId)")
    Page<Chamado> chamados(StatusChamado status, Long usuarioId, Long tecnicoId, Pageable pageable);
    
}
