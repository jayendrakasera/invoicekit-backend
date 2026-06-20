package com.invoicekit.controller;

import com.invoicekit.dto.ClientDto;
import com.invoicekit.entity.Client;
import com.invoicekit.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/{userId}")
    public Client createClient(
            @PathVariable Long userId,
            @RequestBody ClientDto clientDto
    ) {
        return clientService.createClient(userId, clientDto);
    }

    @GetMapping("/{userId}")
    public List<Client> getAllClients(@PathVariable Long userId) {
        return clientService.getAllClients(userId);
    }

    @GetMapping("/single/{id}")
    public Client getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PutMapping("/{id}")
    public Client updateClient(
            @PathVariable Long id,
            @RequestBody ClientDto clientDto
    ) {
        return clientService.updateClient(id, clientDto);
    }

    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return "Client deleted successfully";
    }
}