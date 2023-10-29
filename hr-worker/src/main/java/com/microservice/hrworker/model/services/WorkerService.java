package com.microservice.hrworker.model.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.microservice.hrworker.infra.exceptions.WorkerNotFoundException;
import com.microservice.hrworker.model.entities.Worker;
import com.microservice.hrworker.model.repositories.WorkerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkerService {
	
	private final WorkerRepository rep;
	
	public Worker findById(Long id) {
		try{
			return rep.findById(id).get();
		} catch(NoSuchElementException e) {
			throw new WorkerNotFoundException(String.format("Worker (id:%d) not found.", id));
		}
	}
	
	public Worker findByName(String name) {
		Worker worker = rep.findByNameIgnoreCase(name);
		if(worker == null)
			throw new WorkerNotFoundException(String.format("Worker (name:%s) not found.", name));
		return worker;
	}
	
	public List<Worker> findAll(){
		return rep.findAll();
	}

}
