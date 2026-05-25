package com.ludoteca.tutorialclient.client;

import com.ludoteca.tutorialclient.client.model.Client;
import com.ludoteca.tutorialclient.client.model.ClientDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    public Client get(Long id) {
        return this.clientRepository.findById(id).orElse(null);
    }

    @Override
    public List<Client> findAll() {
        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void save(Long id, ClientDto dto) {
        Client client = (id == null) ? new Client() : this.get(id);
        client.setName(dto.getName());
        this.clientRepository.save(client);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        String url = "http://localhost:8084/loan/exists-client/" + id;
        try {
            Boolean hasLoans = new RestTemplate().getForObject(url, Boolean.class);
            if (Boolean.TRUE.equals(hasLoans)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client has active loans");
            }
        } catch (Exception e) {
        }

        this.clientRepository.deleteById(id);
    }
}

