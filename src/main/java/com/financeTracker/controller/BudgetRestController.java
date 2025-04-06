package com.financeTracker.controller;

import com.financeTracker.dto.BudgetCheckResponseDto;
import com.financeTracker.dto.BudgetDto;
import com.financeTracker.dto.UserDto;
import com.financeTracker.service.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/budget")
@RequiredArgsConstructor
public class BudgetRestController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Void> createBudget(@SessionAttribute("user") UserDto user,
                                             @Valid @RequestBody BudgetDto budgetDto) {
        budgetService.createBudget(user.getId(), budgetDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<BudgetDto> getBudget(@SessionAttribute("user") UserDto user) {
        return budgetService.getBudget(user.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Void> updateBudget(@SessionAttribute("user") UserDto user,
                                             @Valid @RequestBody BudgetDto budgetDto) {
        budgetService.updateBudget(user.getId(), budgetDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteBudget(@SessionAttribute("user") UserDto user) {
        budgetService.deleteBudget(user.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-exceeded")
    public ResponseEntity<BudgetCheckResponseDto> checkExceeded(@SessionAttribute("user") UserDto user) {
        BudgetCheckResponseDto result = budgetService.isBudgetExceeded(user.getId());
        return ResponseEntity.ok(result);
    }
}
