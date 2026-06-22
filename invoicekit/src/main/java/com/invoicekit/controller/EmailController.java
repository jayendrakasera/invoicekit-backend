package com.invoicekit.controller;

import com.invoicekit.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send/{invoiceId}")
    public String sendInvoice(@PathVariable Long invoiceId) {
        emailService.sendInvoiceEmail(invoiceId);
        return "Invoice email sent successfully";
    }
}