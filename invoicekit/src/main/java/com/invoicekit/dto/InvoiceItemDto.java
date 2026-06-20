package com.invoicekit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDto {

    private String itemName;
    private Integer quantity;
    private Double price;
}