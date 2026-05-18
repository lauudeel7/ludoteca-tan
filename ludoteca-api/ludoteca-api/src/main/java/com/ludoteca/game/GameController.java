package com.ludoteca.game;

import com.ludoteca.game.model.GameDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*")
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping
    public List<GameDTO> find(@RequestParam(required = false) String title, @RequestParam(required = false) Long categoryId) {
        return gameService.find(title, categoryId);
    }

    @GetMapping("/{id}")
    public GameDTO findById(@PathVariable Long id) {
        return gameService.findById(id);
    }

    @PostMapping
    public GameDTO save(@RequestBody GameDTO game) {
        return gameService.save(game);
    }

    @PutMapping("/{id}")
    public GameDTO update(@PathVariable Long id, @RequestBody GameDTO game) {
        return gameService.update(id, game);
    }
}
