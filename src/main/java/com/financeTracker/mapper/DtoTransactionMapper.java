package com.financeTracker.mapper;

import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface DtoTransactionMapper {

    @Mapping(source = "id", target = "id")
    TransactionResponseDto toResponseTransactionDto(Transaction transaction);
}

