package com.helpdesk.models;

import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity 
@SequenceGenerator(name = "acesso_seq", sequenceName = "acesso_seq", allocationSize = 1, initialValue = 1)
public class Acesso implements GrantedAuthority {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acesso_seq")
    private long id;

    @Column(nullable = false, unique = true)
    private String acesso;

    @Override
    public String getAuthority() {
        return this.acesso;
    }
}
