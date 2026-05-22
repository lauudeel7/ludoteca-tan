package com.ludoteca.tutorialgame.game;

import com.ludoteca.tutorialgame.game.model.Game;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long>, JpaSpecificationExecutor<Game> {

}
