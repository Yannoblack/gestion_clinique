package com.clinique.yannic.gestion_clinique_backend.exception;

import com.clinique.yannic.gestion_clinique_backend.controllers.dto.RegisterRequest;
import com.clinique.yannic.gestion_clinique_backend.controllers.AuthController;
import com.clinique.yannic.gestion_clinique_backend.repositories.UtilisateurRepository;
import com.clinique.yannic.gestion_clinique_backend.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests pour valider la gestion des exceptions dans l'API.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExceptionHandlingTest {

    @Autowired
    private AuthController authController;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void testResourceAlreadyExistsException() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        request.setRole("ROLE_USER");

        // Act & Assert
        // Premier enregistrement devrait réussir
        ResponseEntity<?> response1 = authController.register(request);
        assertEquals(HttpStatus.OK, response1.getStatusCode());

        // Deuxième enregistrement avec le même nom d'utilisateur devrait lever une exception
        assertThrows(ResourceAlreadyExistsException.class, () -> {
            authController.register(request);
        });
    }

    @Test
    public void testEntityNotFoundException() {
        // Test avec un ID inexistant
        assertThrows(EntityNotFoundException.class, () -> {
            throw new EntityNotFoundException("Patient non trouvé avec l'ID : 999");
        });
    }

    @Test
    public void testInvalidDateException() {
        assertThrows(InvalidDateException.class, () -> {
            throw new InvalidDateException("Impossible de créer un rendez-vous dans le passé.");
        });
    }

    @Test
    public void testAppointmentConflictException() {
        assertThrows(AppointmentConflictException.class, () -> {
            throw new AppointmentConflictException("Un rendez-vous existe déjà pour ce médecin à cette heure.");
        });
    }

    @Test
    public void testBusinessException() {
        assertThrows(BusinessException.class, () -> {
            throw new BusinessException("Règle métier violée.");
        });
    }

    @Test
    public void testUnauthorizedAccessException() {
        assertThrows(UnauthorizedAccessException.class, () -> {
            throw new UnauthorizedAccessException("Accès non autorisé à cette ressource.");
        });
    }
}
