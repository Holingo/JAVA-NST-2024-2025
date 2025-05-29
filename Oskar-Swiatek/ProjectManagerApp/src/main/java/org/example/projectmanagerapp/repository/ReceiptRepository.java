package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.billing.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
