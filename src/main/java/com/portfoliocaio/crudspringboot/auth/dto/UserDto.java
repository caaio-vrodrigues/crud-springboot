package com.portfoliocaio.crudspringboot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Locale;

public record UserDto(
        Long id,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8) String password
) {
    public UserDto {
        if (password != null) {
        	password = password.trim();
        }
        if (email != null) {
            email = email.trim().toLowerCase(Locale.ROOT);
        }
    }

    @Override
    public String toString() {
        return "UserDto[id=%s, email=***]".formatted(id, email);
    }
}