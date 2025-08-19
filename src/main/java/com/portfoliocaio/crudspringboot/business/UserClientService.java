package com.portfoliocaio.crudspringboot.business;

import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.portfoliocaio.crudspringboot.auth.dto.UserDto;
import com.portfoliocaio.crudspringboot.infrastructure.entities.UserClient;
import com.portfoliocaio.crudspringboot.infrastructure
											.repository.UserClientRepository;

@Service
public class UserClientService {
    private final UserClientRepository repository;
    private static final int MAX_ATTEMPTS = 10;
    private static final Duration WINDOW = Duration.ofSeconds(20);
    private static final Duration LOCKOUT_DURATION = Duration.ofSeconds(20);
    private final Map<String, AttemptInfo> attempts = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory
                                        .getLogger(UserClientService.class);
    
    public UserClientService(UserClientRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Long saveUser(UserDto userDto) { 
        final UserClient entity = toEntity(userDto);
        repository.save(entity); 
        log.info("user-created email={} actor={}",
                maskEmail(entity.getEmail()), 
                "anonymous"
            );
        return entity.getId();
    }

    @Transactional
    public UserDto searchUser(String email, String password) {
        final String normalizedEmail = normalizeEmail(email);

        if (isLocked(normalizedEmail)) {
            log.warn("login-locked email={} actor={}", 
                    maskEmail(normalizedEmail), 
                    "anonymous"
                );
            throw new ResponseStatusException(
                HttpStatus.TOO_MANY_REQUESTS, 
                "Muitas tentativas. Tente novamente dentro de 20 segundos."
            );
        }

        return repository.findByEmailIgnoreCase(normalizedEmail)
            .filter(entity -> entity.getPassword().equals(password))
            .map(entity -> {
                resetAttempts(normalizedEmail);
                log.info("login-success email={} actor={}", 
                        maskEmail(normalizedEmail), 
                        "anonymous"
                    );
                return toSafeDto(entity);
            })
            .orElseThrow(() -> {
                registerFailure(normalizedEmail);
                log.warn("login-failed email={} actor={}", 
                        maskEmail(normalizedEmail), 
                        "anonymous"
                    );
                return new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, 
                    "Credenciais inválidas"
                );
            });
    }

    @Transactional(readOnly=true)
    public UserDto searchUser(UserDto credentials) {
        return searchUser(credentials.email(), credentials.password());
    }

    @Transactional 
    public boolean deleteUserByEmail(String email) {
        final String normalizedEmail = normalizeEmail(email);

        long deleted = repository.deleteByEmail(normalizedEmail);
        if (deleted == 0) {
            log.info("user-delete-none email={} actor={}", 
                    maskEmail(normalizedEmail), 
                    "anonymous"
                );
            return false;
        }
        if (deleted > 1) {
            log.warn("user-delete-multiple email={} count={} actor={}", 
            		maskEmail(normalizedEmail), 
            		deleted, 
            		"anonymous"
            	);
        } else {
            log.info("user-deleted email={} actor={}", 
                    maskEmail(normalizedEmail), 
                    "anonymous"
                );
        }
        return true;
    }

    @Transactional
    public boolean updateUserById(Integer id, UserDto dto) {
        try {
            final UserClient userEntity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                                            HttpStatus.NOT_FOUND,
                            				"Recurso não encontrado")
                            			); 
            if (dto.email() != null) {
                userEntity.setEmail(normalizeEmail(dto.email()));
            }
            if (dto.password() != null) userEntity.setPassword(dto.password());
            repository.save(userEntity);
            log.info("user-updated id={} email={} actor={}", 
                    userEntity.getId(), 
                    maskEmail(userEntity.getEmail()), 
                    "anonymous"
                );
            return true;
        } 
        catch (OptimisticLockingFailureException e) {
            log.warn("user-update-conflict id={} actor={}", id, "anonymous");
            throw new ResponseStatusException(
                        HttpStatus.CONFLICT, 
                        "Conflito de atualização. Recarregue e tente novamente."
                    );
        }
    }

    private UserClient toEntity(UserDto dto) {
        return UserClient.builder()
            .id(dto.id())
            .email(normalizeEmail(dto.email()))
            .password(dto.password())
            .build();
    }

    private UserDto toDto(UserClient entity) {
        return new UserDto(entity.getId(), 
                		normalizeEmail(entity.getEmail()),
                		entity.getPassword()
                	);
    }

    private UserDto toSafeDto(UserClient entity) {
        return new UserDto(
        		entity.getId(), normalizeEmail(entity.getEmail()), null);
    }

    private boolean isLocked(String key) { 
        if (key == null) return false;
        AttemptInfo info = attempts.get(key);
        if (info == null) return false;
        if (info.lockUntil != null && 
                Instant.now().isBefore(info.lockUntil)) return true;
        if (info.firstAttemptAt != null && 
                Instant.now().isAfter(info.firstAttemptAt.plus(WINDOW))) 
        {
            attempts.remove(key);
            return false;
        }
        return false;
    }

    private void registerFailure(String key) {
        if (key == null) return;
        AttemptInfo info = attempts
                .computeIfAbsent(key, k -> new AttemptInfo());
        if (info.firstAttemptAt == null || 
                Instant.now().isAfter(info.firstAttemptAt.plus(WINDOW))) 
        {
            info.firstAttemptAt = Instant.now();
            info.count = 0;
            info.lockUntil = null;
        }
        info.count++;
        if (info.count >= MAX_ATTEMPTS) {
            info.lockUntil = Instant.now().plus(LOCKOUT_DURATION);
        }
    }

    private void resetAttempts(String key) { 
        if (key == null) return;
        attempts.remove(key); 
    }

    private String maskEmail(String email) { 
        if (email == null || !email.contains("@")) return "***";
        String[] parts = email.split("@", 2);
        String local = parts[0];
        String domain = parts[1];
        String maskedLocal = local.length() <= 2 ? 
                "**" : local.charAt(0) + "***" + local.charAt(local.length() - 1);
        return (maskedLocal + "@" + domain).toLowerCase(Locale.ROOT);
    }

    private String normalizeEmail(String email) {
        return (email == null) ? null : email.toLowerCase(Locale.ROOT);
    }

    private static class AttemptInfo {
        int count;
        Instant firstAttemptAt;
        Instant lockUntil;
    }
}
