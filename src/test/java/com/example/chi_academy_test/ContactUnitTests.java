package com.example.chi_academy_test;

import com.example.chi_academy_test.config.SecurityConfig;
import com.example.chi_academy_test.config.SpringConfig;
import com.example.chi_academy_test.dto.ContactDTO;
import com.example.chi_academy_test.model.Contact;
import com.example.chi_academy_test.model.Phone;
import com.example.chi_academy_test.model.User;
import com.example.chi_academy_test.repository.ContactRepository;
import com.example.chi_academy_test.repository.UserRepository;
import com.example.chi_academy_test.service.ContactService;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {ChiAcademyTestApplication.class, SpringConfig.class, SecurityConfig.class})
public class ContactUnitTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private ContactRepository contactRepository;
    @Mock
    private Authentication auth;

    @Autowired
    @InjectMocks
    private ContactService contactService;

    @Test
    public void test01GetContactsFor() {
        String username = "testUser";
        User user = new User(username, "pass");

        Contact contact1 = new Contact("John Doe");
        Contact contact2 = new Contact("Jane Smith");
        contact1.setUser(user);
        contact2.setUser(user);

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        when(userRepository.findByLogin(username)).thenReturn(user);
        when(contactRepository.findAllByUser(user)).thenReturn(contacts);

        List<ContactDTO> result = contactService.getContactsFor(username);

        assertEquals(contacts.size(), result.size());
    }

    @Test
    public void test02GetContactsFor() {
        String username = "testUser";
        User user = new User(username, "pass");

        Contact contact1 = new Contact("John Doe");
        Contact contact2 = new Contact("Jane Smith");
        contact1.setUser(user);
        contact1.setPhones(List.of(
                new Phone("+380501112222", contact1),
                new Phone("+380503332222", contact1)
        ));
        contact2.setUser(user);

        List<Contact> contacts = new ArrayList<>();
        contacts.add(contact1);
        contacts.add(contact2);

        when(userRepository.findByLogin(username)).thenReturn(user);
        when(contactRepository.findAllByUser(user)).thenReturn(contacts);

        List<ContactDTO> result = contactService.getContactsFor(username);

        assertEquals(contacts.size(), result.size());
    }

    @Test
    public void test03CreateContact() {
        String username = "testUser";
        User user = new User(username, "pass");

        ContactDTO contactDto = new ContactDTO();
        contactDto.setName("John Doe");

        LinkedHashSet<String> phones = new LinkedHashSet<>();
        phones.add("+380234567890");
        contactDto.setPhones(phones);

        LinkedHashSet<String> emails = new LinkedHashSet<>();
        emails.add("john@example.com");
        contactDto.setEmails(emails);

        when(auth.getName()).thenReturn(username);

        when(userRepository.findByLogin(username)).thenReturn(user);
        when(contactRepository.save(any(Contact.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContactDTO result = contactService.createContact(contactDto, auth);

        assertNotNull(result);
        assertEquals(contactDto.getName(), result.getName());
        assertEquals(contactDto.getPhones(), result.getPhones());
        assertEquals(contactDto.getEmails(), result.getEmails());

        assertEquals(contactDto.getName(), result.getName());
        assertEquals(contactDto.getPhones(), result.getPhones());
        assertEquals(contactDto.getEmails(), result.getEmails());
    }
}
