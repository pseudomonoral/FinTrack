package com.financeTracker.service;



import com.financeTracker.dao.GoalDao;
import com.financeTracker.service.impl.GoalServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalDao goalDao;

    @InjectMocks
    private GoalServiceImpl goalService;
}

