package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.entity.enums.ReservationStatus;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUserId(Long userId);

    List<Reservation> findByUserIdAndStatus(Long userId, ReservationStatus status);

    List<Reservation> findByStatus(ReservationStatus status);
}
