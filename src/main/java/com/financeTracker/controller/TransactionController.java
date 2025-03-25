package com.financeTracker.controller;

import com.financeTracker.dto.TransactionRequestDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.mapper.DtoGoalMapper;
import com.financeTracker.mapper.DtoTransactionMapper;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;

import java.util.List;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final DtoTransactionMapper dtoTransactionMapper = Mappers.getMapper(DtoTransactionMapper.class);;

    public TransactionResponseDto createTransaction(Long userId, TransactionRequestDto dto) {
        Transaction transaction = transactionService.createTransaction(userId,
                dto.getAmount(), dto.getCategory(), dto.getDescription(), dto.getType()
        );
        return dtoTransactionMapper.toResponseDTO(transaction);
    }

    public void deleteTransaction(Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    public TransactionResponseDto updateTransaction(Long userId, Long transactionId, TransactionRequestDto dto) {
        transactionService.updateTransaction(userId, transactionId, dto.getAmount(), dto.getCategory(), dto.getDescription(), dto.getType());
        return dtoTransactionMapper.toResponseDTO(transactionService.getTransactions(userId)
                .stream().filter(transaction -> transaction.getId().equals(transactionId))
                .findFirst().orElse(null));
        //TODO:исправить передачу с потенциальным null
    }

    public List<TransactionResponseDto> getTransactions(Long userId) {
        return transactionService.getTransactions(userId).stream()
                .map(dtoTransactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDto> getTransactionsByCategory(Long userId, String category) {
        return transactionService.getTransactionsByCategory(userId, category).stream()
                .map(dtoTransactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

