package org.example.projectmanagerapp.entity.invoice;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.entity.user.CompanyUser;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private LocalDate issuedDate;

    private BigDecimal totalAmount;

    private String invoiceNumber; // example: FV/2025/0001

    @ManyToOne
    @JoinColumn(name = "company_user_id")
    private CompanyUser companyUser;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;
}
