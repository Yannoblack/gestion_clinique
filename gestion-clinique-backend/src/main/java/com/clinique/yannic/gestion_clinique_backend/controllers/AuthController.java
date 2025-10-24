package com.clinique.yannic.gestion_clinique_backend.controllers;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.LoginRequest;
import com.clinique.yannic.gestion_clinique_backend.controllers.dto.RegisterRequest;
import com.clinique.yannic.gestion_clinique_backend.exception.ResourceAlreadyExistsException;
import com.clinique.yannic.gestion_clinique_backend.models.Utilisateur;
import com.clinique.yannic.gestion_clinique_backend.repositories.UtilisateurRepository;
import com.clinique.yannic.gestion_clinique_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest body) {
        String username = body.getUsername();
        String password = body.getPassword();

        Optional<Utilisateur> userOpt = utilisateurRepository.findByUsername(username);
        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            throw new BadCredentialsException("Identifiants invalides");
        }
        String token = jwtUtil.generateToken(username);
        String role = userOpt.get().getRole();
        return ResponseEntity.ok(Map.of("token", token, "role", role));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest body) {
        String username = body.getUsername();
        String password = body.getPassword();
        String role = body.getRole();

        if (utilisateurRepository.findByUsername(username).isPresent()) {
            throw new ResourceAlreadyExistsException("Nom d'utilisateur déjà utilisé");
        }

        Utilisateur user = new Utilisateur();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        utilisateurRepository.save(user);

        String token = jwtUtil.generateToken(username);
        return ResponseEntity.ok(Map.of("token", token, "role", role));
    }
}
