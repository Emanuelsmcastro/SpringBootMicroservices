package com.microservice.hroauth.model.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.hroauth.model.dtos.AuthUser;
import com.microservice.hroauth.model.dtos.TokenDTO;
import com.microservice.hroauth.model.services.AuthService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthResource {
	
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> login(@RequestBody AuthUser authUser){
		log.info(authUser.toString());
		return ResponseEntity.ok().body(authService.login(authUser));
	}
	
	@GetMapping("/token/validate")
	public ResponseEntity<TokenDTO> login(@RequestHeader String token){
		return ResponseEntity.ok().body(authService.validateToken(token));
	}

}
