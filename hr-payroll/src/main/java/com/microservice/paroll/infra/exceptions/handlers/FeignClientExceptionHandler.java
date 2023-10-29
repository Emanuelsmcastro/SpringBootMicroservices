package com.microservice.paroll.infra.exceptions.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservice.paroll.infra.exceptions.ExceptionFormatter;
import com.microservice.paroll.infra.exceptions.FeignClientNotFound;

@ControllerAdvice
public class FeignClientExceptionHandler extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	@ExceptionHandler(FeignClientNotFound.class)
	public ResponseEntity<?> feignNotFoundHandler(FeignClientNotFound e){
		HttpStatus status = HttpStatus.NOT_FOUND;
		ExceptionFormatter formatter = new ExceptionFormatter(status.value(), e.getMessage(), Instant.now());
		return new ResponseEntity<>(formatter, status);
	}

}
