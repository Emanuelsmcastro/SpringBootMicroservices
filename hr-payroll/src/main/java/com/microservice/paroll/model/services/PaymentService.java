package com.microservice.paroll.model.services;

import java.net.ConnectException;

import org.springframework.stereotype.Service;

import com.microservice.paroll.infra.exceptions.FeignClientNotFound;
import com.microservice.paroll.infra.exceptions.ServiceConnectionException;
import com.microservice.paroll.model.entities.Payment;
import com.microservice.paroll.model.entities.Worker;
import com.microservice.paroll.model.utils.clients.WorkerFeignClient;

import feign.FeignException.NotFound;
import feign.FeignException.ServiceUnavailable;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
	
	private final WorkerFeignClient workerFeignClient;
	
	@CircuitBreaker(name = "hr-worker", fallbackMethod = "getPaymentCircuitBreaker")
	public Payment getPayment(Long workerId, Integer days) {
		try {
			Worker worker = workerFeignClient.findById(workerId).getBody();
			return new Payment(worker.getName(), worker.getDailyIncome(), days);
		} catch(NotFound e) {
			throw new FeignClientNotFound(String.format("WorkerID (id:%d) not found", workerId));
		} 
	}
	
	@SuppressWarnings("unused")
	private Payment getPaymentCircuitBreaker(Long workerId, Integer days, ConnectException e) {
		log.warn(e.getMessage());
		throw new ServiceConnectionException("hr-worker is down.");
	}
	
	@SuppressWarnings("unused")
	private Payment getPaymentCircuitBreaker(Long workerId, Integer days, ServiceUnavailable e) {
		log.warn(e.getMessage());
		throw new ServiceConnectionException("hr-worker is down.");
	}
	
	@SuppressWarnings("unused")
	private Payment getPaymentCircuitBreaker(Long workerId, Integer days, CallNotPermittedException e) {
		log.warn(e.getMessage());
		throw new ServiceConnectionException("CircuitBreaker: hr-worker is OPEN.");
	}
}
