package com.microservice.paroll.infra.exceptions;

import java.time.Instant;

public record ExceptionFormatter(int status, String msg, Instant instant) {
}
