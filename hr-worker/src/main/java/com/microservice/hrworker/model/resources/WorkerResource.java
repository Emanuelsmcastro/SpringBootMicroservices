package com.microservice.hrworker.model.resources;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.hrworker.model.entities.Worker;
import com.microservice.hrworker.model.services.WorkerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/workers")
public class WorkerResource {
	
	private final WorkerService service;
	
	@GetMapping
	public ResponseEntity<List<Worker>> getAllWorkers(){
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Worker> findById(@PathVariable Long id){
		return ResponseEntity.ok().body(service.findById(id));
	}
	
	@GetMapping(value = "/search")
	public ResponseEntity<Worker> findByName(@RequestParam String name){
		return ResponseEntity.ok().body(service.findByName(name));
	}
}
