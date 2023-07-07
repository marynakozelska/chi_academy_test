package com.example.chi_academy_test.service;

import com.example.chi_academy_test.dto.ContactDTO;
import com.example.chi_academy_test.exceptions.ContactNotFoundException;
import com.example.chi_academy_test.model.Contact;
import com.example.chi_academy_test.model.Email;
import com.example.chi_academy_test.model.Phone;
import com.example.chi_academy_test.model.User;
import com.example.chi_academy_test.repository.ContactRepository;
import com.example.chi_academy_test.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ContactService {
    private final ContactRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public ContactService(ContactRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves the {@link Contact contacts} owned by the given user.
     *
     * @param username the user whose contacts are being sought.
     * @return the list of contacts owned by the given user.
     */
    public List<ContactDTO> getContactsFor(String username) {
        User user = userRepository.findByLogin(username);
        List<Contact> contacts = repository.findAllByUser(user);

        List<ContactDTO> contactsDto = new ArrayList<>();
        for (Contact c : contacts)
            contactsDto.add(convertToDto(c));

        return contactsDto;
    }

    public ContactDTO createContact(ContactDTO contactDto, Authentication authentication) {
        User user = userRepository.findByLogin(authentication.getName());

        checkDuplicates(contactDto, user);

        Contact createdContact = convertToEntity(contactDto);
        createdContact.setUser(user);
        createdContact = repository.save(createdContact);

        return convertToDto(createdContact);
    }

    public ContactDTO updateContactById(Long id, ContactDTO contactDto, Authentication authentication) {
        User authorizedUser = userRepository.findByLogin(authentication.getName());

        if (!repository.existsContactByUserAndId(authorizedUser, id)) {
            throw new ContactNotFoundException("Contact with this id doesn't exist.");
        }

        Contact contact = repository.findById(id).get();

        if (!Objects.equals(contactDto.getName(), contact.getName())) {
            checkIfNameExists(contactDto.getName(), authorizedUser);
        }

        boolean hasDuplicateNumbers = contactDto.getPhones()
                .stream()
                .anyMatch(phone -> repository.existsByPhonesNumberAndUserIdAndIdNot(phone, authorizedUser.getId(), contact.getId()));

        if (hasDuplicateNumbers) {
            throw new IllegalArgumentException("Cannot create contact. Contacts with the same phone number already exists");
        }

        boolean hasDuplicateEmails = contactDto.getEmails()
                .stream()
                .anyMatch(email -> repository.existsByEmailsAddressAndUserIdAndIdNot(email, authorizedUser.getId(), contact.getId()));

        if (hasDuplicateEmails) {
            throw new IllegalArgumentException("Cannot create contact. Contacts with the same email address already exists");
        }

        Contact resultContact = convertToEntity(contactDto);
        repository.save(resultContact);

        return convertToDto(resultContact);
    }

    public boolean deleteContactById(Long id, Authentication authentication) {
        User authorizedUser = userRepository.findByLogin(authentication.getName());

        if (!repository.existsContactByUserAndId(authorizedUser, id)) {
            throw new ContactNotFoundException("Contact with this id doesn't exist.");
        }

        repository.deleteById(id);
        return true;
    }

    private void checkIfNameExists(String name, User user) {
        if (repository.existsContactByNameAndUserId(name, user.getId())) {
            throw new IllegalArgumentException("Cannot create contact. Contact with the same name already exists");
        }

    }

    private void checkDuplicates(ContactDTO contactDto, User user) {
        checkIfNameExists(contactDto.getName(), user);

        boolean hasDuplicateNumbers = contactDto.getPhones()
                .stream()
                .anyMatch(phone -> repository.existsByPhonesNumberAndUserId(phone, user.getId()));

        if (hasDuplicateNumbers) {
            throw new IllegalArgumentException("Cannot create contact. Contacts with the same phone number already exists");
        }

        boolean hasDuplicateEmails = contactDto.getEmails()
                .stream()
                .anyMatch(email -> repository.existsByEmailsAddressAndUserId(email, user.getId()));

        if (hasDuplicateEmails) {
            throw new IllegalArgumentException("Cannot create contact. Contacts with the same email address already exists");
        }
    }


//    CONVERTERS:

    private ContactDTO convertToDto(Contact contact) {
        ContactDTO contactDto = new ContactDTO();
        contactDto.setName(contact.getName());

        if (contact.getEmails() != null) {
            LinkedHashSet<String> contactEmails = new LinkedHashSet<>(contact.getEmails().size());
            for (Email e : contact.getEmails()) {
                contactEmails.add(e.getAddress());
            }
            contactDto.setEmails(contactEmails);
        }

        if (contact.getPhones() != null) {
            LinkedHashSet<String> contactPhones = new LinkedHashSet<>(contact.getPhones().size());
            for (Phone e : contact.getPhones()) {
                contactPhones.add(e.getNumber());
            }
            contactDto.setPhones(contactPhones);
        }

        return contactDto;
    }

    private Contact convertToEntity(ContactDTO contactDTO) {
        Contact contact = new Contact(contactDTO.getName());

        List<Email> contactEmails = new LinkedList<>();
        LinkedHashSet<String> emails = contactDTO.getEmails();
        for (String email : emails) {
            contactEmails.add(new Email(email, contact));
        }
        contact.setEmails(contactEmails);

        List<Phone> contactPhones = new LinkedList<>();
        LinkedHashSet<String> phones = contactDTO.getPhones();
        for (String phone : phones) {
            contactPhones.add(new Phone(phone, contact));
        }
        contact.setPhones(contactPhones);

        return contact;
    }
}
