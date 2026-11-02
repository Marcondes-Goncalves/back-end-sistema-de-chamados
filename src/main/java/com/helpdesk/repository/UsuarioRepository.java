package com.helpdesk.repository;

import com.helpdesk.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    @Query(value = "SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario buscarPorLogin(String email);

    /**
     * Consulta a constraint que está impedindo a inserção do acesso padrão para o usuário recém-criado.
     * Por Algum motivo, o JPA não está conseguindo inserir o acesso padrão para o usuário recém-criado. 
     * essa consulta retorna a constraint que está impedindo a inserção.
     * @return
     */
    @Query(value = "SELECT constraint_name " +
                   "FROM information_schema.constraint_column_usage " +
                   "WHERE table_name = 'usuario_acessos' " +
                   "and column_name = 'acesso_id' " +
                   "and constraint_name <> 'unique_usuario_acessos';", nativeQuery = true)
    String consultaConstraintRole();

    /**
     * Insere o acesso padrão (ROLE_CLIENTE) para o usuário recém-criado.
     * @param idUsuario
     */
    @Transactional
    @Modifying
    @Query(value = "insert into usuario_acessos (usuario_id, acesso_id) values(?1, (select id from acesso WHERE acesso = 'ROLE_CLIENTE'));", nativeQuery = true)
    void insereAcessoRolePadrao(Long idUsuario);

    /**
     * Insere um acesso específico para o usuário.
     * @param idUsuario
     * @param role
     */
    @Transactional
    @Modifying
    @Query(value = "insert into usuario_acessos (usuario_id, acesso_id) values(?1, (select id from acesso WHERE acesso = ?2));", nativeQuery = true)
    void insereAcessoRole(Long idUsuario, String role);
}
