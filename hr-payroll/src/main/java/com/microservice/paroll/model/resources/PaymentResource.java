package com.microservice.paroll.model.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microservice.paroll.model.entities.Payment;
import com.microservice.paroll.model.services.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/payments")
public class PaymentResource {

	private final PaymentService service;

	@GetMapping(value = "/get/{workerId}")
	public ResponseEntity<Payment> getPayment(@PathVariable Long workerId, @RequestParam(name = "days") Integer days ){
		return ResponseEntity.ok().body(service.getPayment(workerId, days));
	}
}
