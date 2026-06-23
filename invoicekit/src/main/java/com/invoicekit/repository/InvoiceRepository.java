package com.invoicekit.repository;


import com.invoicekit.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByClientUserId(Long userId);

    List<Invoice> findByInvoiceNumberContainingIgnoreCaseAndClientUserId(
            String invoiceNumber,
            Long userId
    );

    List<Invoice> findByStatusAndClientUserId(
            String status,
            Long userId
    );

    List<Invoice> findByClientUserIdOrderByTotalAmountAsc(Long userId);

    List<Invoice> findByClientUserIdOrderByTotalAmountDesc(Long userId);

    List<Invoice> findByClientUserIdOrderByIssueDateAsc(Long userId);

    List<Invoice> findByClientUserIdOrderByIssueDateDesc(Long userId);

    Page<Invoice> findByClientUserId(
            Long userId,
            Pageable pageable
    );
}