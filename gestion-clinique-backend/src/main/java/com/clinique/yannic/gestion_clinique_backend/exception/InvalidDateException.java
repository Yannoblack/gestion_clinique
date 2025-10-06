package com.clinique.yannic.gestion_clinique_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception levée lors de dates invalides (rendez-vous dans le passé, etc.).
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidDateException extends RuntimeException {
    
    public InvalidDateException(String message) {
        super(message);
    }
    
    public InvalidDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
