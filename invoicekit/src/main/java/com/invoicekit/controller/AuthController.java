package com.invoicekit.controller;

import com.invoicekit.dto.*;
import com.invoicekit.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return new AuthResponse(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return new AuthResponse(authService.login(request));
    }

    @GetMapping("/oauth-success")
    public String oauthSuccess(Authentication authentication) {
        return "OAuth login successful: " + authentication.getName();
    }
}