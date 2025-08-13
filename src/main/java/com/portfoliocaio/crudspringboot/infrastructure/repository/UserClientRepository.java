package com.portfoliocaio.crudspringboot.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.portfoliocaio.crudspringboot.infrastructure.entities.UserClient;

public interface UserClientRepository extends JpaRepository<UserClient, Integer>{
	Optional<UserClient> findByEmailIgnoreCase(String email);
	String queryUserByEmailAndPassword = "SELECT u FROM UserClient u WHERE u.email = :email AND u.password = :password";
	
	@Transactional
	long deleteByEmail(String email);
	
	@Query(queryUserByEmailAndPassword)
	Optional<UserClient> findUser(
		@Param("email") String email,
		@Param("password") String password);
}
