package org.example.projectmanagerapp.entity.car;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private CarModel carModel;

    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @Column(nullable = false)
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    private boolean available = true;
}
