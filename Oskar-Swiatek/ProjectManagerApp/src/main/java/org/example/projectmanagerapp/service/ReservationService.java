package org.example.projectmanagerapp.service;

import lombok.RequiredArgsConstructor;
import org.example.projectmanagerapp.dto.ReservationHistoryDto;
import org.example.projectmanagerapp.entity.billing.Invoice;
import org.example.projectmanagerapp.entity.billing.Receipt;
import org.example.projectmanagerapp.entity.car.Car;
import org.example.projectmanagerapp.entity.enums.ReservationStatus;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.entity.user.CompanyUser;
import org.example.projectmanagerapp.entity.user.PrivateUser;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final InvoiceRepository invoiceRepository;
    private final ReceiptRepository receiptRepository;

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

    public Reservation cancelReservation(Long reservationId, String username) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!reservation.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only cancel your own reservation.");
        }

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new RuntimeException("Only active reservations can be cancelled.");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        // Back car to available
        Car car = reservation.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        return reservationRepository.save(reservation);
    }

    public Reservation completeReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setStatus(ReservationStatus.COMPLETED);

        Car car = reservation.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        // If the user is Company -> Invoice
        if (reservation.getUser() instanceof CompanyUser companyUser) {
            Invoice invoice = new Invoice();
            invoice.setIssuedDate(LocalDate.now());
            invoice.setTotalAmount(calculateTotal(reservation));
            invoice.setInvoiceNumber(generateInvoiceNumber());
            invoice.setCompanyUser(companyUser);
            invoice.setReservation(reservation);
            invoiceRepository.save(invoice);
        } else if (reservation.getUser() instanceof PrivateUser privateUser) {
            Receipt receipt = new Receipt();
            receipt.setIssuedDate(LocalDate.now());
            receipt.setTotalAmount(calculateTotal(reservation));
            receipt.setReceiptNumber(generateReceiptNumber());
            receipt.setPrivateUser(privateUser);
            receipt.setReservation(reservation);
            receiptRepository.save(receipt);
        }

        return reservationRepository.save(reservation);
    }

    public List<ReservationHistoryDto> getCompletedReservationHistory(User user) {
        List<Reservation> reservations = reservationRepository.findByUserIdAndStatus(
                user.getId(), ReservationStatus.COMPLETED
        );

        return reservations.stream().map(reservation -> {
            ReservationHistoryDto dto = new ReservationHistoryDto();
            dto.setReservationId(reservation.getId());
            dto.setCarModel(reservation.getCar().getCarModel().getBrand() + " " + reservation.getCar().getCarModel().getModel());
            dto.setRegistrationNumber(reservation.getCar().getRegistrationNumber());
            dto.setStartDate(reservation.getStartDate());
            dto.setEndDate(reservation.getEndDate());

            if (reservation.getInvoice() != null) {
                dto.setDocumentNumber(reservation.getInvoice().getInvoiceNumber());
                dto.setDocumentType("INVOICE");
            } else if (reservation.getReceipt() != null) {
                dto.setDocumentNumber(reservation.getReceipt().getReceiptNumber());
                dto.setDocumentType("RECEIPT");
            }

            return dto;
        }).toList();
    }

    private BigDecimal calculateTotal(Reservation reservation) {
        long days = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        return reservation.getCar().getPricePerDay().multiply(BigDecimal.valueOf(days));
    }

    private String generateInvoiceNumber() {
        long count = invoiceRepository.count() + 1;
        return "FV/" + LocalDate.now().getYear() + "/" + String.format("%05d", count);
    }

    private String generateReceiptNumber() {
        long count = receiptRepository.count() + 1;
        return "R/" + LocalDate.now().getYear() + "/" + String.format("%05d", count);
    }
}
