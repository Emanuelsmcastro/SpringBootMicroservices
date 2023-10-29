package com.microservice.hrworker.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.hrworker.model.entities.Worker;

public interface WorkerRepository extends JpaRepository<Worker, Long>{
	
	public Worker findByNameIgnoreCase (String name);
}
