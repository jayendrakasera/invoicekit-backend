package com.invoicekit.service;

import com.invoicekit.dto.ClientDto;
import com.invoicekit.entity.Client;

import java.util.List;

public interface ClientService {

    Client createClient(Long userId, ClientDto clientDto);

    List<Client> getAllClients(Long userId);

    Client getClientById(Long id);

    Client updateClient(Long id, ClientDto clientDto);

    void deleteClient(Long id);
}