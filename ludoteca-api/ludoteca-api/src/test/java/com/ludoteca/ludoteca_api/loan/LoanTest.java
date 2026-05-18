package com.ludoteca.ludoteca_api.loan;

import com.ludoteca.client.model.Client;
import com.ludoteca.game.model.Game;
import com.ludoteca.loan.LoanRepository;
import com.ludoteca.loan.LoanService;
import com.ludoteca.loan.model.Loan;
import com.ludoteca.loan.model.LoanDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    public void findFilteredLoansShouldReturnPaginatedLoans() {
        Long gameId = 1L;
        Long clientId = 1L;
        LocalDate searchDate = LocalDate.of(2026, 1, 3);
        Pageable pageable = PageRequest.of(0, 5);

        Game game = new Game();
        game.setId(gameId);
        game.setTitle("On Mars");

        Client client = new Client();
        client.setId(clientId);
        client.setName("Ana Martínez");

        Loan loan = new Loan();
        loan.setId(100L);
        loan.setGame(game);
        loan.setClient(client);
        loan.setStartDate(LocalDate.of(2026, 1, 1));
        loan.setEndDate(LocalDate.of(2026, 1, 6));

        List<Loan> list = new ArrayList<>();
        list.add(loan);
        Page<Loan> pageResult = new PageImpl<>(list, pageable, 1);

        when(loanRepository.findByFilters(gameId, clientId, searchDate, pageable)).thenReturn(pageResult);

        Page<LoanDTO> result = loanService.getFilteredLoans(gameId, clientId, searchDate, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("On Mars", result.getContent().get(0).getGame().getTitle());
        assertEquals("Ana Martínez", result.getContent().get(0).getClient().getName());
        verify(loanRepository, times(1)).findByFilters(gameId, clientId, searchDate, pageable);
    }
}