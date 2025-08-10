package com.portfolio_caio.crud_springboot.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio_caio.crud_springboot.infrastructure.entitys.UserClient;

public interface UserClientRepository extends JpaRepository<UserClient, Integer>{
	Optional<UserClient> findByEmailIgnoreCase(String email);
	
	@Transactional
	void deleteByEmail(String email);
}
