package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.car.Car;
import org.example.projectmanagerapp.entity.enums.ReservationStatus;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.CarRepository;
import org.example.projectmanagerapp.repository.ReservationRepository;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, CarRepository carRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
    }

    public Reservation createReservation(Long userId, Long carId, LocalDateTime startDate, LocalDateTime endDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car not found"));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setCar(car);
        reservation.setStartDate(startDate);
        reservation.setEndDate(endDate);
        reservation.setStatus(ReservationStatus.ACTIVE);

        // set vehicle not available.
        car.setAvailable(false);
        carRepository.save(car);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getUserReservations(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> getActiveReservationsByUser(Long userId) {
        return reservationRepository.findByUserIdAndStatus(userId, ReservationStatus.ACTIVE);
    }

    public List<Reservation> getAllActiveReservations() {
        return reservationRepository.findByStatus(ReservationStatus.ACTIVE);
    }

    public Reservation completeReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(ReservationStatus.COMPLETED);

        Car car = reservation.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        return reservationRepository.save(reservation);
    }
}
