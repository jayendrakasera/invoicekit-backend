package com.invoicekit.serviceImpl;

import com.invoicekit.entity.Invoice;
import com.invoicekit.entity.User;
import com.invoicekit.exception.ResourceNotFoundException;
import com.invoicekit.repository.InvoiceRepository;
import com.invoicekit.service.EmailService;
import com.invoicekit.util.PdfGenerator;
import com.invoicekit.util.QrCodeUtil;
import com.resend.Resend;
import com.resend.services.emails.model.*;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
/*import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.*;*/
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

//    private final JavaMailSender mailSender;
    private final InvoiceRepository invoiceRepository;

    @Async
    @Override
    public void sendInvoiceEmail(Long invoiceId) {

        try {
            Invoice invoice = invoiceRepository.findById(invoiceId)
                    .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

//            byte[] pdf = PdfGenerator.generateInvoicePdf(invoice);
            User user = invoice.getClient().getUser();

            String upiLink =
                    "upi://pay?pa=" + user.getUpiId()
                            + "&pn=" + user.getAccountHolderName()
                            + "&am=" + invoice.getTotalAmount()
                            + "&cu=INR";

            byte[] qrBytes = QrCodeUtil.generateQrCode(upiLink);

            byte[] pdf = PdfGenerator.generateInvoicePdf(invoice, qrBytes, user);
       /*     MimeMessage message = mailSender.createMimeMessage();
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
            );*/

//            mailSender.send(message);
            Resend resend = new Resend(System.getenv("RESEND_API_KEY"));

            byte[] pdfBytes = PdfGenerator.generateInvoicePdf(invoice, qrBytes, user);

            Attachment attachment = Attachment.builder()
                    .fileName("invoice-" + invoice.getInvoiceNumber() + ".pdf")
                    .content(Base64.getEncoder().encodeToString(pdfBytes))
                    .build();

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("jayendrakasera95@gmail.com")
                    .to(invoice.getClient().getEmail())
                    .subject("Invoice " + invoice.getInvoiceNumber())
                    .html("<p>Please find your invoice attached.</p>")
                    .attachments(new Attachment[]{attachment})
                    .build();

            resend.emails().send(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}