package com.portfolio_caio.crud_springboot.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.portfolio_caio.crud_springboot.business.UserClientService;
import com.portfolio_caio.crud_springboot.infrastructure.entitys.UserClient;
import com.portfolio_caio.crud_springboot.security.JwtService;
// Ajuste estes imports conforme o pacote real dos seus DTOs (ex.: auth.dto)
import com.portfolio_caio.crud_springboot.auth.dto.LoginRequest;
import com.portfolio_caio.crud_springboot.auth.dto.TokenResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserClientController {

    private final UserClientService usuarioService;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<Void> createUser(@RequestBody UserClient userClient) {
        usuarioService.saveUser(userClient);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        try {
            UserClient usuario = usuarioService.searchUser(body.email(), body.password());
            // Token simples para portfólio (não é JWT real)
            String token = UUID.randomUUID().toString();
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
    
    @GetMapping("/auth/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authorization) {
        try {
            String token = authorization.replace("Bearer ", "").trim();
            if (!jwtService.isValid(token)) {
                return ResponseEntity.status(401).body("Token inválido");
            }
            String email = jwtService.extractSubject(token);
            return ResponseEntity.ok("Token válido para: " + email);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token inválido");
        }
    }
    
    @GetMapping
    public ResponseEntity<?> readUser(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) String password) {
        try {
            UserClient usuario = usuarioService.searchUser(email, password);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(
            @RequestParam Integer id,
            @RequestBody UserClient userClient) {
        usuarioService.updateUserById(id, userClient);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String email) {
        usuarioService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }
}