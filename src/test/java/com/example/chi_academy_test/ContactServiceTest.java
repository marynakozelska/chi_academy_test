package com.example.chi_academy_test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.chi_academy_test.config.SecurityConfig;
import com.example.chi_academy_test.config.SpringConfig;
import com.example.chi_academy_test.repository.ContactRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@AutoConfigureMockMvc
@SpringBootTest(classes = {ChiAcademyTestApplication.class, SpringConfig.class, SecurityConfig.class})
public class ContactServiceTest {
    private static final String CONTACT_NAME = "Mary";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ContactRepository contactRepository;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void accessDeniedToContactsTest() throws Exception {
        this.mockMvc.perform(get("/contacts"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @WithMockUser("someUser")
    public void getContactsTest() throws Exception {
        this.mockMvc
                .perform(get("/contacts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @Transactional
    public void createContactTest() throws Exception {

        this.mockMvc
                .perform(post("/contacts")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"name\": \"" + CONTACT_NAME + "\",\n" +
                                "    \"emails\": [\n" +
                                "        \"mary2003@gmail.com\", \"marymary@gmail.com\"\n" +
                                "    ],\n" +
                                "    \"phones\": [\n" +
                                "        \"+380991112244\"\n" +
                                "    ]\n" +
                                "}"))
                .andExpect(status().isOk());

        contactRepository.deleteContactPhonesByName(CONTACT_NAME);
        contactRepository.deleteContactEmailsByName(CONTACT_NAME);
        contactRepository.deleteContactByName(CONTACT_NAME);
    }
}
