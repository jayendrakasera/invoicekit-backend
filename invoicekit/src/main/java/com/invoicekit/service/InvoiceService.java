package com.invoicekit.service;

import com.invoicekit.dto.InvoiceRequest;
import com.invoicekit.entity.Invoice;

import java.util.List;

public interface InvoiceService {

    Invoice createInvoice(InvoiceRequest request);

    List<Invoice> getAllInvoices(String email);

    Invoice getInvoiceById(Long id);

    Invoice updateInvoiceStatus(Long id, String status);

    byte[] generateInvoicePdf(Long invoiceId);
}