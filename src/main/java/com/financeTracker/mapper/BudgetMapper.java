package com.financeTracker.mapper;

import com.financeTracker.dto.BudgetResponse;
import com.financeTracker.model.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BudgetMapper {

    @Mapping(target = "limit", source = "budget.limit")
    BudgetResponse toResponse(Budget budget);

    default BudgetResponse toResponse(Double limit) {
        BudgetResponse response = new BudgetResponse();
        response.setLimit(limit);
        return response;
    }
}

