package org.example.projectmanagerapp.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.repository.ReservationRepository;
import org.example.projectmanagerapp.service.ReservationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationAutoCompleter {
    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;

    @Scheduled(fixedRate = 60000) // 60 sec.
    public void completeExpiredReservations() {
        List<Reservation> activeReservations = reservationService.getAllActiveReservations();

        for (Reservation reservation : activeReservations) {
            if (reservation.getEndDate().isBefore(LocalDateTime.now())) {
                try {
                    reservationService.completeReservation(reservation.getId());
                    log.info("Reservation {} auto-completed", reservation.getId());
                } catch (Exception e) {
                    log.error("Failed to complete reservation " + reservation.getId(), e);
                }
            }
        }
    }
}
