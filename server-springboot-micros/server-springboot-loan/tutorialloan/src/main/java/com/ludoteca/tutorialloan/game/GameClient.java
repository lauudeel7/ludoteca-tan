package com.ludoteca.tutorialloan.game;

import com.ludoteca.tutorialloan.game.model.GameDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "spring-cloud-eureka-client-game")
public interface GameClient {
    @GetMapping(path = "/game/{id}")
    GameDto getGame(@PathVariable("id") Long id);
}
