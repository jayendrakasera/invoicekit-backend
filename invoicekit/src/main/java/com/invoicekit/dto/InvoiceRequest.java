package com.invoicekit.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    private Long clientId;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private String notes;
    private Double gstPercentage;
    private List<InvoiceItemDto> items;
}