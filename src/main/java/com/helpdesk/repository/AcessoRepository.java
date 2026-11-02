package com.helpdesk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.helpdesk.models.Acesso;

public interface AcessoRepository extends JpaRepository<Acesso, Long> {

    @Query(value = "SELECT * FROM Acesso WHERE acesso = :acesso", nativeQuery = true)
    Acesso buscarAcessoPorDescricao(String acesso);
}
