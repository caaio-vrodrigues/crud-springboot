package com.portfoliocaio.crudspringboot.security;

import java.util.List;

import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; 
import org.springframework.security.config.Customizer; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration
															.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration 
@EnableWebSecurity
public class SecurityConfig {
    @Bean 
    public SecurityFilterChain securityFilterChain(
    										HttpSecurity http) throws Exception 
    {
        http.csrf(csrf -> csrf.disable()) 
            .cors(Customizer.withDefaults()) 
            .sessionManagement(sm -> 
            		sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) 
            		  .authorizeHttpRequests(auth -> auth
            			.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() 
                        .requestMatchers("/auth/login").permitAll() 
                        .requestMatchers("/ping").permitAll()
                        .anyRequest().authenticated()
            		);
        return http.build();
    }

    @Bean 
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of(
            "https://projeto-login-client-side.vercel.app",
            "http://localhost:3000"
        ));

        config.setAllowedMethods(List
        						  .of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = 
        									new UrlBasedCorsConfigurationSource();
        
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
