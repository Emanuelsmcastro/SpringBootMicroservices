package com.microservice.hrworker.model.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.hrworker.model.entities.Worker;
import com.microservice.hrworker.model.repositories.WorkerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkerService {
	
	private final WorkerRepository rep;
	
	public Worker findById(Long id) {
		return rep.findById(id).get();
	}
	
	public Worker findByName(String name) {
		return rep.findByNameIgnoreCase(name);
	}
	
	public List<Worker> findAll(){
		return rep.findAll();
	}

}
