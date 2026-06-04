package com.ludoteca.loan;

import com.ludoteca.client.ClientService;
import com.ludoteca.common.criteria.SearchCriteria;
import com.ludoteca.exception.BadRequestException;
import com.ludoteca.game.GameService;
import com.ludoteca.loan.model.Loan;
import com.ludoteca.loan.model.LoanDto;
import com.ludoteca.loan.model.LoanSearchDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private ClientService clientService;

    @Override
    public Page<Loan> findPage(LoanSearchDto dto, org.springframework.data.domain.Pageable pageable) {
        Specification<Loan> spec = Specification.where(null);

        if (dto.getIdGame() != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("game.id", ":", dto.getIdGame())));
        }
        if (dto.getIdClient() != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("client.id", ":", dto.getIdClient())));
        }
        if (dto.getDate() != null) {
            spec = spec.and(new LoanSpecification(new SearchCriteria("date", "isBetween", dto.getDate())));
        }

        return this.loanRepository.findAll(spec, pageable);
    }

    @Override
    public void save(Long id, LoanDto dto) throws BadRequestException {
        // 1. Validación: Fecha de fin posterior o igual al inicio
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new BadRequestException("La fecha de fin no puede ser anterior a la de inicio.");
        }

        // 2. Validación: Período máximo de 14 días
        long days = ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate());
        if (days > 14) {
            throw new BadRequestException("El periodo máximo de préstamo no puede superar los 14 días.");
        }

        // Obtenemos todos los préstamos existentes para validar cruces de fechas
        List<Loan> allLoans = (List<Loan>) loanRepository.findAll();

        for (LocalDate date = dto.getStartDate(); !date.isAfter(dto.getEndDate()); date = date.plusDays(1)) {
            final LocalDate currentDate = date;

            List<Loan> activeLoansToday = allLoans.stream().filter(l -> !l.getId().equals(id)).filter(l -> !currentDate.isBefore(l.getStartDate()) && !currentDate.isAfter(l.getEndDate())).toList();

            // 3. Validación: Mismo juego prestado a dos clientes distintos el mismo día
            boolean gameAlreadyLoaned = activeLoansToday.stream().anyMatch(l -> l.getGame().getId().equals(dto.getGame().getId()));
            if (gameAlreadyLoaned) {
                throw new BadRequestException("El juego ya está prestado a otro cliente en los días seleccionados.");
            }

            // 4. Validación: Un mismo cliente no puede tener más de 2 juegos a la vez en un día
            long clientLoanCount = activeLoansToday.stream().filter(l -> l.getClient().getId().equals(dto.getClient().getId())).count();
            if (clientLoanCount >= 2) {
                throw new BadRequestException("El cliente ya tiene el cupo máximo de 2 préstamos activos en los días seleccionados.");
            }
        }

        Loan loan = (id == null) ? new Loan() : loanRepository.findById(id).orElseThrow();
        BeanUtils.copyProperties(dto, loan, "id", "game", "client");

        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientService.get(dto.getClient().getId()));

        this.loanRepository.save(loan);
    }

    @Override
    public void delete(Long id) {
        this.loanRepository.deleteById(id);
    }

    @Override
    public boolean existsByClientId(Long idClient) {
        return this.loanRepository.existsByClientId(idClient);
    }
}
