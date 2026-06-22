package com.invoicekit.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRequest {

    @NotBlank
    private Long clientId;

    @NotBlank
    private LocalDate issueDate;

    @NotBlank
    private LocalDate dueDate;

    private String notes;

    @NotBlank
    private Double gstPercentage;

    @NotBlank
    private List<InvoiceItemDto> items;
}