package com.microservice.paroll.infra.exceptions;


public class ServiceConnectionException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public ServiceConnectionException(String msg) {
		super(msg);
	}

}
