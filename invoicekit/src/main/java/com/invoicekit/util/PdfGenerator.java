package com.invoicekit.util;

import com.invoicekit.entity.Invoice;
import com.invoicekit.entity.InvoiceItem;
import com.invoicekit.entity.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;

public class PdfGenerator {

    public static byte[] generateInvoicePdf(Invoice invoice, byte[] qrBytes, User user) {

        try {
            Document document = new Document();
            ByteArrayOutputStream out = new ByteArrayOutputStream();

            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

//            document.add(new Paragraph("InvoiceKit (by JayendraKasera)", titleFont));
//            document.add(new Paragraph("(By JayendraKasera)", normalFont));
            Paragraph title = new Paragraph();
            title.add(new Chunk("InvoiceKit ", titleFont));
            title.add(new Chunk("by JayendraKasera", normalFont));
            document.add(title);

            document.add(new Paragraph("Invoice Number: " + invoice.getInvoiceNumber(), normalFont));
            document.add(new Paragraph("Issue Date: " + invoice.getIssueDate(), normalFont));
            document.add(new Paragraph("Due Date: " + invoice.getDueDate(), normalFont));

            document.add(new Paragraph(" "));

            document.add(new Paragraph("Client Details", titleFont));
            document.add(new Paragraph("Name: " + invoice.getClient().getName(), normalFont));
            document.add(new Paragraph("Email: " + invoice.getClient().getEmail(), normalFont));
            document.add(new Paragraph("Phone: " + invoice.getClient().getPhone(), normalFont));
            document.add(new Paragraph("Address: " + invoice.getClient().getAddress(), normalFont));

            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            table.addCell("Item");
            table.addCell("Quantity");
            table.addCell("Price");
            table.addCell("Amount");

            for (InvoiceItem item : invoice.getItems()) {
                table.addCell(item.getItemName());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell(String.valueOf(item.getPrice()));
                table.addCell(String.valueOf(item.getAmount()));
            }

            document.add(table);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Subtotal: ₹" + invoice.getSubtotal(), normalFont));
            document.add(new Paragraph("GST: ₹" + invoice.getGstAmount(), normalFont));
            document.add(new Paragraph("Total: ₹" + invoice.getTotalAmount(), titleFont));

            Image qrImage = Image.getInstance(qrBytes);

            qrImage.scaleToFit(150, 150);

            document.add(new Paragraph("Scan to Pay"));
            document.add(qrImage);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Bank Details"));
            document.add(new Paragraph(
                    "Bank Name: " + user.getBankName()
            ));
            document.add(new Paragraph(
                    "Account Holder: " + user.getAccountHolderName()
            ));
            document.add(new Paragraph(
                    "Account Number: " + user.getAccountNumber()
            ));
            document.add(new Paragraph(
                    "IFSC Code: " + user.getIfscCode()
            ));
            document.add(new Paragraph(
                    "UPI ID: " + user.getUpiId()
            ));

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF");
        }
    }
}