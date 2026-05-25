package com.ludoteca.tutorialloan.loan.model;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public class LoanSearchDto {
    private Long gameId;
    private Long clientId;
    private LocalDate date;
    private Pageable pageable;

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}

