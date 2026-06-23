package com.invoicekit.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String accountHolderName;
    private String upiId;
}