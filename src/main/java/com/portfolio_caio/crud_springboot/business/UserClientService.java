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
	
	public void saveUser(UserClient userClient) {
		repository.saveAndFlush(userClient);
	}
	
	public UserClient searchUser(String email, String password) {
		return repository.findUser(email, password)
			.orElseThrow(() -> 
				new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Usuário não encontrado")
		);
	}
	
	public void deleteUserByEmail(String email) {
		repository.deleteByEmail(email);
	}
	
	public void updateUserById(Integer id, UserClient client) {
		UserClient usuarioEntity = repository.findById(id).orElseThrow(
			() -> new RuntimeException("Usuário não encontrado")
		);
		final String email = client.getEmail() != null ? 
			client.getEmail() : usuarioEntity.getEmail();
		final String password = client.getPassword() != null ? 
			client.getPassword() : usuarioEntity.getPassword();
		final UserClient usuarioAtualizado = UserClient.builder()
			.email(email)
			.password(password)
			.id(usuarioEntity.getId())
			.build();
		
		repository.saveAndFlush(usuarioAtualizado);
	}
}
