package com.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.helpdesk.models.Chamado;

public interface ChamadoRepository extends JpaRepository<Chamado, Long> {
    
}
