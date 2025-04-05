package com.financeTracker.mapper;

import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.model.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionResponseDto toResponseTransactionDto(Transaction transaction);
}
