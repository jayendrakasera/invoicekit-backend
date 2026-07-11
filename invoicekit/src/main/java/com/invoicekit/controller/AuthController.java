package com.invoicekit.controller;

import com.invoicekit.dto.*;
import com.invoicekit.service.AuthService;
import com.invoicekit.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return new AuthResponse(authService.register(request));
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return new AuthResponse(authService.login(request));
    }

    /*@GetMapping("/oauth-success")
    public String oauthSuccess(Authentication authentication) {
        return "OAuth login successful: " + authentication.getName();
    }*/
    @GetMapping("/oauth-success")
    public void oauthSuccess(
            Authentication authentication,
            HttpServletResponse response
    ) throws IOException {

        String email = authentication.getName();

        String token = jwtService.generateToken(email);

        response.sendRedirect(
                "https://invoicekit-frontend.vercel.app/dashboard.html?token=" + token
        );
    }
}