package org.example.projectmanagerapp.entity.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private boolean available = true;
}
