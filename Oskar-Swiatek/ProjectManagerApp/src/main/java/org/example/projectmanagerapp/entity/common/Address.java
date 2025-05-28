package org.example.projectmanagerapp.entity.common;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class Address {
    private String houseNumber;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
}
