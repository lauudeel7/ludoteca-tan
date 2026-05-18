package com.ludoteca.ludoteca_api.author;

import com.ludoteca.author.AuthorRepository;
import com.ludoteca.author.AuthorServiceImpl;
import com.ludoteca.author.model.Author;
import com.ludoteca.author.model.AuthorDTO;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Test
    public void findAllShouldReturnPaginatedAuthorsMappedToDTO() {
        int page = 0;
        int size = 5;
        List<Author> list = new ArrayList<>();

        Author author = new Author();
        author.setId(1L);
        author.setName("Alan R. Moon");
        author.setNationality("US");
        list.add(author);

        Page<Author> pageResult = new PageImpl<>(list, PageRequest.of(page, size), 1);
        when(authorRepository.findAll(PageRequest.of(page, size))).thenReturn(pageResult);

        Page<AuthorDTO> result = authorService.findAll(page, size);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("Alan R. Moon", result.getContent().get(0).getName());
        assertEquals("US", result.getContent().get(0).getNationality());
        verify(authorRepository, times(1)).findAll(PageRequest.of(page, size));
    }

    @Test
    public void saveShouldCreateAuthorAndReturnDTO() {
        AuthorDTO inputDto = new AuthorDTO();
        inputDto.setName("Nuevo Autor");
        inputDto.setNationality("ES");

        Author savedAuthor = new Author();
        savedAuthor.setId(7L);
        savedAuthor.setName("Nuevo Autor");
        savedAuthor.setNationality("ES");

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        AuthorDTO result = authorService.save(inputDto);

        assertNotNull(result);
        assertEquals(7L, result.getId());
        assertEquals("Nuevo Autor", result.getName());
        assertEquals("ES", result.getNationality());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void updateShouldModifyExistingAuthorAndReturnDTO() {
        Long id = 1L;
        AuthorDTO inputDto = new AuthorDTO();
        inputDto.setName("Alan R. Moon Modificado");
        inputDto.setNationality("US");

        Author existingAuthor = new Author();
        existingAuthor.setId(id);
        existingAuthor.setName("Alan R. Moon");
        existingAuthor.setNationality("US");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(id);
        updatedAuthor.setName("Alan R. Moon Modificado");
        updatedAuthor.setNationality("US");

        when(authorRepository.findById(id)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        AuthorDTO result = authorService.update(id, inputDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Alan R. Moon Modificado", result.getName());
        verify(authorRepository, times(1)).findById(id);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void deleteWithoutGamesShouldInvokeRepositoryDelete() {
        Long id = 2L;
        when(gameRepository.existsByAuthorId(id)).thenReturn(false);

        authorService.delete(id);

        verify(gameRepository, times(1)).existsByAuthorId(id);
        verify(authorRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteWithGamesShouldThrowBadRequestException() {
        Long id = 1L;
        when(gameRepository.existsByAuthorId(id)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> {
            authorService.delete(id);
        });

        verify(gameRepository, times(1)).existsByAuthorId(id);
        verify(authorRepository, never()).deleteById(id);
    }
}