package com.clinique.yannic.gestion_clinique_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lors de conflits de rendez-vous (même médecin, même horaire).
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class AppointmentConflictException extends RuntimeException {
    
    public AppointmentConflictException(String message) {
        super(message);
    }
    
    public AppointmentConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
