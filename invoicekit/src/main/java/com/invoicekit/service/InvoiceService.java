package com.invoicekit.service;

import com.invoicekit.dto.InvoiceRequest;
import com.invoicekit.entity.Invoice;

public interface InvoiceService {
    Invoice createInvoice(InvoiceRequest request);
}