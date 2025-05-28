package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.user.PrivateUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrivateUserRepository extends JpaRepository<PrivateUser, Long> {
}
