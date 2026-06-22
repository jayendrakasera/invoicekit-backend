package com.invoicekit.serviceImpl;

import com.invoicekit.dto.*;
import com.invoicekit.entity.*;
import com.invoicekit.exception.ResourceNotFoundException;
import com.invoicekit.repository.*;
import com.invoicekit.service.InvoiceService;
import com.invoicekit.util.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final UserRepository userRepository;
    private final InvoiceRepository invoiceRepository;
    private final ClientRepository clientRepository;

    @Override
    public Invoice createInvoice(InvoiceRequest request) {

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));

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

    @Override
    public List<Invoice> getAllInvoices(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return invoiceRepository.findByClientUserId(user.getId());
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
    }

    @Override
    public Invoice updateInvoiceStatus(Long id, String status) {

        Invoice invoice = getInvoiceById(id);

        invoice.setStatus(InvoiceStatus.valueOf(status.toUpperCase()));

        return invoiceRepository.save(invoice);
    }

    @Override
    public byte[] generateInvoicePdf(Long invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);
        return PdfGenerator.generateInvoicePdf(invoice);
    }
}