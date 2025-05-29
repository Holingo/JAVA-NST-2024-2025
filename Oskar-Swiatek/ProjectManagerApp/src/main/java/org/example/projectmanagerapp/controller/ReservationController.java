package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.dto.ReservationHistoryDto;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.service.ReservationService;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Reservations", description = "Operations related to reservations")
@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, UserService userService) {
        this.reservationService = reservationService;
        this.userService = userService;
    }

    // For USER:
    @Operation(summary = "Create reservation")
    @PostMapping
    public Reservation createReservation(
            Authentication authentication,
            @RequestParam Long carId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        String username = authentication.getName();
        Long userId = userService.getByUsername(username).getId();
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        return reservationService.createReservation(userId, carId, startTime, endTime);
    }

    @Operation(summary = "Get my reservations")
    @GetMapping("/my")
    public List<Reservation> getMyReservations(Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.getByUsername(username).getId();
        return reservationService.getActiveReservationsByUser(userId);
    }


    @Operation(summary = "Get all active reservations")
    @GetMapping("/my/active")
    public List<Reservation> getMyActiveReservations(Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.getByUsername(username).getId();
        return reservationService.getActiveReservationsByUser(userId);
    }


    @Operation(summary = "My reservation history")
    @GetMapping("/my/history")
    public ResponseEntity<List<ReservationHistoryDto>> getReservationHistory(Authentication auth) {
        String username = auth.getName();
        User user = userService.getByUsername(username);
        return ResponseEntity.ok(reservationService.getCompletedReservationHistory(user));
    }

    @Operation(summary = "Cancel my reservation")
    @PatchMapping("/reservations/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id, Authentication authentication) {
        String username = authentication.getName();
        Reservation cancelled = reservationService.cancelReservation(id, username);
        return ResponseEntity.ok(cancelled);
    }

    // For ADMIN:
    @Operation(summary = "[ADMIN] All active reservations")
    @GetMapping("/admin/active")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reservation> getAllActiveReservations() {
        return reservationService.getAllActiveReservations();
    }

    @Operation(summary = "[ADMIN] Complete reservation")
    @PostMapping("/admin/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public Reservation completeReservation(@PathVariable Long id) {
        return reservationService.completeReservation(id);
    }
}
