package com.example.chi_academy_test.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Pattern(regexp = "[+]{1}380[0-9]{9}")
    private String number;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

    public Phone(String number) {
        this.number = number;
    }

    public Phone(String number, Contact contact) {
        this.number = number;
        this.contact = contact;
    }
}
