package com.example.chi_academy_test;

import static org.junit.Assert.*;

import com.example.chi_academy_test.dto.AuthRequest;
import com.example.chi_academy_test.model.User;
import com.example.chi_academy_test.service.AuthService;
import jakarta.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=AuthService.class)
@Transactional
public class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    public void simpleRegistration() {
        AuthRequest authRequest = new AuthRequest("testUser", "password");
        assertTrue(authService.register(authRequest));

        assertNotNull();
    }
}
