package com.invoicekit.controller;

import com.invoicekit.dto.DashboardResponse;
import com.invoicekit.dto.InvoiceRequest;
import com.invoicekit.entity.Invoice;
import com.invoicekit.security.JwtService;
import java.util.List;
import com.invoicekit.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping("/dashboard")
    public DashboardResponse getDashboardStats(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        return invoiceService.getDashboardStats(email);
    }

    @GetMapping("/search")
    public List<Invoice> searchInvoices(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        return invoiceService.searchInvoices(keyword, email);
    }

    @GetMapping("/sort/amount")
    public List<Invoice> sortByAmount(
            @RequestParam String order,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        return invoiceService.sortInvoicesByAmount(order, email);
    }

    @GetMapping("/sort/date")
    public List<Invoice> sortByDate(
            @RequestParam String order,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        return invoiceService.sortInvoicesByDate(order, email);
    }

    @GetMapping("/filter/status")
    public List<Invoice> filterByStatus(
            @RequestParam String status,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        return invoiceService.filterInvoicesByStatus(status, email);
    }

    @GetMapping("/paginated")
    public Page<Invoice> getInvoicesPaginated(
            @RequestParam int page,
            @RequestParam int size,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtService.extractTokenFromHeader(authHeader);
        String email = jwtService.extractEmail(token);

        return invoiceService.getInvoicesPaginated(
                email,
                page,
                size
        );
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteInvoice(
            @PathVariable Long id
    ) {
        invoiceService.deleteInvoice(id);

        return ResponseEntity.ok("Invoice deleted successfully");
    }
}