package com.ludoteca.ludoteca_api.client;

import com.ludoteca.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ClientIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "/clients";

    @Test
    public void getAllShouldReturnInitialClientsFromDatabase() {
        ParameterizedTypeReference<List<ClientDto>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, HttpEntity.EMPTY, responseType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("Ana Martínez", response.getBody().get(0).getName());
        assertEquals("Carlos Gómez", response.getBody().get(1).getName());
        assertEquals("Lucía Restrepo", response.getBody().get(2).getName());
    }

    @Test
    public void saveShouldCreateNewClientInDatabaseIfNameIsUnique() {
        ClientDto dto = new ClientDto();
        dto.setName("Roberto Sánchez");

        ResponseEntity<ClientDto> response = restTemplate.postForEntity(BASE_URL, dto, ClientDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Roberto Sánchez", response.getBody().getName());
    }

    @Test
    public void saveShouldReturnBadRequestIfNameAlreadyExists() {
        ClientDto dto = new ClientDto();
        dto.setName("Ana Martínez");

        ResponseEntity<String> response = restTemplate.postForEntity(BASE_URL, dto, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CLIENT_NAME_EXISTS", response.getBody());
    }

    @Test
    public void deleteShouldRemoveClientFromDatabase() {
        Long idEliminar = 3L;

        ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "/" + idEliminar, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}