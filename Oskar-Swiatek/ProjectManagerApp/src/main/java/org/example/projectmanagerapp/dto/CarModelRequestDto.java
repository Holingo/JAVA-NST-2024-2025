package org.example.projectmanagerapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarModelRequestDto {
    private String brand;
    private String model;
    private String engineType;
    private int seats;
    private String bodyType;
    private int productionYear;
}
