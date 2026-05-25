package com.ludoteca.tutorialloan.loan;

import com.ludoteca.tutorialloan.loan.model.LoanDto;
import com.ludoteca.tutorialloan.loan.model.LoanSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Operation(summary = "Find Page", description = "Method that returns a page of Loans")
    @PostMapping(path = "")
    public Page<LoanDto> findPage(@RequestBody LoanSearchDto searchDto) {
        return this.loanService.findPage(searchDto);
    }

    @Operation(summary = "Save or Update", description = "Method that saves or updates a Loan")
    @PutMapping(path = { "", "/{id}" })
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) throws Exception {
        this.loanService.save(id, dto);
    }

    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") Long id) throws Exception {
        this.loanService.delete(id);
    }

    @Operation(summary = "Exists by Client", description = "Checks if a client has any loan")
    @GetMapping(path = "/exists-client/{id}")
    public boolean existsByClientId(@PathVariable("id") Long id) {
        return this.loanService.existsByClientId(id);
    }
}
