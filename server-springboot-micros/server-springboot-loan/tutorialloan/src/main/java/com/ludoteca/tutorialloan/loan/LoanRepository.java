package com.ludoteca.tutorialloan.loan;

import com.ludoteca.tutorialloan.loan.model.Loan;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {
    @Query("SELECT l FROM Loan l WHERE l.gameId = :gameId AND l.id <> :id AND NOT (l.returnDate < :loanDate OR l.loanDate > :returnDate)")
    List<Loan> findOverlappingGameLoans(@Param("id") Long id, @Param("gameId") Long gameId, @Param("loanDate") LocalDate loanDate, @Param("returnDate") LocalDate returnDate);

    @Query("SELECT l FROM Loan l WHERE l.clientId = :clientId AND l.id <> :id AND NOT (l.returnDate < :loanDate OR l.loanDate > :returnDate)")
    List<Loan> findOverlappingClientLoans(@Param("id") Long id, @Param("clientId") Long clientId, @Param("loanDate") LocalDate loanDate, @Param("returnDate") LocalDate returnDate);

    boolean existsByClientId(Long clientId);
}
