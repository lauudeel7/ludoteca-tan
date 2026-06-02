package com.ludoteca.ludoteca_api.client;

import com.ludoteca.client.ClientRepository;
import com.ludoteca.client.ClientServiceImpl;
import com.ludoteca.client.model.Client;
import com.ludoteca.client.model.ClientDto;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.loan.LoanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

    public static final String CLIENT_NAME = "Cliente 1";
    public static final String DUPLICATED_NAME = "Cliente Duplicado";
    public static final Long EXISTS_CLIENT_ID = 1L;
    public static final Long NOT_EXISTS_CLIENT_ID = 0L;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void findAllShouldReturnAllClients() {
        List<Client> list = new ArrayList<>();
        Client client = new Client();
        list.add(client);

        when(clientRepository.findAll()).thenReturn(list);

        List<Client> clients = clientService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    @Test
    public void saveNotExistsClientIdAndUniqueNameShouldInsert() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        when(clientRepository.existsByName(CLIENT_NAME)).thenReturn(false);

        clientService.save(null, clientDto);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(clientCaptor.capture());
        assertEquals(CLIENT_NAME, clientCaptor.getValue().getName());
    }

    @Test
    public void saveNewClientWithExistingNameShouldThrowBadRequestException() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(DUPLICATED_NAME);

        when(clientRepository.existsByName(DUPLICATED_NAME)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            clientService.save(null, clientDto);
        });

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    public void saveExistsClientIdAndUniqueNameShouldUpdate() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        Client client = new Client();
        client.setId(EXISTS_CLIENT_ID);
        client.setName("Nombre Antiguo");

        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));
        when(clientRepository.existsByName(CLIENT_NAME)).thenReturn(false);

        clientService.save(EXISTS_CLIENT_ID, clientDto);

        verify(clientRepository).save(client);
        assertEquals(CLIENT_NAME, client.getName());
    }

    @Test
    public void deleteExistsClientIdWithoutLoansShouldDelete() {
        when(clientRepository.existsById(EXISTS_CLIENT_ID)).thenReturn(true);
        when(loanRepository.existsByClientId(EXISTS_CLIENT_ID)).thenReturn(false);

        clientService.delete(EXISTS_CLIENT_ID);

        verify(clientRepository).deleteById(EXISTS_CLIENT_ID);
    }

    @Test
    public void deleteClientWithLoansShouldThrowBadRequestException() {
        when(clientRepository.existsById(EXISTS_CLIENT_ID)).thenReturn(true);
        when(loanRepository.existsByClientId(EXISTS_CLIENT_ID)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            clientService.delete(EXISTS_CLIENT_ID);
        });

        verify(clientRepository, never()).deleteById(anyLong());
    }

    @Test
    public void getExistsClientIdShouldReturnClient() {
        Client client = new Client();
        client.setId(EXISTS_CLIENT_ID);
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        Client clientResponse = clientService.get(EXISTS_CLIENT_ID);

        assertNotNull(clientResponse);
        assertEquals(EXISTS_CLIENT_ID, clientResponse.getId());
    }

    @Test
    public void getNotExistsClientIdShouldReturnNull() {
        when(clientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty());

        Client client = clientService.get(NOT_EXISTS_CLIENT_ID);

        assertNull(client);
    }
}