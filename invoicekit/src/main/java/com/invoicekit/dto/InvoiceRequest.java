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

    @NotNull
    private Long clientId;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate dueDate;

    private String notes;

    @NotNull
    private Double gstPercentage;

    @NotEmpty
    private List<InvoiceItemDto> items;
}