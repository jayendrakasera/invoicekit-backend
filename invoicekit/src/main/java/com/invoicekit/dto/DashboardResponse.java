package com.invoicekit.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private Long totalClients;
    private Long totalInvoices;
    private Double totalRevenue;
}