package com.microservice.hroauth.model.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.microservice.hroauth.infra.exceptions.AuthorizationException;
import com.microservice.hroauth.model.dtos.AuthUser;
import com.microservice.hroauth.model.dtos.TokenDTO;
import com.microservice.hroauth.model.entities.User;
import com.microservice.hroauth.model.repositories.UserRepository;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final String credentialsFail = "Credential is incorrect.";
	
	public TokenDTO login(AuthUser authUser) {
		User user = userRepository.findByUsername(authUser.name()).orElseThrow(() -> new AuthorizationException(credentialsFail));
		validatePassword(authUser.password(), user.getPassword());
		String accessToken = jwtService.createToken(authUser, user.getRoles());
		return new TokenDTO(accessToken);
		
	}
	
	public TokenDTO validateToken(String token) {
		validateExistingToken(token);
		jwtService.validateAccessToken(token);
		return new TokenDTO(token);
	}
	
	private void validatePassword(String rawPassword, String encodedPassword) {
		if(!passwordEncoder.matches(rawPassword, encodedPassword))
			throw new AuthorizationException(credentialsFail);
	}
	
	private void validateExistingToken(String token) {
		if(ObjectUtils.isEmpty(token))
			throw new ValidationException("Token not found.");
	}
	
}
