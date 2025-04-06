//package com.financeTracker.service;
//
//import com.financeTracker.dao.UserDao;
//import com.financeTracker.exceptions.authorization.AuthorizationException;
//import com.financeTracker.exceptions.registration.RegistrationException;
//import com.financeTracker.model.entity.User;
//import com.financeTracker.service.impl.SecurityServiceImpl;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static com.financeTracker.constants.TestConstants.USER;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//
//@ExtendWith(MockitoExtension.class)
//public class SecurityServiceImplTest {
//
//
//    @Mock
//    private UserDao userDao;
//
//    @InjectMocks
//    private SecurityServiceImpl securityService;
//
//    @Test
//    @DisplayName("Should successfully register a new user")
//    void testRegister_Success() {
//        Mockito.when(userDao.findByLogin(USER.getLogin())).thenReturn(Optional.empty());
//        Mockito.when(userDao.save(any(User.class))).thenReturn(USER);
//
//        User registerUser = securityService.registration(USER.getLogin(), USER.getPassword());
//
//        assertThat(USER.getLogin()).isEqualTo(registerUser.getLogin());
//        assertThat(USER.getPassword()).isEqualTo(registerUser.getPassword());
//    }
//
//    @Test
//    @DisplayName("Should throw exception when user already exists")
//    void testRegister_ThrowException() {
//        Mockito.when(userDao.findByLogin(USER.getLogin())).thenReturn(Optional.of(USER));
//
//        assertThrows(RegistrationException.class, () -> securityService.registration(USER.getLogin(), USER.getPassword()));
//    }
//
//    @Test
//    @DisplayName("Should authorize user with correct credentials")
//    void testAuthorization_Success() {
//        Mockito.when(userDao.findByLogin(USER.getLogin())).thenReturn(Optional.of(USER));
//
//        User authorization = securityService.authorize(USER.getLogin(), USER.getPassword());
//
//        assertThat(USER.getLogin()).isEqualTo(authorization.getLogin());
//        assertThat(USER.getPassword()).isEqualTo(authorization.getPassword());
//    }
//
//    @Test
//    @DisplayName("Should throw exception when user is not found")
//    void testAuthorization_ThrowException() {
//        Mockito.when(userDao.findByLogin(USER.getLogin())).thenReturn(Optional.empty());
//
//        assertThrows(AuthorizationException.class, () -> securityService.authorize(USER.getLogin(), USER.getPassword()));
//    }
//}
