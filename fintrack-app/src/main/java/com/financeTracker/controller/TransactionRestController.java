package com.financeTracker.controller;

import com.financeTracker.dto.TransactionRequestDto;
import com.financeTracker.dto.TransactionResponseDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionRestController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @SessionAttribute("user") UserDto user,
            @RequestBody @Valid TransactionRequestDto requestDto) {

        TransactionResponseDto created = transactionService.createTransaction(user.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransaction(
            @SessionAttribute("user") UserDto user,
            @PathVariable("id") Long transactionId,
            @RequestBody @Valid TransactionRequestDto requestDto) {

        transactionService.updateTransaction(user.getId(), transactionId, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable("id") Long transactionId) {
        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(
            @SessionAttribute("user") UserDto user,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type) {

        List<TransactionResponseDto> result;

        if (category != null) {
            result = transactionService.getTransactionsByCategory(user.getId(), category);
        } else if (type != null) {
            try {
                TransactionType.valueOf(type.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Недопустимое значение типа транзакции: " + type);
            }
            result = transactionService.getTransactionsByType(user.getId(), type.toUpperCase(Locale.ROOT));
        } else {
            result = transactionService.getTransactionsByUserId(user.getId());
        }

        return ResponseEntity.ok(result);
    }
}