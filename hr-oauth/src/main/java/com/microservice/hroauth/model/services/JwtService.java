package com.microservice.hroauth.model.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.crypto.SecretKey;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.microservice.hroauth.infra.exceptions.AuthorizationException;
import com.microservice.hroauth.model.dtos.AuthUser;
import com.microservice.hroauth.model.entities.Role;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
	
	private static final Integer EXPIRED_TIME_IN_HOURS = 24;
	private static final String SEPARATOR = " ";
	private static final Integer TOKEN_INDEX = 1;
	
	@Value("${secret.token.secret-key}")
	private String secretKey;
	
	public String createToken(AuthUser user, Set<Role> roles) {
		Map<String, Object> data = new HashMap<>();
		data.put("username", user.name());
		data.put("roles", roles);
		return Jwts.builder()
				.setClaims(data)
				.setExpiration(generateExpiresAt())
				.signWith(generateSign())
				.compact();
	}
	
	public void validateAccessToken(String token) {
		String accessToken = extractToken(token);
		try {
			Jwts.parserBuilder()
			.setSigningKey(generateSign())
			.build()
			.parseClaimsJws(accessToken)
			.getBody();
		} catch (Exception e) {
			throw new AuthorizationException("Invalid token: " + e.getMessage());
		}
	}
	
	private Date generateExpiresAt() {
		return Date.from(LocalDateTime.now()
				.plusHours(EXPIRED_TIME_IN_HOURS)
				.atZone(ZoneId.systemDefault()).toInstant());
	}
	
	private SecretKey generateSign() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	private String extractToken(String token) {
		if(ObjectUtils.isEmpty(token))
			throw new ValidationException("The access token was not informed.");
		if(token.contains(SEPARATOR))
			return token.split(SEPARATOR)[TOKEN_INDEX];
		return token;

	}

}
