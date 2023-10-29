package com.microservice.paroll.infra.exceptions.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservice.paroll.infra.exceptions.ExceptionFormatter;
import com.microservice.paroll.infra.exceptions.FeignClientNotFound;
import com.microservice.paroll.infra.exceptions.ServiceConnectionException;

@ControllerAdvice
public class FeignClientExceptionHandler extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler(FeignClientNotFound.class)
	public ResponseEntity<?> feignNotFoundHandler(FeignClientNotFound e){
		HttpStatus status = HttpStatus.NOT_FOUND;
		ExceptionFormatter formatter = new ExceptionFormatter(status.value(), e.getMessage(), Instant.now());
		return new ResponseEntity<>(formatter, status);
	}
	
	@ExceptionHandler(ServiceConnectionException.class)
	public ResponseEntity<?> feignNotFoundHandler(ServiceConnectionException e){
		HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;
		ExceptionFormatter formatter = new ExceptionFormatter(status.value(), e.getMessage(), Instant.now());
		return new ResponseEntity<>(formatter, status);
	}

}
