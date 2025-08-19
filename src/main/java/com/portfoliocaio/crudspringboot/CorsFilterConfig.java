package com.portfoliocaio.crudspringboot;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsFilterConfig {
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = 
        								new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
       
        config.setAllowedOrigins(List.of(
            "https://projeto-login-client-side.vercel.app",
            "http://localhost:3000"
        ));
        
        config.setAllowedMethods(List.of(
        		"GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));       
        config.setAllowedHeaders(List.of(
        		"Authorization", "Content-Type", "Origin", "Accept"));      
        config.setExposedHeaders(List.of("Authorization"));       
        config.setAllowCredentials(false);        
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean<CorsFilter> bean = 
        				new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
