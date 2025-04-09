package com.financeTracker.controller;

import com.financeTracker.dto.goal.GoalRequestDto;
import com.financeTracker.dto.goal.GoalResponseDto;
import com.financeTracker.dto.user.UserDto;
import com.financeTracker.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalRestController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponseDto> createGoal(@SessionAttribute("user") UserDto user,
                                                      @Valid @RequestBody GoalRequestDto goalRequestDto) {
        GoalResponseDto createdGoal = goalService.createGoal(user.getId(), goalRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGoal);
    }

    @GetMapping
    public ResponseEntity<?> getGoals(@SessionAttribute("user") UserDto user,
                                      @RequestParam(required = false) String name) {
        if (name != null) {
            GoalResponseDto goal = goalService.getGoal(GoalResponseDto.builder().name(name).build(), user.getId());
            return ResponseEntity.ok(goal);
        } else {
            List<GoalResponseDto> goals = goalService.getAllGoals(user.getId());
            return ResponseEntity.ok(goals);
        }
    }

    @PutMapping
    public ResponseEntity<Void> updateGoal(@SessionAttribute("user") UserDto user,
                                           @Valid @RequestBody GoalRequestDto goalRequestDto) {
        goalService.updateGoal(user.getId(), goalRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteGoal(@SessionAttribute("user") UserDto user,
                                           @RequestParam String name) {
        goalService.deleteGoal(GoalRequestDto.builder().name(name).build(), user.getId());
        return ResponseEntity.noContent().build();
    }
}


