package com.invoicekit.controller;

import com.invoicekit.dto.InvoiceRequest;
import com.invoicekit.entity.Invoice;
import com.invoicekit.security.JwtService;
import java.util.List;
import com.invoicekit.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final JwtService jwtService;
    private final InvoiceService invoiceService;

    @PostMapping
    public Invoice createInvoice(@Valid @RequestBody InvoiceRequest request) {
        return invoiceService.createInvoice(request);
    }

    @GetMapping
    public List<Invoice> getAllInvoices(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        return invoiceService.getAllInvoices(email);
    }

    @GetMapping("/{id}")
    public Invoice getInvoiceById(@PathVariable Long id) {
        return invoiceService.getInvoiceById(id);
    }

    @PutMapping("/{id}/status")
    public Invoice updateInvoiceStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return invoiceService.updateInvoiceStatus(id, status);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoicePdf(@PathVariable Long id) {

        byte[] pdf = invoiceService.generateInvoicePdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}