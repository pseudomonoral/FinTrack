package com.financeTracker.mapper;

import com.financeTracker.dto.BudgetResponseDto;
import com.financeTracker.model.entity.Budget;
import org.mapstruct.Mapper;

@Mapper
public interface DtoBudgetMapper {

    BudgetResponseDto toResponse(Budget budget);

    default BudgetResponseDto toResponse(Double limit) {
        BudgetResponseDto response = new BudgetResponseDto();
        response.setLimit(limit);
        return response;
    }
}

