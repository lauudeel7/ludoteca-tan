package com.ludoteca.loan;

import com.ludoteca.client.model.Client;
import com.ludoteca.client.model.ClientDTO;
import com.ludoteca.game.model.Game;
import com.ludoteca.game.model.GameDTO;
import com.ludoteca.loan.model.Loan;
import com.ludoteca.loan.model.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    public Page<LoanDTO> getFilteredLoans(Long gameId, Long clientId, LocalDate date, Pageable pageable) {
        return loanRepository.findByFilters(gameId, clientId, date, pageable).map(this::convertToDto);
    }

    public LoanDTO saveLoan(LoanDTO dto) {
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("ERR_END_BEFORE_START");
        }
        if (ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) > 14) {
            throw new IllegalArgumentException("ERR_MAX_14_DAYS");
        }

        List<Loan> gameOverlap = loanRepository.findOverlappingLoansForGame(dto.getGame().getId(), dto.getStartDate(), dto.getEndDate(), dto.getId());
        if (!gameOverlap.isEmpty()) {
            throw new IllegalArgumentException("ERR_GAME_ALREADY_LOANED");
        }

        List<Loan> clientLoans = loanRepository.findOverlappingLoansForClient(dto.getClient().getId(), dto.getStartDate(), dto.getEndDate(), dto.getId());

        long maxConcurrent = dto.getStartDate().datesUntil(dto.getEndDate().plusDays(1)).mapToLong(date -> clientLoans.stream().filter(l -> !date.isBefore(l.getStartDate()) && !date.isAfter(l.getEndDate())).count()).max().orElse(0);

        if (maxConcurrent >= 2) {
            throw new IllegalArgumentException("ERR_CLIENT_MAX_LOANS");
        }

        Loan loan = new Loan();
        loan.setId(dto.getId());
        loan.setStartDate(dto.getStartDate());
        loan.setEndDate(dto.getEndDate());

        Game game = new Game();
        game.setId(dto.getGame().getId());
        loan.setGame(game);
        Client client = new Client();
        client.setId(dto.getClient().getId());
        loan.setClient(client);

        Loan saved = loanRepository.save(loan);
        return convertToDto(saved);
    }

    public void deleteLoan(Long id) {
        loanRepository.deleteById(id);
    }

    private LoanDTO convertToDto(Loan loan) {
        LoanDTO dto = new LoanDTO();
        dto.setId(loan.getId());
        dto.setStartDate(loan.getStartDate());
        dto.setEndDate(loan.getEndDate());

        GameDTO gameDto = new GameDTO();
        gameDto.setId(loan.getGame().getId());
        gameDto.setTitle(loan.getGame().getTitle());
        gameDto.setAge(loan.getGame().getAge());

        if (loan.getGame().getCategory() != null) {
            gameDto.setCategoryId(loan.getGame().getCategory().getId());
            gameDto.setCategoryName(loan.getGame().getCategory().getName());
        }

        if (loan.getGame().getAuthor() != null) {
            gameDto.setAuthorId(loan.getGame().getAuthor().getId());
            gameDto.setAuthorName(loan.getGame().getAuthor().getName());
            gameDto.setAuthorNationality(loan.getGame().getAuthor().getNationality());
        }
        dto.setGame(gameDto);

        ClientDTO clientDto = new ClientDTO();
        clientDto.setId(loan.getClient().getId());
        clientDto.setName(loan.getClient().getName());
        dto.setClient(clientDto);

        return dto;
    }
}