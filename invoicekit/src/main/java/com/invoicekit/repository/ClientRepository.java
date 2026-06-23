package com.invoicekit.repository;

import com.invoicekit.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByUserId(Long userId);

    List<Client> findByNameContainingIgnoreCaseAndUserId(
            String name,
            Long userId
    );

    Page<Client> findByUserId(Long userId, Pageable pageable);
}