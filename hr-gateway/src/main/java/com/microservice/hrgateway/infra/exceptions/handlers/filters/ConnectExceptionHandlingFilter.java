package com.microservice.hrgateway.infra.exceptions.handlers.filters;
import java.net.ConnectException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.hrgateway.infra.exceptions.ExceptionFormatter;

import reactor.core.publisher.Mono;

@Component
public class ConnectExceptionHandlingFilter implements GlobalFilter, Ordered {
	
	@Autowired
	private ObjectMapper objectMapper;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
            .onErrorResume(ConnectException.class, e -> handleConnectException(exchange));
    }

    private Mono<Void> handleConnectException(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ExceptionFormatter exceptionFormatter = new ExceptionFormatter(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Service not found",
                Instant.now()
        );
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(exceptionFormatter);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return exchange.getResponse().writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            return Mono.error(e);
        }
    }
    
    @Override
    public int getOrder() {
        return -1;
    }
}