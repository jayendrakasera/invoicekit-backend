package com.invoicekit.repository;

//import com.invoicekit.entity.Invoice;
import com.invoicekit.entity.InvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
//    List<Invoice> findByClientUserId(Long userId);
List<InvoiceItem> findByInvoiceClientUserId(Long userId);
}