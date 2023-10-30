package com.microservice.paroll.model.services;

import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.paroll.infra.exceptions.AuthorizationException;
import com.microservice.paroll.infra.exceptions.ValidationException;
import com.microservice.paroll.model.dtos.UserResponse;
import com.microservice.paroll.model.entities.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

@Service
@RefreshScope
@RequiredArgsConstructor
public class JwtService {
	private static final String SEPARATOR = " ";
	private static final Integer TOKEN_INDEX = 1;
	private final ObjectMapper mapper = new ObjectMapper();

	@Value("${secret.token.secret-key}")
	private String secretKey;

	public UserResponse getAuthenticatedUser(String token) {
		Claims tokenClaims = getClaims(token);
		String username = (String) tokenClaims.get("username");
		@SuppressWarnings("unchecked")
		Set<Role> roles = (Set<Role>) mapper.convertValue(tokenClaims.get("roles"), Set.class).stream()
				.map(object -> mapper.convertValue(object, Role.class)).collect(Collectors.toSet());
		return new UserResponse(username, roles);
	}

	private SecretKey generateSign() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	private Claims getClaims(String token) {
		String accessToken = extractToken(token);
		try {
			return Jwts.parserBuilder().setSigningKey(generateSign()).build().parseClaimsJws(accessToken).getBody();

		} catch (Exception e) {
			throw new AuthorizationException("Invalid token: " + e.getMessage());
		}
	}

	private String extractToken(String token) {
		if (ObjectUtils.isEmpty(token))
			throw new ValidationException("The access token was not informed.");
		if (token.contains(SEPARATOR))
			return token.split(SEPARATOR)[TOKEN_INDEX];
		return token;
	}
}
