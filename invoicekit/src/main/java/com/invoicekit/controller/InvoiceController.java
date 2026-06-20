package com.invoicekit.controller;

import com.invoicekit.dto.InvoiceRequest;
import com.invoicekit.entity.Invoice;
import com.invoicekit.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public Invoice createInvoice(@RequestBody InvoiceRequest request) {
        return invoiceService.createInvoice(request);
    }
}