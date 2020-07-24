package com.sample;

import com.sample.component.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JWTApplication {
    public static void main(String[] args) {
        SpringApplication.run(JWTApplication.class, args);
    }

    /* jwt 관련 Bean, key 등록 */
    @Value("${jwt.secret}")
    private String secret;

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils(secret);
    }
}