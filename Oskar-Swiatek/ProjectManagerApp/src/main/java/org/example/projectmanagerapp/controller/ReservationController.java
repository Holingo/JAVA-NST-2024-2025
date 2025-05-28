package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.service.ReservationService;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER')")
    public Reservation createReservation(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam Long carId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        Long userId = userService.getByUsername(currentUser.getUsername()).getId();
        LocalDateTime startTime = LocalDateTime.parse(start);
        LocalDateTime endTime = LocalDateTime.parse(end);

        return reservationService.createReservation(userId, carId, startTime, endTime);
    }

    @Operation(summary = "Get my reservations")
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public List<Reservation> getMyReservations(@AuthenticationPrincipal UserDetails currentUser) {
        Long userId = userService.getByUsername(currentUser.getUsername()).getId();
        return reservationService.getActiveReservationsByUser(userId);
    }

    @Operation(summary = "Get all active reservations")
    @GetMapping("/my/active")
    @PreAuthorize("hasRole('USER')")
    public List<Reservation> getMyActiveReservations(@AuthenticationPrincipal UserDetails currentUser) {
        Long userId = userService.getByUsername(currentUser.getUsername()).getId();
        return reservationService.getActiveReservationsByUser(userId);
    }

    // For ADMIN:
    @Operation(summary = "(ADMIN) All active reservations")
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Reservation> getAllActiveReservations() {
        return reservationService.getAllActiveReservations();
    }

    @Operation(summary = "(Admin) Complete reservation")
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public Reservation completeReservation(@PathVariable Long id) {
        return reservationService.completeReservation(id);
    }
}
