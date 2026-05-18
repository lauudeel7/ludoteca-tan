package com.ludoteca.loan;

import com.ludoteca.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    @Query("SELECT l FROM Loan l WHERE " + "(:gameId IS NULL OR l.game.id = :gameId) AND " + "(:clientId IS NULL OR l.client.id = :clientId) AND " + "(:searchDate IS NULL OR (:searchDate BETWEEN l.startDate AND l.endDate))")
    Page<Loan> findByFilters(@Param("gameId") Long gameId, @Param("clientId") Long clientId, @Param("searchDate") LocalDate searchDate, Pageable pageable);

    @Query("SELECT l FROM Loan l WHERE l.game.id = :gameId AND " + "(:id IS NULL OR l.id <> :id) AND " + "(l.startDate <= :endDate AND l.endDate >= :startDate)")
    List<Loan> findOverlappingLoansForGame(@Param("gameId") Long gameId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("id") Long id);

    @Query("SELECT l FROM Loan l WHERE l.client.id = :clientId AND " + "(:id IS NULL OR l.id <> :id) AND " + "(l.startDate <= :endDate AND l.endDate >= :startDate)")
    List<Loan> findOverlappingLoansForClient(@Param("clientId") Long clientId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("id") Long id);
}
