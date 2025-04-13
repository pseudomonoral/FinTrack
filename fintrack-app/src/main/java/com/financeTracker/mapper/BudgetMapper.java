package com.financeTracker.mapper;

import com.financeTracker.dto.budget.BudgetRequestDto;
import com.financeTracker.model.entity.Budget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    BudgetRequestDto toBudgetDto(Budget budget);
}
