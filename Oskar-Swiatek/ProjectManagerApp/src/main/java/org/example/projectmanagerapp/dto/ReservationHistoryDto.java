package org.example.projectmanagerapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReservationHistoryDto {
    private Long reservationId;
    private String carModel;
    private String registrationNumber;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String documentNumber; // invoiceNumber lub receiptNumber
    private String documentType;
}
