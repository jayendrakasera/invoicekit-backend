package com.invoicekit.serviceImpl;

import com.invoicekit.dto.*;
import com.invoicekit.entity.*;
import com.invoicekit.repository.*;
import com.invoicekit.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;

    @Override
    public Invoice createInvoice(InvoiceRequest request) {

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + System.currentTimeMillis());
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        invoice.setNotes(request.getNotes());
        invoice.setStatus(InvoiceStatus.DRAFT);

        List<InvoiceItem> invoiceItems = new ArrayList<>();
        double subtotal = 0;

        for (InvoiceItemDto itemDto : request.getItems()) {

            InvoiceItem item = new InvoiceItem();
            item.setItemName(itemDto.getItemName());
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(itemDto.getPrice());

            double amount = itemDto.getQuantity() * itemDto.getPrice();
            item.setAmount(amount);

            subtotal += amount;

            item.setInvoice(invoice);
            invoiceItems.add(item);
        }

        double gstAmount = subtotal * (request.getGstPercentage() / 100);
        double totalAmount = subtotal + gstAmount;

        invoice.setSubtotal(subtotal);
        invoice.setGstAmount(gstAmount);
        invoice.setTotalAmount(totalAmount);

        invoice.setClient(client);
        invoice.setItems(invoiceItems);

        return invoiceRepository.save(invoice);
    }
}