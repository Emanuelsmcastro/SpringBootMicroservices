package com.microservice.paroll.infra.exceptions;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String msg) {
		super(msg);
	}

}
