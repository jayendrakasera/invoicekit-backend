package com.invoicekit.service;

import com.invoicekit.dto.InvoiceRequest;
import com.invoicekit.entity.Invoice;
import com.invoicekit.dto.DashboardResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface InvoiceService {

    Invoice createInvoice(InvoiceRequest request);

    List<Invoice> getAllInvoices(String email);

    List<Invoice> searchInvoices(String keyword, String email);

    List<Invoice> filterInvoicesByStatus(String status, String email);

    List<Invoice> sortInvoicesByAmount(String order, String email);

    List<Invoice> sortInvoicesByDate(String order, String email);

    DashboardResponse getDashboardStats(String email);

    Invoice getInvoiceById(Long id);

    Invoice updateInvoiceStatus(Long id, String status);

    byte[] generateInvoicePdf(Long invoiceId);

    Page<Invoice> getInvoicesPaginated(
            String email,
            int page,
            int size
    );
}