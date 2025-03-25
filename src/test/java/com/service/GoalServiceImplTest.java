package com.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import com.financeTracker.dao.GoalDao;
import com.financeTracker.mapper.EntityGoalMapper;
import com.financeTracker.service.impl.GoalServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalDao goalDao;

    @Mock
    private EntityGoalMapper goalMapper;

    @InjectMocks
    private GoalServiceImpl goalService;
}

