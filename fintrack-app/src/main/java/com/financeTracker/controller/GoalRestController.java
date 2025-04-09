package com.financeTracker.controller;

import com.financeTracker.dto.goal.GoalRequestDto;
import com.financeTracker.dto.goal.GoalResponseDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.GoalService;
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

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "Финансовые цели", description = "Управление целями пользователя")
@Validated
public class GoalRestController {

    private final GoalService goalService;

    @Operation(summary = "Создать новую цель")
    @PostMapping
    public ResponseEntity<GoalResponseDto> createGoal(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Valid @RequestBody GoalRequestDto goalRequestDto) {
        GoalResponseDto createdGoal = goalService.createGoal(user.getId(), goalRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
    }

    @Operation(summary = "Получить список целей или конкретную цель по названию")
    @GetMapping
    public ResponseEntity<?> getGoals(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Parameter(description = "Название цели для поиска (необязательно)")
            @RequestParam(required = false) String name) {
        if (name != null) {
            GoalResponseDto goal = goalService.getGoal(
                    GoalResponseDto.builder().name(name).build(), user.getId());
            return ResponseEntity.ok(goal);
        } else {
            List<GoalResponseDto> goals = goalService.getAllGoals(user.getId());
            return ResponseEntity.ok(goals);
        }
    }

    @Operation(summary = "Обновить цель")
    @PutMapping
    public ResponseEntity<Void> updateGoal(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Valid @RequestBody GoalRequestDto goalRequestDto) {
        goalService.updateGoal(user.getId(), goalRequestDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить цель по названию")
    @DeleteMapping
    public ResponseEntity<Void> deleteGoal(
            @Parameter(hidden = true) @SessionAttribute("user") UserDto user,
            @Parameter(description = "Название цели для удаления")
            @RequestParam String name) {
        goalService.deleteGoal(GoalRequestDto.builder().name(name).build(), user.getId());
        return ResponseEntity.noContent().build();
    }
}