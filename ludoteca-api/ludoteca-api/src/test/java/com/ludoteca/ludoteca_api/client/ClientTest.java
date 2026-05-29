package com.ludoteca.ludoteca_api.client;

import com.ludoteca.client.ClientRepository;
import com.ludoteca.client.ClientService;
import com.ludoteca.client.model.Client;
import com.ludoteca.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @Test
    public void getAllClientsShouldReturnAllClientsMappedToDto() {
        List<Client> list = new ArrayList<>();
        Client client = new Client();
        client.setId(1L);
        client.setName("Ana Martínez");
        list.add(client);

        when(clientRepository.findAll()).thenReturn(list);

        List<ClientDto> result = clientService.getAllClients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Ana Martínez", result.get(0).getName());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    public void saveNewClientWithUniqueNameShouldCreateClient() {
        ClientDto inputDto = new ClientDto();
        inputDto.setName("Pedro Picapiedra");

        Client savedClient = new Client();
        savedClient.setId(4L);
        savedClient.setName("Pedro Picapiedra");

        when(clientRepository.findByNameIgnoreCase("Pedro Picapiedra")).thenReturn(Optional.empty());
        when(clientRepository.save(any(Client.class))).thenReturn(savedClient);

        ClientDto result = clientService.saveClient(inputDto);

        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("Pedro Picapiedra", result.getName());
        verify(clientRepository, times(1)).findByNameIgnoreCase("Pedro Picapiedra");
        verify(clientRepository, times(1)).save(any(Client.class));
    }

    @Test
    public void saveClientWithExistingNameShouldThrowIllegalArgumentException() {
        ClientDto inputDto = new ClientDto();
        inputDto.setName("Ana Martínez");

        Client existingClient = new Client();
        existingClient.setId(1L);
        existingClient.setName("Ana Martínez");

        when(clientRepository.findByNameIgnoreCase("Ana Martínez")).thenReturn(Optional.of(existingClient));

        assertThrows(IllegalArgumentException.class, () -> {
            clientService.saveClient(inputDto);
        });

        verify(clientRepository, times(1)).findByNameIgnoreCase("Ana Martínez");
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void deleteClientShouldInvokeRepositoryDelete() {
        Long id = 1L;

        clientService.deleteClient(id);

        verify(clientRepository, times(1)).deleteById(id);
    }
}