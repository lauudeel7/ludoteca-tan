package com.ludoteca.client;

import com.ludoteca.client.model.Client;
import com.ludoteca.client.model.ClientDto;

import java.util.List;

public interface ClientService {

    List<Client> findAll();

    void save(Long id, ClientDto dto);

    void delete(Long id) throws Exception;
}