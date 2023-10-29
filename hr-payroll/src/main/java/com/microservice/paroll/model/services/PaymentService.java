package com.microservice.paroll.model.services;

import org.springframework.stereotype.Service;

import com.microservice.paroll.model.entities.Payment;
import com.microservice.paroll.model.entities.Worker;
import com.microservice.paroll.model.utils.clients.WorkerFeignClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {
	
	private final WorkerFeignClient workerFeignClient;
	
	public Payment getPayment(Long workerId, Integer days) {
		Worker worker = workerFeignClient.findById(workerId).getBody();
		return new Payment(worker.getName(), worker.getDailyIncome(), days);
	}
	
}
