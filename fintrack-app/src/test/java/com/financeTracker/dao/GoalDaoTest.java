package com.financeTracker.dao;

import com.financeTracker.dao.impl.GoalDaoImpl;
import com.financeTracker.model.entity.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

class GoalDaoTest extends AbstractDaoTest {

    private GoalDaoImpl goalDao;

    @BeforeEach
    void setUpDao() {
        goalDao = new GoalDaoImpl(jdbcTemplate);
    }

    @Override
    protected void setupSchema() {
        jdbcTemplate.execute("""
            CREATE SCHEMA IF NOT EXISTS fintrack;

            CREATE TABLE IF NOT EXISTS fintrack.goals (
                id SERIAL PRIMARY KEY,
                user_id BIGINT NOT NULL,
                name TEXT NOT NULL,
                target_amount NUMERIC NOT NULL,
                current_amount NUMERIC NOT NULL,
                goal_completed BOOLEAN NOT NULL,
                goal_actual BOOLEAN NOT NULL,
                created_at TIMESTAMP NOT NULL,
                updated_at TIMESTAMP NOT NULL
            );
        """);
    }

    @Test
    void saveAndFindById_shouldWorkCorrectly() {
        Goal goal = Goal.builder()
                .userId(1L)
                .name("Buy a car")
                .targetAmount(10000.0)
                .currentAmount(2000.0)
                .goalCompleted(false)
                .goalActual(true)
                .build();

        Goal saved = goalDao.save(goal);

        Optional<Goal> fromDb = goalDao.findById(saved.getId());
        assertTrue(fromDb.isPresent());

        Goal actual = fromDb.get();
        assertEquals("Buy a car", actual.getName());
        assertEquals(10000.0, actual.getTargetAmount(), 0.0001);
        assertEquals(2000.0, actual.getCurrentAmount(), 0.0001);
        assertFalse(actual.isGoalCompleted());
        assertTrue(actual.isGoalActual());
    }

    @Test
    void findAllByUserId_shouldReturnList() {
        Goal g1 = Goal.builder().userId(2L).name("Goal1").targetAmount(500.0).currentAmount(100.0).goalCompleted(false).goalActual(true).build();
        Goal g2 = Goal.builder().userId(2L).name("Goal2").targetAmount(1000.0).currentAmount(250.0).goalCompleted(false).goalActual(false).build();

        goalDao.save(g1);
        goalDao.save(g2);

        List<Goal> goals = goalDao.findAllByUserId(2L);
        assertEquals(2, goals.size());
    }

    @Test
    void findGoalByNameAndId_shouldReturnCorrectGoal() {
        Goal goal = Goal.builder()
                .userId(3L)
                .name("Vacation")
                .targetAmount(3000.0)
                .currentAmount(500.0)
                .goalCompleted(false)
                .goalActual(true)
                .build();

        goalDao.save(goal);

        Optional<Goal> result = goalDao.findGoalByNameAndId("Vacation", 3L);
        assertTrue(result.isPresent());
        assertEquals("Vacation", result.get().getName());
    }

    @Test
    void updateGoal_shouldModifyFields() {
        Goal goal = Goal.builder()
                .userId(4L)
                .name("Bike")
                .targetAmount(800.0)
                .currentAmount(100.0)
                .goalCompleted(false)
                .goalActual(true)
                .build();

        Goal saved = goalDao.save(goal);

        saved.setName("Mountain Bike");
        saved.setCurrentAmount(300.0);
        saved.setGoalCompleted(true);
        saved.setGoalActual(false);

        goalDao.updateGoal(saved);

        Optional<Goal> updated = goalDao.findById(saved.getId());
        assertTrue(updated.isPresent());
        assertEquals("Mountain Bike", updated.get().getName());
        assertEquals(300.0, updated.get().getCurrentAmount(), 0.0001);
        assertTrue(updated.get().isGoalCompleted());
        assertFalse(updated.get().isGoalActual());
    }

    @Test
    void delete_shouldRemoveGoal() {
        Goal goal = Goal.builder()
                .userId(5L)
                .name("Delete me")
                .targetAmount(1000.0)
                .currentAmount(0.0)
                .goalCompleted(false)
                .goalActual(true)
                .build();

        Goal saved = goalDao.save(goal);
        goalDao.delete(saved.getId());

        Optional<Goal> result = goalDao.findById(saved.getId());
        assertTrue(result.isEmpty());
    }
}
