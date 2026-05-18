package com.ludoteca.loan;

import com.ludoteca.loan.model.LoanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping
    public Page<LoanDTO> getLoans(@RequestParam(required = false) Long gameId, @RequestParam(required = false) Long clientId, @RequestParam(required = false) String date, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        LocalDate searchDate = (date != null && !date.isEmpty()) ? LocalDate.parse(date) : null;
        return loanService.getFilteredLoans(gameId, clientId, searchDate, PageRequest.of(page, size, Sort.by("id").ascending()));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody LoanDTO loanDto) {
        try {
            return ResponseEntity.ok(loanService.saveLoan(loanDto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.ok().build();
    }
}

