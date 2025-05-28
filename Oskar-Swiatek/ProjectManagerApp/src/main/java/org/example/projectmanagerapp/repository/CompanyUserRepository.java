package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.user.CompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyUserRepository extends JpaRepository<CompanyUser, Long> {
}
