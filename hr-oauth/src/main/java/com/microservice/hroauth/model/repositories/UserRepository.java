package com.microservice.hroauth.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservice.hroauth.model.entities.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
