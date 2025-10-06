package com.clinique.yannic.gestion_clinique_backend.services;

import com.clinique.yannic.gestion_clinique_backend.models.Utilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private final Utilisateur user;

    public UserDetailsImpl(Utilisateur user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Remplacez par votre logique pour obtenir les rôles
        List<String> roles = user.getRoles(); // Supposons que vous ayez une méthode getRoles()
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Remplacez par votre logique
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Remplacez par votre logique
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Remplacez par votre logique
    }

    @Override
    public boolean isEnabled() {
        return true; // Remplacez par votre logique
    }
}
