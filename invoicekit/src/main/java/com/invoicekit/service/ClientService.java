package com.invoicekit.service;

import com.invoicekit.dto.ClientDto;
import com.invoicekit.entity.Client;

import java.util.List;

public interface ClientService {

    Client createClient(String email, ClientDto clientDto);

    List<Client> getAllClients(String email);

    Client getClientById(Long id);

    Client updateClient(Long id, ClientDto clientDto);

    void deleteClient(Long id);
}