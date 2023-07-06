package com.example.chi_academy_test.controller;

import com.example.chi_academy_test.dto.AuthRequest;
import com.example.chi_academy_test.dto.AuthResponse;
import com.example.chi_academy_test.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AuthController {
    private final AuthService service;

    @Autowired
    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> register(@Validated @RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(path = "/auth")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
