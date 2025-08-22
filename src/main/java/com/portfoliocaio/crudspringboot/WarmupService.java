package com.portfoliocaio.crudspringboot;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.portfoliocaio.crudspringboot.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarmupService {

    private final JdbcTemplate jdbcTemplate;
    private final JwtService jwtService;

    @Async("warmupExecutor")
    public CompletableFuture<Void> warmUpAsync() {
        long start = System.nanoTime();
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            String warmToken = jwtService.generateToken("warmup@local");
            jwtService.isValid(warmToken);
            jwtService.extractSubject(warmToken);

            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info("Warmup concluído em {} ms", ms);
        } catch (Exception e) {
            long ms = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.warn("Warmup parcial com erro após {} ms: {}", ms, e.getMessage());
        }
        return CompletableFuture.completedFuture(null);
    }
}
