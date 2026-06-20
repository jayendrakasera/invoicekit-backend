package com.invoicekit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String gstNumber;
}