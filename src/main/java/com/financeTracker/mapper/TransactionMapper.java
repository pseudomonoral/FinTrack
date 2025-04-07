package com.financeTracker.mapper;

import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TransactionMapper {
    TransactionMapper INSTANCE = Mappers.getMapper(TransactionMapper.class);

    TransactionResponseDto toResponseTransactionDto(Transaction transaction);
}
