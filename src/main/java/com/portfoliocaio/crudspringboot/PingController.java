package com.portfoliocaio.crudspringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate; // injeção opcional para acessar o DB

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        if (jdbcTemplate != null) {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class); // mantém o banco “acordado”
        }
        return ResponseEntity.ok("ok"); // novo endpoint leve para keep-alive
    }
}
