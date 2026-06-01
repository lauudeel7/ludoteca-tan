package com.ludoteca.loan;

import com.ludoteca.common.criteria.SearchCriteria; // Ajusta según tu paquete base
import com.ludoteca.loan.model.Loan;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LoanSpecification implements Specification<Loan> {

    private final SearchCriteria criteria;

    public LoanSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        if (criteria.getValue() == null)
            return null;

        if (criteria.getOperation().equalsIgnoreCase("isBetween") && criteria.getValue() instanceof LocalDate) {
            LocalDate date = (LocalDate) criteria.getValue();
            return builder.and(builder.lessThanOrEqualTo(root.get("startDate"), date), builder.greaterThanOrEqualTo(root.get("endDate"), date));
        }

        if (criteria.getOperation().equalsIgnoreCase(":")) {
            String[] split = criteria.getKey().split("[.]", 0);
            Path<?> expression = root.get(split[0]);
            for (int i = 1; i < split.length; i++) {
                expression = expression.get(split[i]);
            }
            return builder.equal(expression, criteria.getValue());
        }

        return null;
    }
}
