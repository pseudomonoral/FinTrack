package com.financeTracker.controller;

import com.financeTracker.dto.budget.BudgetCheckResponseDto;
import com.financeTracker.dto.budget.BudgetDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
@Tag(name = "Бюджет", description = "Операции с бюджетом пользователя")
@Validated
public class BudgetRestController {

    private final BudgetService budgetService;

    @Operation(summary = "Создать бюджет")
    @PostMapping
    public ResponseEntity<Void> createBudget(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Valid @RequestBody BudgetDto budgetDto) {
        budgetService.createBudget(user.getId(), budgetDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Получить бюджет пользователя")
    @GetMapping
    public ResponseEntity<BudgetDto> getBudget(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        return budgetService.getBudget(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Обновить бюджет")
    @PutMapping
    public ResponseEntity<Void> updateBudget(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Valid @RequestBody BudgetDto budgetDto) {
        budgetService.updateBudget(user.getId(), budgetDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить бюджет")
    @DeleteMapping
    public ResponseEntity<Void> deleteBudget(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        budgetService.deleteBudget(user.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Проверить, превышен ли бюджет")
    @GetMapping("/check-exceeded")
    public ResponseEntity<BudgetCheckResponseDto> checkExceeded(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user) {
        BudgetCheckResponseDto result = budgetService.isBudgetExceeded(user.getId());
        return ResponseEntity.ok(result);
    }
}

