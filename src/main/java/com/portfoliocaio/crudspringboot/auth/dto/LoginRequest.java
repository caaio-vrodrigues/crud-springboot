package com.portfoliocaio.crudspringboot.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank @Email 
    String email,
    @NotBlank @Size(min = 8, max = 72)
    String password
) {
    @Override
    public String toString() {
	    return "LoginRequest[email=%s, password=****]".formatted(email);
    }
}