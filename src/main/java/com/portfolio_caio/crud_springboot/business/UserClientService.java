package com.portfolio_caio.crud_springboot.business;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio_caio.crud_springboot.infrastructure.entitys.UserClient;
import com.portfolio_caio.crud_springboot.infrastructure.repository.UserClientRepository;

@Service
public class UserClientService {
	private final UserClientRepository repository;
	
	public UserClientService(UserClientRepository repository) {
		this.repository = repository;
	}
	
	public void salvarUsuario(UserClient userClient) {
		repository.saveAndFlush(userClient);
	}
	
	public UserClient buscarUsuarioPorEmail(String email) {
		return repository.findByEmailIgnoreCase(email.trim())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")
		);
	}
	
	public void deleteUsuarioPorEmail(String email) {
		repository.deleteByEmail(email);
	}
	
	public void atualizarUsuarioPorId(Integer id, UserClient client) {
		UserClient usuarioEntity = repository.findById(id).orElseThrow(
			() -> new RuntimeException("Usuário não encontrado")
		);
		UserClient usuarioAtualizado = UserClient.builder()
				.email(client.getEmail() != null ? client.getEmail() : usuarioEntity.getEmail())
				.password(client.getPassword() != null ? client.getPassword() : usuarioEntity.getPassword())
				.id(usuarioEntity.getId())
				.build();
		
		repository.saveAndFlush(usuarioAtualizado);
	}
}
