package com.helpdesk.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


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

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
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
