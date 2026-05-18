package com.ludoteca.client;

import com.ludoteca.client.model.Client;
import com.ludoteca.client.model.ClientDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream().map(client -> new ClientDTO(client.getId(), client.getName())).collect(Collectors.toList());
    }

    public ClientDTO saveClient(ClientDTO clientDto) {
        Optional<Client> existing = clientRepository.findByNameIgnoreCase(clientDto.getName().trim());
        if (existing.isPresent() && (clientDto.getId() == null || !existing.get().getId().equals(clientDto.getId()))) {
            throw new IllegalArgumentException("CLIENT_NAME_EXISTS");
        }

        Client client = new Client();
        client.setId(clientDto.getId());
        client.setName(clientDto.getName().trim());

        Client savedClient = clientRepository.save(client);
        return new ClientDTO(savedClient.getId(), savedClient.getName());
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}