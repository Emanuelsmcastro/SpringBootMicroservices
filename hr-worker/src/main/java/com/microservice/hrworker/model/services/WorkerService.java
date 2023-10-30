package com.microservice.hrworker.model.services;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.microservice.hrworker.infra.exceptions.AuthorizationException;
import com.microservice.hrworker.infra.exceptions.WorkerNotFoundException;
import com.microservice.hrworker.model.dtos.UserResponse;
import com.microservice.hrworker.model.entities.Role;
import com.microservice.hrworker.model.entities.Worker;
import com.microservice.hrworker.model.repositories.WorkerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkerService {
	
	private static final List<String> allowedRoles = Arrays.asList("ROLE_OPERATOR", "ROLE_ADMIN");
	private final WorkerRepository rep;
	private final JwtService jwtService;
	
	public Worker findById(Long id, String token) {
		haveAuthorization(token);
		try{
			return rep.findById(id).get();
		} catch(NoSuchElementException e) {
			throw new WorkerNotFoundException(String.format("Worker (id:%d) not found.", id));
		}
	}
	
	public Worker findByName(String name, String token) {
		haveAuthorization(token);
		Worker worker = rep.findByNameIgnoreCase(name);
		if(worker == null)
			throw new WorkerNotFoundException(String.format("Worker (name:%s) not found.", name));
		return worker;
	}
	
	public List<Worker> findAll(String token){
		haveAuthorization(token);
		return rep.findAll();
	}
	
	private Boolean haveAuthorization(String token) {
		UserResponse user = jwtService.getAuthenticatedUser(token);
		for(Role role: user.roles()) {
			for(String allowedRole: allowedRoles) {
				if(role.getName().equals(allowedRole))
					return true;
			}
		}
		throw new AuthorizationException("You don't have permissions.");
	}
}
