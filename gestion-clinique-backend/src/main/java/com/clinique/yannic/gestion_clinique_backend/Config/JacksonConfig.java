package com.clinique.yannic.gestion_clinique_backend.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Configuration minimale pour les dates Java 8+
        mapper.registerModule(new JavaTimeModule());
        
        // Désactiver l'écriture des dates en timestamps
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Ignorer les propriétés inconnues lors de la désérialisation
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        
        // Permettre les propriétés vides
        mapper.disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES);
        
        // Permettre les propriétés manquantes
        mapper.disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES);
        
        // PAS de stratégie de nommage pour laisser @JsonProperty fonctionner
        // PAS de configuration de sérialisation qui interfère
        
        return mapper;
    }
}
