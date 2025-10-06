package com.clinique.yannic.gestion_clinique_backend.services;

import com.clinique.yannic.gestion_clinique_backend.models.Utilisateur;
import com.clinique.yannic.gestion_clinique_backend.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository; // Assurez-vous d'avoir ce repository

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utilisateur> user = utilisateurRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        return new UserDetailsImpl(user.get()); // Créez une classe UserDetailsImpl pour encapsuler Utilisateur
    }
}
