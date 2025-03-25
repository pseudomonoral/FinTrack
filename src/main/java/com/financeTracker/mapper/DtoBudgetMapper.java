package com.financeTracker.mapper;

import com.financeTracker.dto.BudgetResponseDto;
import com.financeTracker.model.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DtoBudgetMapper {

    @Mapping(target = "limit", source = "budget.limit")
    BudgetResponseDto toResponse(Budget budget);

    default BudgetResponseDto toResponse(Double limit) {
        BudgetResponseDto response = new BudgetResponseDto();
        response.setLimit(limit);
        return response;
    }
}

