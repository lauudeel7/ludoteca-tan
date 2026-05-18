package com.ludoteca.game;

import com.ludoteca.game.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    boolean existsByAuthorId(Long authorId);

    boolean existsByCategoryId(Long categoryId);

    List<Game> findByTitleContainingIgnoreCase(String title);

    List<Game> findByCategoryId(Long categoryId);

    List<Game> findByTitleContainingIgnoreCaseAndCategoryId(String title, Long categoryId);
}
