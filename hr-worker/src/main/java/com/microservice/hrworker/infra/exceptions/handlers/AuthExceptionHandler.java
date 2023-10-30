package com.microservice.hrworker.infra.exceptions.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservice.hrworker.infra.exceptions.AuthorizationException;
import com.microservice.hrworker.infra.exceptions.ExceptionFormatter;
import com.microservice.hrworker.infra.exceptions.ValidationException;

@ControllerAdvice
public class AuthExceptionHandler {

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> validationExceptionHandler(ValidationException e) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ExceptionFormatter formatter = new ExceptionFormatter(status.value(), e.getMessage(), Instant.now());
		return new ResponseEntity<>(formatter, status);
	}

	@ExceptionHandler(AuthorizationException.class)
	public ResponseEntity<?> validationExceptionHandler(AuthorizationException e) {
		HttpStatus status = HttpStatus.UNAUTHORIZED;
		ExceptionFormatter formatter = new ExceptionFormatter(status.value(), e.getMessage(), Instant.now());
		return new ResponseEntity<>(formatter, status);
	}

}
