package com.financeTracker.mapper;

import com.financeTracker.dto.BudgetDto;
import com.financeTracker.model.entity.Budget;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BudgetMapper {
    BudgetDto toBudgetDto(Budget budget);
}
