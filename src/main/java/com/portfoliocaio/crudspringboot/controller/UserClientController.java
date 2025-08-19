package com.portfoliocaio.crudspringboot.controller;

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

import com.portfoliocaio.crudspringboot.security.JwtService;

import jakarta.validation.Valid;

import com.portfoliocaio.crudspringboot.auth.dto.ErrorPayload;
import com.portfoliocaio.crudspringboot.auth.dto.LoginRequest;
import com.portfoliocaio.crudspringboot.auth.dto.TokenResponse;
import com.portfoliocaio.crudspringboot.auth.dto.UserDto;
import com.portfoliocaio.crudspringboot.auth.dto.ValidateTokenResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserClientController {
    private final UserClientService userService;
    private final JwtService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto body) { 
        userService.saveUser(body); 
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest body) {
        try {
            userService.searchUser(body.email(), body.password());
            String token = jwtService.generateToken(body.email());
            return ResponseEntity.ok(new TokenResponse(token));
        } 
        catch (RuntimeException e) {
            return ResponseEntity
                .status(401)
                .body(ErrorPayload
                		.of(401, "Credenciais inválidas", "/auth/login"));
        }
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
        @RequestHeader("Authorization") String authorization) 
    {
        try {
            String token = authorization.replace("Bearer ", "").trim();
            if (!jwtService.isValid(token)) {
                return ResponseEntity
                    .status(401)
                    .body(ErrorPayload
                    		.of(401, "Token inválido", "/auth/validate"));
            }
            String email = jwtService.extractSubject(token);
            Long exp = null;
            return ResponseEntity.ok(new ValidateTokenResponse(true, email, exp));
        } 
        catch (Exception e) {
            return ResponseEntity
	            .status(401)
	            .body(ErrorPayload.of(401, "Token inválido", "/auth/validate"));
        }
    }
    
    @GetMapping
    public ResponseEntity<?> readUser(
        @RequestParam(required = true) String email,
        @RequestParam(required = true) String password) 
    {
        try {
            UserDto usuario = userService.searchUser(email, password);
            return ResponseEntity.ok(usuario); 
        } 
        catch (RuntimeException e) {
            return ResponseEntity
                .status(404)
                .body(ErrorPayload.of(404, "Usuário não encontrado", "/auth"));
        }
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(
            @RequestParam Integer id,
            @RequestBody UserDto userDto) 
    { 
        userService.updateUserById(id, userDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String email) {
        userService.deleteUserByEmail(email);
        return ResponseEntity.ok().build();
    }
}