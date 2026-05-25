package com.ludoteca.tutorialloan.loan;

import com.ludoteca.tutorialloan.client.ClientClient;
import com.ludoteca.tutorialloan.game.GameClient;
import com.ludoteca.tutorialloan.loan.model.Loan;
import com.ludoteca.tutorialloan.loan.model.LoanDto;
import com.ludoteca.tutorialloan.loan.model.LoanSearchDto;
import jakarta.persistence.criteria.Predicate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameClient gameFeignClient;

    @Autowired
    ClientClient clientFeignClient;

    @Autowired
    ModelMapper mapper;

    @Override
    public Page<LoanDto> findPage(LoanSearchDto searchDto) {
        Specification<Loan> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (searchDto.getGameId() != null) {
                predicates.add(cb.equal(root.get("gameId"), searchDto.getGameId()));
            }
            if (searchDto.getClientId() != null) {
                predicates.add(cb.equal(root.get("clientId"), searchDto.getClientId()));
            }
            if (searchDto.getDate() != null) {
                predicates.add(cb.and(cb.lessThanOrEqualTo(root.get("loanDate"), searchDto.getDate()), cb.greaterThanOrEqualTo(root.get("returnDate"), searchDto.getDate())));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Loan> page = this.loanRepository.findAll(spec, searchDto.getPageable());
        return page.map(loan -> {
            LoanDto dto = mapper.map(loan, LoanDto.class);
            try {
                dto.setGame(gameFeignClient.getGame(loan.getGameId()));
                dto.setClient(clientFeignClient.getClient(loan.getClientId()));
            } catch (Exception e) {
            }
            return dto;
        });
    }

    @Override
    public void save(Long id, LoanDto dto) throws Exception {
        if (dto.getReturnDate().isBefore(dto.getLoanDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        long days = ChronoUnit.DAYS.between(dto.getLoanDate(), dto.getReturnDate());
        if (days > 14) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Long targetId = (id == null) ? 0L : id;

        List<Loan> gameOverlap = this.loanRepository.findOverlappingGameLoans(targetId, dto.getGame().getId(), dto.getLoanDate(), dto.getReturnDate());
        if (!gameOverlap.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        List<Loan> clientOverlap = this.loanRepository.findOverlappingClientLoans(targetId, dto.getClient().getId(), dto.getLoanDate(), dto.getReturnDate());
        if (clientOverlap.size() >= 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        Loan loan = (id == null) ? new Loan() : this.loanRepository.findById(id).orElseThrow(() -> new Exception("Not exists"));
        loan.setGameId(dto.getGame().getId());
        loan.setClientId(dto.getClient().getId());
        loan.setLoanDate(dto.getLoanDate());
        loan.setReturnDate(dto.getReturnDate());
        this.loanRepository.save(loan);
    }

    @Override
    public void delete(Long id) throws Exception {
        if (!this.loanRepository.existsById(id)) {
            throw new Exception("Not exists");
        }
        this.loanRepository.deleteById(id);
    }

    @Override
    public boolean existsByClientId(Long clientId) {
        return this.loanRepository.existsByClientId(clientId);
    }
}
