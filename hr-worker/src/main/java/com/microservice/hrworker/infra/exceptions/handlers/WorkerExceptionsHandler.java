package com.microservice.hrworker.infra.exceptions.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.microservice.hrworker.infra.exceptions.ExceptionFormatter;
import com.microservice.hrworker.infra.exceptions.WorkerNotFoundException;

@ControllerAdvice
public class WorkerExceptionsHandler{

	@ExceptionHandler(WorkerNotFoundException.class)
	public ResponseEntity<?> workerNotFoundHandler(WorkerNotFoundException e){
		HttpStatus status = HttpStatus.NOT_FOUND;
		ExceptionFormatter formatter = new ExceptionFormatter(status.value(), e.getMessage(), Instant.now());
		return new ResponseEntity<>(formatter, status);
	}

}
