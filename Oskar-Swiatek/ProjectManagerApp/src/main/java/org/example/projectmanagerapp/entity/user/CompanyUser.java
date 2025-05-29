package org.example.projectmanagerapp.entity.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.common.Address;
import org.example.projectmanagerapp.entity.billing.Invoice;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CompanyUser extends User {
    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String taxId; // NIP

    @Column(unique = true)
    private String regon;

    @Column(unique = true)
    private String krs;

    @Column(nullable = false)
    private String contactPerson;

    @Column(nullable = false)
    private String contactEmail;

    @Column(nullable = false)
    private String contactPhone;

    @Embedded
    private Address companyAddress;

    @OneToMany(mappedBy = "companyUser")
    private List<Invoice> invoices; // {PL: Faktury}
}
