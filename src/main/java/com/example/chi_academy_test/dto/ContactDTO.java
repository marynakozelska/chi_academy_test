package com.example.chi_academy_test.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;

@NoArgsConstructor
@Getter
@Setter
public class ContactDTO {
    private String name;
    private LinkedHashSet<String> emails;
    private LinkedHashSet<String> phones;
}
