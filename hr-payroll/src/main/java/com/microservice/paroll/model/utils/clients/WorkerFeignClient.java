package com.microservice.paroll.model.utils.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservice.paroll.model.entities.Worker;

@Component
@FeignClient(name = "hr-worker", path = "/api/v1/workers")
public interface WorkerFeignClient {

	@GetMapping(value = "/{id}")
	public ResponseEntity<Worker> findById(@PathVariable(value = "id") Long id);
}
