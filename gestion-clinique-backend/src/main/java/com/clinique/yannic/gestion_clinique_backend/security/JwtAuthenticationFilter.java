package com.clinique.yannic.gestion_clinique_backend.security;

import com.clinique.yannic.gestion_clinique_backend.repositories.UtilisateurRepository;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	private final UtilisateurRepository utilisateurRepository;
	private final JwtUtil jwtUtil;

	// 1. Injection par constructeur (meilleure pratique)
	public JwtAuthenticationFilter(UtilisateurRepository utilisateurRepository, JwtUtil jwtUtil) {
		this.utilisateurRepository = utilisateurRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
									@NonNull HttpServletResponse response,
									@NonNull FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader("Authorization");

		// Si pas de header ou si le format n'est pas "Bearer ", on continue.
		// La requête sera soit autorisée (pour les endpoints publics), soit rejetée (401).
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// 5. On ne traite le token que si aucun utilisateur n'est déjà authentifié
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			final String token = authHeader.substring(7); // Extrait le token

			try {
				DecodedJWT decodedJWT = jwtUtil.verify(token);
				String username = decodedJWT.getSubject();

				// On s'assure que le username n'est pas null avant de chercher dans la BDD
				if (username != null) {
					// On utilise l'interface UserDetails pour plus de flexibilité
					UserDetails userDetails = this.utilisateurRepository.findByUsername(username)
							.orElse(null);

					if (userDetails != null) {
						// Création du jeton d'authentification
						UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails,
								null, // Les credentials sont nuls pour une authentification par JWT
								userDetails.getAuthorities()
						);

						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

						// On place l'authentification dans le contexte de sécurité de Spring
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
						logger.debug("Utilisateur '{}' authentifié avec succès.", username);
					}
				}
			} catch (JWTVerificationException e) {
				// 3 & 4. Gestion d'erreur spécifique et journalisation propre
				logger.error("Erreur de validation du token JWT : {}", e.getMessage());
			} catch (Exception e) {
				// Attrape toute autre erreur inattendue
				logger.error("Erreur inattendue lors de l'authentification JWT : {}", e.getMessage());
			}
		}

		filterChain.doFilter(request, response);
	}
}