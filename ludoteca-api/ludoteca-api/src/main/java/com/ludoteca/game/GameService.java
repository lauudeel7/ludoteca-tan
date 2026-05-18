package com.ludoteca.game;

import com.ludoteca.game.model.GameDTO;

import java.util.List;

public interface GameService {
    List<GameDTO> find(String title, Long categoryId);

    GameDTO findById(Long id);

    GameDTO save(GameDTO dto);

    GameDTO update(Long id, GameDTO dto);
}
