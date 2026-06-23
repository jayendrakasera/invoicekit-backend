package com.invoicekit.controller;

import com.invoicekit.dto.DashboardResponse;
import com.invoicekit.repository.ClientRepository;
import com.invoicekit.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;

    @GetMapping("/stats")
    public DashboardResponse getStats() {

        long totalClients = clientRepository.count();
        long totalInvoices = invoiceRepository.count();

        double totalRevenue = invoiceRepository.findAll()
                .stream()
                .mapToDouble(invoice -> invoice.getTotalAmount())
                .sum();

        long paidInvoices = invoiceRepository
                .findAll()
                .stream()
                .filter(i -> i.getStatus().name().equals("PAID"))
                .count();

        long pendingInvoices = invoiceRepository
                .findAll()
                .stream()
                .filter(i ->
                        i.getStatus().name().equals("DRAFT")
                                || i.getStatus().name().equals("SENT")
                )
                .count();

        return new DashboardResponse(
                totalClients,
                totalInvoices,
                totalRevenue,
                paidInvoices,
                pendingInvoices
        );
    }
}