package com.ludoteca.game;

import com.ludoteca.author.model.Author;
import com.ludoteca.category.model.Category;
import com.ludoteca.game.model.Game;
import com.ludoteca.game.model.GameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public List<GameDTO> find(String title, Long categoryId) {

        List<Game> games;

        if (title != null && !title.isBlank() && categoryId != null) {
            games = gameRepository.findByTitleContainingIgnoreCaseAndCategoryId(title, categoryId);
        } else if (title != null && !title.isBlank()) {
            games = gameRepository.findByTitleContainingIgnoreCase(title);
        } else if (categoryId != null) {
            games = gameRepository.findByCategoryId(categoryId);
        } else {
            games = gameRepository.findAll();
        }

        return games.stream().map(this::mapToDTO).toList();
    }

    @Override
    public GameDTO findById(Long id) {
        return mapToDTO(gameRepository.findById(id).orElseThrow());
    }

    @Override
    public GameDTO save(GameDTO dto) {
        Game game = mapToEntity(dto);
        return mapToDTO(gameRepository.save(game));
    }

    @Override
    public GameDTO update(Long id, GameDTO dto) {
        Game game = gameRepository.findById(id).orElseThrow();

        game.setTitle(dto.getTitle());
        game.setAge(dto.getAge());

        return mapToDTO(gameRepository.save(game));
    }

    private GameDTO mapToDTO(Game g) {
        GameDTO dto = new GameDTO();
        dto.setId(g.getId());
        dto.setTitle(g.getTitle());
        dto.setAge(g.getAge());

        dto.setCategoryId(g.getCategory().getId());
        dto.setCategoryName(g.getCategory().getName());

        dto.setAuthorId(g.getAuthor().getId());
        dto.setAuthorName(g.getAuthor().getName());
        dto.setAuthorNationality(g.getAuthor().getNationality());

        return dto;
    }

    private Game mapToEntity(GameDTO dto) {
        Game game = new Game();
        game.setTitle(dto.getTitle());
        game.setAge(dto.getAge());

        Category category = new Category();
        category.setId(dto.getCategoryId());

        Author author = new Author();
        author.setId(dto.getAuthorId());

        game.setCategory(category);
        game.setAuthor(author);

        return game;
    }
}
