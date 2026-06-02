package com.ludoteca.ludoteca_api.loan;

import com.ludoteca.client.model.ClientDto;
import com.ludoteca.game.model.GameDto;
import com.ludoteca.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void saveWithEndDateBeforeStartDateShouldReturnBadRequest() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setStartDate(LocalDate.of(2026, 6, 10));
        dto.setEndDate(LocalDate.of(2026, 6, 5));
        dto.setGame(gameDto);
        dto.setClient(clientDto);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void saveWithPeriodOver14DaysShouldReturnBadRequest() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setStartDate(LocalDate.of(2026, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 6, 20));
        dto.setGame(gameDto);
        dto.setClient(clientDto);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteWithIdShouldWorkCorrectly() {
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/1", HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void existsByClientIdShouldReturnBoolean() {
        ResponseEntity<Boolean> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/exists-client/1", HttpMethod.GET, null, Boolean.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}

