package org.example.projectmanagerapp.entity.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.common.Address;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PrivateUser extends User {
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String pesel;

    @Column(nullable = false)
    private String idCardNumber;

    @Column(nullable = false)
    private String drivingLicenseNumber;

    @Column(nullable = false)
    private boolean verified;

    @Embedded
    private Address address;
}
