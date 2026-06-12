package com.projetos.diego.pet_management_system.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Sex sex;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "weight_kg", nullable = false, precision = 5)
    private Double weight;

    private String breed;

    private String address;

    @Column(nullable = false)
    private String owner;

    public enum Type {
        DOG,
        CAT
    }

    public enum Sex {
        MALE,
        FEMALE
    }
}
