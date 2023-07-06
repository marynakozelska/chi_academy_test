package com.example.chi_academy_test.repository;

import com.example.chi_academy_test.model.Contact;
import com.example.chi_academy_test.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends CrudRepository<Contact, Long> {
    @Override
    List<Contact> findAll();

    List<Contact> findAllByUser(User user);

    boolean existsContactByUserAndId(User user, Long id);

    boolean existsContactByNameAndUserId(String name, Long userId);

    boolean existsByPhonesNumberAndUserId(String phoneNumber, Long userId);

    boolean existsByPhonesNumberAndUserIdAndIdNot(String phoneNumber, Long userId, Long contactId);

    boolean existsByEmailsAddressAndUserId(String address, Long userId);

    boolean existsByEmailsAddressAndUserIdAndIdNot(String address, Long userId, Long contactId);

    void deleteContactByName(String name);

    void deleteContactPhonesByName(String name);

    void deleteContactEmailsByName(String name);

}
