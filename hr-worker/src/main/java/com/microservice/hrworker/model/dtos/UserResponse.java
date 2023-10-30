package com.microservice.hrworker.model.dtos;

import java.util.Set;

import com.microservice.hrworker.model.entities.Role;

public record UserResponse(String username, Set<Role> roles) {

}
