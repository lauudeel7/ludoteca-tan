package com.ludoteca.loan;

import com.ludoteca.loan.model.Loan;
import com.ludoteca.loan.model.LoanDto;
import com.ludoteca.loan.model.LoanSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ModelMapper mapper;

    @Operation(summary = "Find Page", description = "Method that returns a filtered and paged list of Loans")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findPage(@RequestBody LoanSearchDto dto, org.springframework.data.domain.Pageable pageable) {
        Page<Loan> page = this.loanService.findPage(dto, pageable);
        return page.map(entity -> mapper.map(entity, LoanDto.class));
    }

    @Operation(summary = "Save or Update", description = "Method that saves or updates a Loan")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody LoanDto dto) {
        this.loanService.save(id, dto);
    }

    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) {
        this.loanService.delete(id);
    }

    @Operation(summary = "Exists by Client", description = "Method that checks if a client has any associated loans")
    @RequestMapping(path = "/exists-client/{idClient}", method = RequestMethod.GET)
    public boolean existsByClientId(@PathVariable("idClient") Long idClient) {
        return this.loanService.existsByClientId(idClient);
    }
}
