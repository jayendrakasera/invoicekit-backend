package com.invoicekit.controller;

import com.invoicekit.dto.UpdateProfileRequest;
import com.invoicekit.security.JwtService;
import com.invoicekit.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final AuthService authService;

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateProfileRequest request
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        authService.updateBankDetails(email, request);

        return ResponseEntity.ok("Profile updated");
    }
}