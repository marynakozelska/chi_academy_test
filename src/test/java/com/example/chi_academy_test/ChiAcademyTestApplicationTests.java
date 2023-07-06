package com.example.chi_academy_test;

import com.example.chi_academy_test.config.SecurityConfig;
import com.example.chi_academy_test.config.SpringConfig;
import com.example.chi_academy_test.repository.UserRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = {ChiAcademyTestApplication.class, SpringConfig.class, SecurityConfig.class})
public class ChiAcademyTestApplicationTests {
    private static final String REGISTRATION_URL = "/register";
    private static final String AUTH_URL = "/auth";
    private static final String USER_LOGIN = "i_am_new_user";
    private static final String USER_PASSWORD = "password2003";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void test01RegistrationTest() throws Exception {
        this.mockMvc.perform(post(REGISTRATION_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"login\": \"" + USER_LOGIN + "\",\n" +
                                "    \"password\": \"" + USER_PASSWORD + "\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void test02RegistrationTestDuplicate() throws Exception {
        this.mockMvc.perform(post(REGISTRATION_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"login\": \"" + USER_LOGIN + "\",\n" +
                                "    \"password\": \"" + USER_PASSWORD + "\"\n" +
                                "}"))
                .andExpect(status().is(409))
                .andReturn();
    }

    @Test
    public void test03AuthorizationTest() throws Exception {
        mockMvc
                .perform(post(AUTH_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"login\": \"" + USER_LOGIN + "\",\n" +
                                "    \"password\": \"" + USER_PASSWORD + "\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test04FailedAuthorizationTest() throws Exception {
        mockMvc
                .perform(post(AUTH_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"login\": \"" + USER_LOGIN + "\",\n" +
                                "    \"password\": \"pass\"\n" +
                                "}"))
                .andExpect(status().isUnauthorized());

        userRepository.deleteUserByLogin(USER_LOGIN);
    }
}
