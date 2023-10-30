package com.microservice.paroll.model.services;

import java.net.ConnectException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microservice.paroll.infra.exceptions.AuthorizationException;
import com.microservice.paroll.infra.exceptions.FeignClientNotFound;
import com.microservice.paroll.infra.exceptions.ServiceConnectionException;
import com.microservice.paroll.model.dtos.UserResponse;
import com.microservice.paroll.model.entities.Payment;
import com.microservice.paroll.model.entities.Role;
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
	private final JwtService jwtService;
	private static final List<String> allowedRoles = Arrays.asList("ROLE_ADMIN");

	@CircuitBreaker(name = "hr-worker", fallbackMethod = "getPaymentCircuitBreaker")
	public Payment getPayment(Long workerId, Integer days, String token) {
		haveAuthorization(token);
		try {
			Worker worker = workerFeignClient.findById(workerId, token).getBody();
			return new Payment(worker.getName(), worker.getDailyIncome(), days);
		} catch (NotFound e) {
			throw new FeignClientNotFound(String.format("WorkerID (id:%d) not found", workerId));
		}
	}

	@SuppressWarnings("unused")
	private Payment getPaymentCircuitBreaker(Long workerId, Integer days, String token, ConnectException e) {
		log.warn(e.getMessage());
		throw new ServiceConnectionException("hr-worker is down.");
	}

	@SuppressWarnings("unused")
	private Payment getPaymentCircuitBreaker(Long workerId, Integer days, String token, ServiceUnavailable e) {
		log.warn(e.getMessage());
		throw new ServiceConnectionException("hr-worker is down.");
	}

	@SuppressWarnings("unused")
	private Payment getPaymentCircuitBreaker(Long workerId, Integer days, String token, CallNotPermittedException e) {
		log.warn(e.getMessage());
		throw new ServiceConnectionException("CircuitBreaker: hr-worker is OPEN.");
	}

	private Boolean haveAuthorization(String token) {
		UserResponse user = jwtService.getAuthenticatedUser(token);
		for (Role role : user.roles()) {
			for (String allowedRole : allowedRoles) {
				if (role.getName().equals(allowedRole))
					return true;
			}
		}
		throw new AuthorizationException("You don't have permissions.");
	}
}
