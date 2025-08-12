package com.portfoliocaio.crudspringboot.controller;

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

import com.portfoliocaio.crudspringboot.business.UserClientService;
// [REMOVIDO] import da entidade UserClient
import com.portfoliocaio.crudspringboot.security.JwtService;

import jakarta.validation.Valid;

import com.portfoliocaio.crudspringboot.auth.dto.LoginRequest;
import com.portfoliocaio.crudspringboot.auth.dto.TokenResponse;
import com.portfoliocaio.crudspringboot.auth.dto.UserDto; // [ADICIONADO] uso do UserDto

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserClientController {
    private final UserClientService usuarioService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDto) { 
        usuarioService.saveUser(userDto); 
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest body) {
        try {
            UserDto usuario = usuarioService.searchUser(body.email(), body.password());
            String token = UUID.randomUUID().toString();
            return ResponseEntity.ok(new TokenResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
    
    @GetMapping("/validate")
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
            UserDto usuario = usuarioService.searchUser(email, password);
            return ResponseEntity.ok(usuario); 
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body("Usuário não encontrado");
        }
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(
            @RequestParam Integer id,
            @RequestBody UserDto userDto) { 
        usuarioService.updateUserById(id, userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String email) {
        usuarioService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }
}