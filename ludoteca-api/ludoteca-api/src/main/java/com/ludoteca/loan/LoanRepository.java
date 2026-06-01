package com.ludoteca.loan;

import com.ludoteca.loan.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends CrudRepository<Loan, Long>, JpaSpecificationExecutor<Loan> {

    @Override
    @EntityGraph(attributePaths = { "game", "client" })
    Page<Loan> findAll(Specification<Loan> spec, Pageable pageable);

    boolean existsByClientId(Long idClient);
}
