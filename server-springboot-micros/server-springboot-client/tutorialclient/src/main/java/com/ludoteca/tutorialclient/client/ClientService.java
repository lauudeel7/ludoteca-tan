package com.ludoteca.tutorialclient.client;

import com.ludoteca.tutorialclient.client.model.Client;
import com.ludoteca.tutorialclient.client.model.ClientDto;

import java.util.List;

public interface ClientService {
    Client get(Long id);

    List<Client> findAll();

    void save(Long id, ClientDto dto);

    void delete(Long id) throws Exception;
}
