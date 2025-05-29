package org.example.projectmanagerapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CarRequestDto {
    private Long modelId;
    private String registrationNumber;
    private BigDecimal pricePerDay;
    private boolean available = true;
}
