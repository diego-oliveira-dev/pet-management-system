package com.projetos.diego.pet_management_system.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private PetOwner petOwner;

    public enum Type {
        DOG,
        CAT
    }

    public enum Sex {
        MALE,
        FEMALE
    }
}
