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

import com.portfolio_caio.crud_springboot.business.UsuarioService;
import com.portfolio_caio.crud_springboot.infrastructure.entitys.Usuario;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioController {
	private final UsuarioService usuarioService;
	
	@PostMapping("/login") //create
	public ResponseEntity<Void> salvarUsuario(@RequestBody Usuario usuario){
		usuarioService.searchUser(usuario.getEmail());
		return ResponseEntity.ok().build();
	}
	
	@GetMapping //read
	public ResponseEntity<?> buscarUsuarioPorEmail(
			@RequestParam(required=true) String email){
		try {
			Usuario usuario = usuarioService.searchUser(email.trim());
			return ResponseEntity.ok(usuario);
		}
		catch(RuntimeException e) {
			return ResponseEntity.status(404).body("Usuário não encontrado");
		}
	}
	
	@PutMapping //update
	public ResponseEntity<Void> atualizarUsuarioPorId(
			@RequestParam Integer id, 
			@RequestBody Usuario usuario){
		usuarioService.atualizarUsuarioPorId(id, usuario);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping //delete
	public ResponseEntity<Void> deletarUsuarioPorEmail(@RequestParam String email){
		usuarioService.deleteUsuarioPorEmail(email);
		return ResponseEntity.ok().build();
	}
}
