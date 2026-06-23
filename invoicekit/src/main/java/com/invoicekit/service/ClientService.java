package com.invoicekit.service;

import com.invoicekit.dto.ClientDto;
import com.invoicekit.entity.Client;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientService {

    Client createClient(String email, ClientDto clientDto);

    List<Client> getAllClients(String email);

    List<Client> searchClients(String keyword, String email);

    Client getClientById(Long id);

    Client updateClient(Long id, ClientDto clientDto);

    Page<Client> getClientsPaginated(
            String email,
            int page,
            int size
    );

    void deleteClient(Long id);
}