package com.ludoteca.ludoteca_api.loan;

import com.ludoteca.client.ClientService;
import com.ludoteca.client.model.Client;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameService;
import com.ludoteca.game.model.Game;
import com.ludoteca.game.model.GameDto;
import com.ludoteca.loan.LoanRepository;
import com.ludoteca.loan.LoanServiceImpl;
import com.ludoteca.loan.model.Loan;
import com.ludoteca.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GameService gameService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void saveWithEndDateBeforeStartDateShouldThrowBadRequestException() {
        LoanDto dto = new LoanDto();
        dto.setStartDate(LocalDate.of(2026, 6, 10));
        dto.setEndDate(LocalDate.of(2026, 6, 5));

        assertThrows(BadRequestException.class, () -> {
            loanService.save(null, dto);
        });

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void saveWithPeriodOver14DaysShouldThrowBadRequestException() {
        LoanDto dto = new LoanDto();
        dto.setStartDate(LocalDate.of(2026, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 6, 20));

        assertThrows(BadRequestException.class, () -> {
            loanService.save(null, dto);
        });

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void saveWithGameAlreadyLoanedOnSameDaysShouldThrowBadRequestException() {
        Game gameEntity = new Game();
        gameEntity.setId(1L);

        Client client1 = new Client();
        client1.setId(1L);

        Client client2 = new Client();
        client2.setId(2L);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        com.ludoteca.client.model.ClientDto clientDto2 = new com.ludoteca.client.model.ClientDto();
        clientDto2.setId(2L);

        LoanDto dto = new LoanDto();
        dto.setStartDate(LocalDate.of(2026, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 6, 5));
        dto.setGame(gameDto);
        dto.setClient(clientDto2);

        Loan existingLoan = new Loan();
        existingLoan.setId(99L);
        existingLoan.setStartDate(LocalDate.of(2026, 6, 2));
        existingLoan.setEndDate(LocalDate.of(2026, 6, 4));
        existingLoan.setGame(gameEntity);
        existingLoan.setClient(client1);

        List<Loan> allLoans = new ArrayList<>();
        allLoans.add(existingLoan);

        when(loanRepository.findAll()).thenReturn(allLoans);

        assertThrows(BadRequestException.class, () -> {
            loanService.save(null, dto);
        });

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void saveWithClientOverMaximumLoansShouldThrowBadRequestException() {
        Game game1 = new Game();
        game1.setId(1L);

        Game game2 = new Game();
        game2.setId(2L);

        Game game3 = new Game();
        game3.setId(3L);

        Client clientEntity = new Client();
        clientEntity.setId(1L);

        GameDto gameDto3 = new GameDto();
        gameDto3.setId(3L);

        com.ludoteca.client.model.ClientDto clientDto = new com.ludoteca.client.model.ClientDto();
        clientDto.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setStartDate(LocalDate.of(2026, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 6, 5));
        dto.setGame(gameDto3);
        dto.setClient(clientDto);

        Loan loan1 = new Loan();
        loan1.setId(101L);
        loan1.setStartDate(LocalDate.of(2026, 6, 1));
        loan1.setEndDate(LocalDate.of(2026, 6, 5));
        loan1.setGame(game1);
        loan1.setClient(clientEntity);

        Loan loan2 = new Loan();
        loan2.setId(102L);
        loan2.setStartDate(LocalDate.of(2026, 6, 1));
        loan2.setEndDate(LocalDate.of(2026, 6, 5));
        loan2.setGame(game2);
        loan2.setClient(clientEntity);

        List<Loan> allLoans = new ArrayList<>();
        allLoans.add(loan1);
        allLoans.add(loan2);

        when(loanRepository.findAll()).thenReturn(allLoans);

        assertThrows(BadRequestException.class, () -> {
            loanService.save(null, dto);
        });

        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    public void saveValidNewLoanShouldInsert() {
        Game gameEntity = new Game();
        gameEntity.setId(1L);

        Client clientEntity = new Client();
        clientEntity.setId(1L);

        GameDto gameDto = new GameDto();
        gameDto.setId(1L);

        com.ludoteca.client.model.ClientDto clientDto = new com.ludoteca.client.model.ClientDto();
        clientDto.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setStartDate(LocalDate.of(2026, 6, 1));
        dto.setEndDate(LocalDate.of(2026, 6, 5));
        dto.setGame(gameDto);
        dto.setClient(clientDto);

        when(loanRepository.findAll()).thenReturn(new ArrayList<>());
        when(gameService.get(1L)).thenReturn(gameEntity);
        when(clientService.get(1L)).thenReturn(clientEntity);

        loanService.save(null, dto);

        ArgumentCaptor<Loan> loanCaptor = ArgumentCaptor.forClass(Loan.class);
        verify(loanRepository).save(loanCaptor.capture());
        assertEquals(LocalDate.of(2026, 6, 1), loanCaptor.getValue().getStartDate());
        assertEquals(LocalDate.of(2026, 6, 5), loanCaptor.getValue().getEndDate());
    }

    @Test
    public void deleteShouldInvokeRepositoryDelete() {
        loanService.delete(1L);
        verify(loanRepository).deleteById(1L);
    }

    @Test
    public void existsByClientIdShouldReturnRepositoryResult() {
        when(loanRepository.existsByClientId(1L)).thenReturn(true);
        boolean result = loanService.existsByClientId(1L);
        assertTrue(result);
    }
}

