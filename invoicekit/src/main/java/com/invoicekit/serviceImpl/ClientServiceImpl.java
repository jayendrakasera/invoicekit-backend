package com.invoicekit.serviceImpl;

import com.invoicekit.dto.ClientDto;
import com.invoicekit.entity.Client;
import com.invoicekit.entity.User;
import com.invoicekit.repository.ClientRepository;
import com.invoicekit.repository.UserRepository;
import com.invoicekit.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    @Override
    public Client createClient(String email, ClientDto clientDto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Client client = new Client();
        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setGstNumber(clientDto.getGstNumber());
        client.setUser(user);

        return clientRepository.save(client);
    }

    @Override
    public List<Client> getAllClients(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return clientRepository.findByUserId(user.getId());
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    @Override
    public Client updateClient(Long id, ClientDto clientDto) {

        Client client = getClientById(id);

        client.setName(clientDto.getName());
        client.setEmail(clientDto.getEmail());
        client.setPhone(clientDto.getPhone());
        client.setAddress(clientDto.getAddress());
        client.setGstNumber(clientDto.getGstNumber());

        return clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}