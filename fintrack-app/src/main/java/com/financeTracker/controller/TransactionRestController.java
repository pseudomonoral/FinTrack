package com.financeTracker.controller;

import com.financeTracker.dto.transaction.TransactionRequestDto;
import com.financeTracker.dto.transaction.TransactionResponseDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.model.type.TransactionType;
import com.financeTracker.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Транзакции", description = "Создание, обновление, удаление и получение транзакций")
@Validated
public class TransactionRestController {

    private final TransactionService transactionService;

    @Operation(summary = "Создание транзакции")
    @PostMapping
    public ResponseEntity<TransactionResponseDto> createTransaction(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Valid @RequestBody TransactionRequestDto requestDto) {

        TransactionResponseDto created = transactionService.createTransaction(user.getId(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Обновление транзакции")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransaction(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @PathVariable("id") Long transactionId,
            @Valid @RequestBody TransactionRequestDto requestDto) {

        transactionService.updateTransaction(user.getId(), transactionId, requestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление транзакции")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "Идентификатор транзакции") @PathVariable("id") Long transactionId) {

        transactionService.deleteTransaction(transactionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получение списка транзакций")
    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getTransactions(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Parameter(description = "Фильтр по категории") @RequestParam(required = false) String category,
            @Parameter(description = "Фильтр по типу транзакции (INCOME или EXPENSE)") @RequestParam(required = false) String type) {

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
