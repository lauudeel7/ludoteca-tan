package com.ludoteca.loan.model;

import com.ludoteca.client.model.ClientDTO;
import com.ludoteca.game.model.GameDTO;

import java.time.LocalDate;

public class LoanDTO {
    private Long id;
    private GameDTO game;
    private ClientDTO client;
    private LocalDate startDate;
    private LocalDate endDate;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }

    public ClientDTO getClient() {
        return client;
    }

    public void setClient(ClientDTO client) {
        this.client = client;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}