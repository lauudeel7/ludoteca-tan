package com.ludoteca.ludoteca_api.game;

import com.ludoteca.author.model.Author;
import com.ludoteca.category.model.Category;
import com.ludoteca.game.GameRepository;
import com.ludoteca.game.GameServiceImpl;
import com.ludoteca.game.model.Game;
import com.ludoteca.game.model.GameDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameServiceImpl gameService;

    private Game mockGame;
    private List<Game> mockList;

    @BeforeEach
    public void setUp() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Eurogames");

        Author author = new Author();
        author.setId(3L);
        author.setName("Simone Luciani");
        author.setNationality("IT");

        mockGame = new Game();
        mockGame.setId(4L);
        mockGame.setTitle("Barrage");
        mockGame.setAge(14);
        mockGame.setCategory(category);
        mockGame.setAuthor(author);

        mockList = new ArrayList<>();
        mockList.add(mockGame);
    }

    @Test
    public void findWithTitleAndCategoryShouldInvokeSpecificQuery() {
        String title = "Barrage";
        Long categoryId = 1L;
        when(gameRepository.findByTitleContainingIgnoreCaseAndCategoryId(title, categoryId)).thenReturn(mockList);

        List<GameDTO> result = gameService.find(title, categoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Barrage", result.get(0).getTitle());
        verify(gameRepository, times(1)).findByTitleContainingIgnoreCaseAndCategoryId(title, categoryId);
    }

    @Test
    public void findWithTitleOnlyShouldInvokeTitleQuery() {
        String title = "Barrage";
        when(gameRepository.findByTitleContainingIgnoreCase(title)).thenReturn(mockList);

        List<GameDTO> result = gameService.find(title, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gameRepository, times(1)).findByTitleContainingIgnoreCase(title);
    }

    @Test
    public void findWithCategoryOnlyShouldInvokeCategoryQuery() {
        Long categoryId = 1L;
        when(gameRepository.findByCategoryId(categoryId)).thenReturn(mockList);

        List<GameDTO> result = gameService.find(null, categoryId);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gameRepository, times(1)).findByCategoryId(categoryId);
    }

    @Test
    public void findWithoutFiltersShouldInvokeFindAll() {
        when(gameRepository.findAll()).thenReturn(mockList);

        List<GameDTO> result = gameService.find(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(gameRepository, times(1)).findAll();
    }

    @Test
    public void findByIdShouldReturnMappedDto() {
        Long id = 4L;
        when(gameRepository.findById(id)).thenReturn(Optional.of(mockGame));

        GameDTO result = gameService.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Barrage", result.getTitle());
        verify(gameRepository, times(1)).findById(id);
    }

    @Test
    public void saveShouldInsertEntityAndReturnDto() {
        GameDTO inputDto = new GameDTO();
        inputDto.setTitle("Barrage");
        inputDto.setAge(14);
        inputDto.setCategoryId(1L);
        inputDto.setAuthorId(3L);

        when(gameRepository.save(any(Game.class))).thenReturn(mockGame);

        GameDTO result = gameService.save(inputDto);

        assertNotNull(result);
        assertEquals(4L, result.getId());
        assertEquals("Barrage", result.getTitle());
        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    public void updateShouldModifyTitleAndAgeAndReturnDto() {
        Long id = 4L;
        GameDTO inputDto = new GameDTO();
        inputDto.setTitle("Barrage Modificado");
        inputDto.setAge(16);

        Game updatedGame = new Game();
        updatedGame.setId(id);
        updatedGame.setTitle("Barrage Modificado");
        updatedGame.setAge(16);
        updatedGame.setCategory(mockGame.getCategory());
        updatedGame.setAuthor(mockGame.getAuthor());

        when(gameRepository.findById(id)).thenReturn(Optional.of(mockGame));
        when(gameRepository.save(any(Game.class))).thenReturn(updatedGame);

        GameDTO result = gameService.update(id, inputDto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Barrage Modificado", result.getTitle());
        assertEquals(16, result.getAge());
        verify(gameRepository, times(1)).findById(id);
        verify(gameRepository, times(1)).save(any(Game.class));
    }
}
