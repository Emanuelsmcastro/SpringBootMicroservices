package com.microservice.paroll.infra.exceptions;

public class FeignClientNotFound extends RuntimeException{
	private static final long serialVersionUID = 1L;

	public FeignClientNotFound(String msg) {
		super(msg);
	}
}
