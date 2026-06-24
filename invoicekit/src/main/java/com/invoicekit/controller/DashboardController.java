/*
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
}*/
// changed by query solver
package com.invoicekit.controller;

import com.invoicekit.dto.DashboardResponse;
import com.invoicekit.entity.Invoice;
import com.invoicekit.entity.User;
import com.invoicekit.exception.ResourceNotFoundException;
import com.invoicekit.repository.ClientRepository;
import com.invoicekit.repository.InvoiceRepository;
import com.invoicekit.repository.UserRepository;
import com.invoicekit.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final ClientRepository clientRepository;
    private final InvoiceRepository invoiceRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @GetMapping("/stats")
    public DashboardResponse getStats(
            @RequestHeader("Authorization") String authHeader
    ) {

        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        long totalClients =
                clientRepository.findByUserId(user.getId()).size();

        List<Invoice> invoices =
                invoiceRepository.findByClientUserId(user.getId());

        long totalInvoices = invoices.size();

        double totalRevenue = invoices.stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();

        long paidInvoices = invoices.stream()
                .filter(i -> i.getStatus().name().equals("PAID"))
                .count();

        long pendingInvoices = invoices.stream()
                .filter(i ->
                        i.getStatus().name().equals("DRAFT")
                                || i.getStatus().name().equals("SENT"))
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
