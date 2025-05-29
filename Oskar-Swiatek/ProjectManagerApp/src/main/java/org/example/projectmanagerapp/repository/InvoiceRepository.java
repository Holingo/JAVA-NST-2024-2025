package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.billing.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}
