package com.portfolio_caio.crud_springboot.business;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio_caio.crud_springboot.infrastructure.entitys.Usuario;
import com.portfolio_caio.crud_springboot.infrastructure.repository.UsuarioRepository;

@Service
public class UsuarioService {
	private final UsuarioRepository repository;
	
	public UsuarioService(UsuarioRepository repository) {
		this.repository = repository;
	}
	
	public void salvarUsuario(Usuario usuario) {
		repository.saveAndFlush(usuario);
	}
	
	public Usuario searchUser(String email) {
		return repository.findAccount(email.trim())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
		);
	}
	
	public void deleteUsuarioPorEmail(String email) {
		repository.deleteByEmail(email);
	}
	
	public void atualizarUsuarioPorId(Integer id, Usuario usuario) {
		Usuario usuarioEntity = repository.findById(id).orElseThrow(
			() -> new RuntimeException("Usuário não encontrado")
		);
		Usuario usuarioAtualizado = Usuario.builder()
				.email(usuario.getEmail() != null ? usuario.getEmail() : usuarioEntity.getEmail())
				.password(usuario.getPassword() != null ? usuario.getPassword() : usuarioEntity.getPassword())
				.id(usuarioEntity.getId())
				.build();
		
		repository.saveAndFlush(usuarioAtualizado);
	}
}
