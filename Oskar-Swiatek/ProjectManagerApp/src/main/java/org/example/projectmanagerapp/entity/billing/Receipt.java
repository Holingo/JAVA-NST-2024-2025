package org.example.projectmanagerapp.entity.billing;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.entity.user.PrivateUser;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Receipt extends BillingDocument {

    private String receiptNumber;

    @ManyToOne
    private PrivateUser privateUser;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;
}
