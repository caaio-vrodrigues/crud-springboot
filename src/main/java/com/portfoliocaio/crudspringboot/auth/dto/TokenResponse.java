package com.portfoliocaio.crudspringboot.auth.dto;

public record TokenResponse(String token) {
    public TokenResponse {
        if (token == null) {
            throw new IllegalArgumentException("Token não pode ser nulo");
        }
        token = token.trim();
        if (token.isEmpty()) {
            throw new IllegalArgumentException("Token não pode ser vazio");
        }
    }

    @Override
    public String toString() {
        int keep = Math.min(8, token.length());
        String masked = token.substring(0, keep) + "***";
        return "TokenResponse[token=" + masked + "]";
    }
}