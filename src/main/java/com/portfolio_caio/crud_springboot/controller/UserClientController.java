package com.portfolio_caio.crud_springboot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio_caio.crud_springboot.business.UserClientService;
import com.portfolio_caio.crud_springboot.infrastructure.entitys.UserClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserClientController {
	private final UserClientService usuarioService;
	
	@PostMapping 
	public ResponseEntity<Void> createUser(@RequestBody UserClient userClient){
		usuarioService.saveUser(userClient);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping 
	public ResponseEntity<?> readUser(
			@RequestParam(required=true) String email){
		try {
			UserClient usuario = usuarioService.searchUser(email.trim());
			return ResponseEntity.ok(usuario);
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(404).body("Usuário não encontrado");
		}
	}
	
	@PutMapping
	public ResponseEntity<Void> updateUser(
			@RequestParam Integer id, 
			@RequestBody UserClient userClient){
		usuarioService.updateUserById(id, userClient);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteUser(@RequestParam String email){
		usuarioService.deleteUserByEmail(email);
		return ResponseEntity.ok().build();
	}
}
