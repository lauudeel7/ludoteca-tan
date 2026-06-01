package com.ludoteca.loan;

import com.ludoteca.loan.model.Loan;
import com.ludoteca.loan.model.LoanDto;
import com.ludoteca.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

public interface LoanService {

    /**
     * Recupera una página de préstamos aplicando los filtros de juego, cliente y fecha.
     *
     * @param dto datos de la entidad
     * @return {@link Page} de {@link Loan}
     */
    Page<Loan> findPage(LoanSearchDto dto, org.springframework.data.domain.Pageable pageable);

    /**
     * Guarda o modifica un préstamo, aplicando las reglas de negocio y validaciones.
     *
     * @param id  PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, LoanDto dto);

    /**
     * Elimina un préstamo por su identificador.
     *
     * @param id PK de la entidad
     */
    void delete(Long id);

    /**
     * Comprueba si un cliente tiene préstamos asociados.
     *
     * @param idClient PK del cliente
     * @return true si tiene préstamos, false en caso contrario
     */
    boolean existsByClientId(Long idClient);
}
