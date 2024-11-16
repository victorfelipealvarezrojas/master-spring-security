package com.cursos.api.springsecuritycourse.persistence.entity;

import com.cursos.api.springsecuritycourse.persistence.util.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    // permisos sobre mi rol
    // convierto mis enum de permisos a objeto de tipo GrantedAuthority
    // GrantedAuthority:  Representa la autorización o rol que se otorga al usuario autenticado.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) return null;
        if (role.getPermissions() == null) return null;

        List<SimpleGrantedAuthority> authorities = role.getPermissions().stream()
                .map(Enum::name) // convierto a string el nombre del permiso (Enum)
                .map(SimpleGrantedAuthority::new) // convierto los permisos a una lista de tipo GrantedAuthority
                .collect(Collectors.toList()); /// retorna una lista mutable de los elementos de la secuencia dada  .toList();  ES (inmutable)

        // Esto lo hago para que el rol también sea considerado como un permiso
        // ademas a los roles se les agrega un prefijo "ROLE_" para que spring los reconozca como roles
        // y no como permisos, el siguente add lo qeu hace es agregar el rol como un permiso
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role.name()));

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

/***********************************************************
 * Spring Security
 * FUNCIONA EN BASE A PERMISOS Y NO TIENE NINGUN METODO QUE ENTREGUE
 * EL ROL DE UN USUARIO, POR LO QUE DEBEMOS AGREGAR EL ROL COMO UN PERMISO (AUTHORITY)
 * Y PARA DIFERENCIARLO DE LOS PERMISOS, DEBEMOS AGREGAR UN PREFIJO "ROLE_" Y AHI ESTA EL TRUCO
 * ************************************************************/