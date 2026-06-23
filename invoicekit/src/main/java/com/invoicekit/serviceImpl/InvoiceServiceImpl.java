package com.invoicekit.serviceImpl;

import com.invoicekit.dto.*;
import com.invoicekit.entity.*;
import com.invoicekit.exception.ResourceNotFoundException;
import com.invoicekit.repository.*;
import com.invoicekit.service.InvoiceService;
import com.invoicekit.util.PdfGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

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

    @Override
    public DashboardResponse getDashboardStats(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        List<Client> clients = clientRepository.findByUserId(user.getId());

        List<Invoice> invoices = invoiceRepository.findByClientUserId(user.getId());

        long totalClients = clients.size();
        long totalInvoices = invoices.size();

        double totalRevenue = invoices.stream()
                .mapToDouble(Invoice::getTotalAmount)
                .sum();

        long paidInvoices = invoices.stream()
                .filter(invoice -> invoice.getStatus().equals("PAID"))
                .count();

        long pendingInvoices = invoices.stream()
                .filter(invoice -> !invoice.getStatus().equals("PAID"))
                .count();

        return new DashboardResponse(
                totalClients,
                totalInvoices,
                totalRevenue,
                paidInvoices,
                pendingInvoices
        );
    }

    @Override
    public List<Invoice> searchInvoices(String keyword, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return invoiceRepository
                .findByInvoiceNumberContainingIgnoreCaseAndClientUserId(
                        keyword,
                        user.getId()
                );
    }

    @Override
    public List<Invoice> filterInvoicesByStatus(
            String status,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return invoiceRepository.findByStatusAndClientUserId(
                status,
                user.getId()
        );
    }

    @Override
    public List<Invoice> sortInvoicesByAmount(
            String order,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (order.equalsIgnoreCase("asc")) {
            return invoiceRepository
                    .findByClientUserIdOrderByTotalAmountAsc(user.getId());
        }

        return invoiceRepository
                .findByClientUserIdOrderByTotalAmountDesc(user.getId());
    }

    @Override
    public List<Invoice> sortInvoicesByDate(
            String order,
            String email
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (order.equalsIgnoreCase("asc")) {
            return invoiceRepository
                    .findByClientUserIdOrderByIssueDateAsc(user.getId());
        }

        return invoiceRepository
                .findByClientUserIdOrderByIssueDateDesc(user.getId());
    }

    @Override
    public Page<Invoice> getInvoicesPaginated(
            String email,
            int page,
            int size
    ) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        Pageable pageable = PageRequest.of(page, size);

        return invoiceRepository.findByClientUserId(
                user.getId(),
                pageable
        );
    }
}