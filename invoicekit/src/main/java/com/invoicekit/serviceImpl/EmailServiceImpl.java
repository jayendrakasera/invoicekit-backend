package com.invoicekit.serviceImpl;

import com.invoicekit.entity.Invoice;
import com.invoicekit.exception.ResourceNotFoundException;
import com.invoicekit.repository.InvoiceRepository;
import com.invoicekit.service.EmailService;
import com.invoicekit.util.PdfGenerator;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final InvoiceRepository invoiceRepository;

    @Override
    public void sendInvoiceEmail(Long invoiceId) {

        try {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

            byte[] pdf = PdfGenerator.generateInvoicePdf(invoice);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(invoice.getClient().getEmail());
            helper.setSubject("Invoice from InvoiceKit - " + invoice.getInvoiceNumber());

            helper.setText(
                    "Hello " + invoice.getClient().getName() + ",\n\n" +
                            "Please find your invoice attached.\n\n" +
                            "Total Amount: ₹" + invoice.getTotalAmount() + "\n\n" +
                            "Thank you."
            );

            helper.addAttachment(
                    "invoice.pdf",
                    new ByteArrayResource(pdf)
            );

            mailSender.send(message);

        } catch (Exception e) {
            throw new ResourceNotFoundException("Error sending email");
        }
    }
}