package com.sample.controller;

import com.sample.component.JwtUtils;
import com.sample.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public void login() {
    }

    @GetMapping("/user")
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok().body("{\"role\":\"user\", \"data\": [{\"id\": 1, \"name\": \"item1\"}, {\"id\": 2, \"name\": \"item2\"}]}");
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdmin() {
        return ResponseEntity.ok().body("{\"role\": \"admin\", \"data\": [{\"id\": 1, \"name\": \"item1\", \"author\": \"user1\"}, {\"id\": 2, \"name\": \"item2\", \"author\": \"user2\"}]}");
    }
}
