package com.example.chi_academy_test.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column
    @Size(max = 50)
    private String name;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    private List<Phone> phones;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL)
    private List<Email> emails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Contact(String name) {
        this.name = name;
    }
}
