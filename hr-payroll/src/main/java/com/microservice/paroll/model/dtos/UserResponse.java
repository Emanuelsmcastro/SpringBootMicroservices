package com.microservice.paroll.model.dtos;

import java.util.Set;

import com.microservice.paroll.model.entities.Role;

public record UserResponse(String username, Set<Role> roles) {

}
