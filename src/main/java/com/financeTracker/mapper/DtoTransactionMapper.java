package com.financeTracker.mapper;

import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.model.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper
public interface DtoTransactionMapper {

    TransactionResponseDto toResponseTransactionDto(Transaction transaction);
}

