package com.financeTracker.controller;

import com.financeTracker.dto.TransactionRequestDTO;
import com.financeTracker.dto.TransactionResponseDTO;
import com.financeTracker.mapper.TransactionMapper;
import com.financeTracker.model.entity.Transaction;
import com.financeTracker.service.TransactionService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
        Transaction transaction = transactionService.createTransaction(
                dto.getAmount(), dto.getCategory(), dto.getDescription(), dto.getType()
        );
        return transactionMapper.toResponseDTO(transaction);
    }

    public void deleteTransaction(Long transactionId) {
        transactionService.deleteTransaction(transactionId);
    }

    public TransactionResponseDTO updateTransaction(Long userId, Long transactionId, TransactionRequestDTO dto) {
        transactionService.updateTransaction(transactionId, dto.getAmount(), dto.getCategory(), dto.getDescription(), dto.getType());
        return transactionMapper.toResponseDTO(transactionService.getTransactions(userId)
                .stream().filter(transaction -> transaction.getId().equals(transactionId))
                .findFirst().orElse(null));
    }

    public List<TransactionResponseDTO> getTransactions(Long userId) {
        return transactionService.getTransactions(userId).stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionResponseDTO> getTransactionsByCategory(Long userId, String category) {
        return transactionService.getTransactionsByCategory(userId, category).stream()
                .map(transactionMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}

