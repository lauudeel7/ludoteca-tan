package com.ludoteca.tutorialloan.loan;

import com.ludoteca.tutorialloan.loan.model.LoanDto;
import com.ludoteca.tutorialloan.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

public interface LoanService {
    Page<LoanDto> findPage(LoanSearchDto searchDto);

    void save(Long id, LoanDto dto) throws Exception;

    void delete(Long id) throws Exception;

    boolean existsByClientId(Long clientId);
}
