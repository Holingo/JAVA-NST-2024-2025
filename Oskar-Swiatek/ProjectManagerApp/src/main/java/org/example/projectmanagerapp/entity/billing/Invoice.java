package org.example.projectmanagerapp.entity.billing;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.entity.user.CompanyUser;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice extends BillingDocument {

    private String invoiceNumber;

    @ManyToOne
    private CompanyUser companyUser;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
}
