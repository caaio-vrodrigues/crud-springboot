package com.portfolio_caio.crud_springboot.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.portfolio_caio.crud_springboot.infrastructure.entitys.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
	Optional<Usuario> findAccount(String email);
	
	@Transactional
	void deleteByEmail(String email);
}
