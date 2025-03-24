package com.financeTracker.mapper;

import com.financeTracker.dto.TransactionResponseDTO;
import com.financeTracker.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta")
public interface TransactionMapper {

    @Mapping(source = "id", target = "id")
    TransactionResponseDTO toResponseDTO(Transaction transaction);
}

