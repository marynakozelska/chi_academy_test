package com.example.chi_academy_test.controller;

import com.example.chi_academy_test.dto.ContactDTO;
import com.example.chi_academy_test.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/contacts")
public class ContactController {
    private ContactService service;

    @Autowired
    public ContactController(ContactService service) {
        this.service = service;
    }

    @GetMapping
    public List<ContactDTO> list(@AuthenticationPrincipal String principal) {
        return service.getContactsFor(principal);
    }

    @PostMapping
    public ResponseEntity<ContactDTO> create(@Validated @RequestBody ContactDTO contactDto,
                                             Authentication authentication) {
        return ResponseEntity.ok(service.createContact(contactDto, authentication));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ContactDTO> update(@PathVariable("id") Long id,
                                             @Validated @RequestBody ContactDTO contactDto,
                                             Authentication authentication) {
        return ResponseEntity.ok(service.updateContactById(id, contactDto, authentication));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id,
                                          Authentication authentication) {
        return ResponseEntity.ok(service.deleteContactById(id, authentication));
    }

}
