package com.portfoliocaio.crudspringboot.business;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.portfoliocaio.crudspringboot.auth.dto.UserDto;
import com.portfoliocaio.crudspringboot.infrastructure.entities.UserClient;
import com.portfoliocaio.crudspringboot.infrastructure.repository.UserClientRepository;

@Service
public class UserClientService {
    private final UserClientRepository repository;

    public UserClientService(UserClientRepository repository) {
        this.repository = repository;
    }

    public void saveUser(UserDto userDto) {
        UserClient entity = toEntity(userDto);
        repository.saveAndFlush(entity);
    }

    public UserDto searchUser(String email, String password) {
        return repository.findUser(email, password)
            .map(this::toDto)
            .orElseThrow(() ->
            	new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    public UserDto searchUser(UserDto credentials) {
        return searchUser(credentials.email(), credentials.password());
    }

    public void deleteUserByEmail(String email) {
        repository.deleteByEmail(email);
    }

    public void updateUserById(Integer id, UserDto dto) {
        UserClient usuarioEntity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));

        final String email = dto.email() != null ? dto.email() : usuarioEntity.getEmail();
        final String password = dto.password() != null ? dto.password() : usuarioEntity.getPassword();

        final UserClient usuarioAtualizado = UserClient.builder()
            .id(usuarioEntity.getId())
            .email(email)
            .password(password)
            .build();

        repository.saveAndFlush(usuarioAtualizado);
    }

    private UserClient toEntity(UserDto dto) {
        return UserClient.builder()
            .id(dto.id())
            .email(dto.email())
            .password(dto.password())
            .build();
    }

    private UserDto toDto(UserClient entity) {
        return new UserDto(entity.getId(), entity.getEmail(), entity.getPassword());
    }
}