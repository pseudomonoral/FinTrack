//package com.servlet;
//
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@DisplayName("Logout servlet test")
//public class LogoutServletTest extends AbstractServletTest {
//
//    @InjectMocks
//    private LogoutServlet servlet;
//
//    @Test
//    @DisplayName("Log out, expected success")
//    public void doGet() {
//        when(request.getSession()).thenReturn(session);
//
//        servlet.doGet(request, response);
//
//        verify(session).invalidate();
//    }
//}
