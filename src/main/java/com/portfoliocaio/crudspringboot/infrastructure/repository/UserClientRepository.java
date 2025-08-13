package com.portfoliocaio.crudspringboot.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.portfoliocaio.crudspringboot.infrastructure.entities.UserClient;

public interface UserClientRepository extends JpaRepository<UserClient, Integer>{
    long deleteByEmail(String email);
    Optional<UserClient> findByEmailIgnoreCase(String email);
}