package com.financeTracker.mapper;

import com.financeTracker.dto.BudgetDto;
import com.financeTracker.model.entity.Budget;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface BudgetMapper {
    BudgetMapper INSTANCE = Mappers.getMapper(BudgetMapper.class);

    BudgetDto toBudgetDto(Budget budget);
}
