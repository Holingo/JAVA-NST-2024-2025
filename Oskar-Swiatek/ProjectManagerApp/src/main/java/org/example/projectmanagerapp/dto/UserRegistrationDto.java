package org.example.projectmanagerapp.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.projectmanagerapp.entity.common.Address;
import org.example.projectmanagerapp.entity.enums.UserType;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegistrationDto {
    private UserType userType;
    private String username;
    private String password;

    // PrivateUser fields
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String pesel;
    private String idCardNumber;
    private String drivingLicenseNumber;
    private boolean verified;

    // CompanyUser fields
    private String companyName;
    private String taxId;
    private String regon;
    private String krs;
    private String contactPerson;
    private String contactEmail;
    private String contactPhone;

    private Address address;


}
