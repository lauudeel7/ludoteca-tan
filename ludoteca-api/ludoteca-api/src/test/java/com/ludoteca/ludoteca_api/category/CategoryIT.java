package com.ludoteca.ludoteca_api.category;

import com.ludoteca.category.model.CategoryDTO;
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
public class CategoryIT {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URL = "/categories";

    @Test
    public void findAllShouldReturnInitialCategoriesFromDatabase() {
        ParameterizedTypeReference<List<CategoryDTO>> responseType = new ParameterizedTypeReference<>() {
        };

        ResponseEntity<List<CategoryDTO>> response = restTemplate.exchange(BASE_URL, HttpMethod.GET, HttpEntity.EMPTY, responseType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        assertEquals("Eurogames", response.getBody().get(0).getName());
        assertEquals("Ameritrash", response.getBody().get(1).getName());
        assertEquals("Familiar", response.getBody().get(2).getName());
    }

    @Test
    public void createShouldInsertNewCategoryInDatabase() {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Party Games");

        ResponseEntity<CategoryDTO> response = restTemplate.postForEntity(BASE_URL, dto, CategoryDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Party Games", response.getBody().getName());
    }

    @Test
    public void updateShouldModifyExistingCategoryInDatabase() {
        Long idExistente = 1L;
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Eurogames Hardcore");

        HttpEntity<CategoryDTO> requestEntity = new HttpEntity<>(dto);
        ResponseEntity<CategoryDTO> response = restTemplate.exchange(BASE_URL + "/" + idExistente, HttpMethod.PUT, requestEntity, CategoryDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(idExistente, response.getBody().getId());
        assertEquals("Eurogames Hardcore", response.getBody().getName());
    }

    @Test
    public void deleteShouldRemoveCategoryFromDatabaseIfNoGamesAssociated() {
        Long idSinJuegos = 2L;

        ResponseEntity<Void> response = restTemplate.exchange(BASE_URL + "/" + idSinJuegos, HttpMethod.DELETE, HttpEntity.EMPTY, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}