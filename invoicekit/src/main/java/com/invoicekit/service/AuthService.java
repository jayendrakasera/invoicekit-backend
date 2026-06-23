package com.invoicekit.service;

import com.invoicekit.dto.*;

public interface AuthService {

    String register(RegisterRequest request);

    String login(LoginRequest request);

    void updateBankDetails(
            String email,
            UpdateProfileRequest request
    );
}