package com.helpdesk.models;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter 
@Setter 
@NoArgsConstructor 
@Entity 
@SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_seq", allocationSize = 1, initialValue = 1)
public class Usuario implements UserDetails {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    private long id;

    @NotNull 
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    private Boolean ativo = true;

    private LocalDateTime criadoEm;

    /**
     * Relação entre o usuário e seus acessos.
     * Um usuário pode ter múltiplos acessos.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_acessos", 
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "acesso_id"}, name = "unique_usuario_acessos"),
        joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "usuario", foreignKey = @ForeignKey(name = "fk_usuario", value = ConstraintMode.CONSTRAINT), unique = false),
        inverseJoinColumns = @JoinColumn(name = "acesso_id", referencedColumnName = "id", table = "acessos", foreignKey = @ForeignKey(name = "fk_acesso", value = ConstraintMode.CONSTRAINT), unique = false)
    )
    private List<Acesso> acessos = new ArrayList<Acesso>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.acessos; // Retornar as autoridades do usuário, se houver
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
