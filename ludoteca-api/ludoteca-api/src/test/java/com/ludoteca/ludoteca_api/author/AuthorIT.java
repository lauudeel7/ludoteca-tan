package com.ludoteca.ludoteca_api.author;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthorIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "/authors";

    @Test
    public void getAuthorsPageShouldReturnInitialAuthorsFromDatabase() throws Exception {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(BASE_URL).queryParam("page", 0).queryParam("size", 5);

        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, HttpEntity.EMPTY, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        JsonNode root = objectMapper.readTree(response.getBody());
        long totalElements = root.path("totalElements").asLong();

        assertEquals(6, totalElements);

        JsonNode firstAuthor = root.path("content").get(0);
        assertNotNull(firstAuthor);
        assertEquals("Alan R. Moon", firstAuthor.path("name").asText());
    }
}