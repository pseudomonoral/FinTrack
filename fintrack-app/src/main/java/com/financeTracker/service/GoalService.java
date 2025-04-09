package com.financeTracker.service;

import com.financeTracker.dto.goal.GoalRequestDto;
import com.financeTracker.dto.goal.GoalResponseDto;

import java.util.List;
import java.util.Optional;

/**
 * The GoalService interface provides the operations for managing user goals.
 * It includes methods for creating, retrieving, updating, deleting goals, and checking goal progress.
 */
public interface GoalService {

    /**
     * Creates a new goal for a specific user.
     *
     * @param userId The ID of the user creating the goal.
     * @param goalRequestDto The DTO containing goal creation data.
     * @return GoalResponseDto The created goal's response DTO.
     */
    GoalResponseDto createGoal(Long userId, GoalRequestDto goalRequestDto);

    /**
     * Retrieves a specific goal by its name and user ID from a given DTO.
     *
     * @param goalResponseDto A DTO containing goal identification info (name).
     * @param userId The ID of the user.
     * @return GoalResponseDto The retrieved goal response.
     */
    GoalResponseDto getGoal(GoalResponseDto goalResponseDto, Long userId);

    /**
     * Retrieves all goals for a specific user.
     *
     * @param userId The ID of the user.
     * @return List of GoalResponseDto containing all goals for the user.
     */
    List<GoalResponseDto> getAllGoals(Long userId);

    /**
     * Deletes a goal for a user based on the goal's name in the request DTO.
     *
     * @param userId The ID of the user.
     */
    void deleteGoal(GoalRequestDto requestDto, Long userId);

    /**
     * Updates an existing goal for a user.
     *
     * @param userId The ID of the user.
     * @param goalRequestDto The DTO containing updated goal data.
     */
    void updateGoal(Long userId, GoalRequestDto goalRequestDto);

    /**
     * Retrieves the currently active goal of a user.
     *
     * @param userId The ID of the user.
     * @return Optional containing GoalResponseDto if an active goal is found.
     */
    Optional<GoalResponseDto> getActualGoalByUserId(Long userId);

    /**
     * Deactivates the currently active goal of a user, if any.
     *
     * @param userId The ID of the user.
     */
    void deactivateGoalByUserId(Long userId);
}


