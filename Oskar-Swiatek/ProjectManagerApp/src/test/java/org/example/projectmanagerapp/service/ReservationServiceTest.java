package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.dto.ReservationHistoryDto;
import org.example.projectmanagerapp.entity.billing.Invoice;
import org.example.projectmanagerapp.entity.billing.Receipt;
import org.example.projectmanagerapp.entity.car.Car;
import org.example.projectmanagerapp.entity.car.CarModel;
import org.example.projectmanagerapp.entity.enums.ReservationStatus;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.entity.user.CompanyUser;
import org.example.projectmanagerapp.entity.user.PrivateUser;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private InvoiceRepository invoiceRepository;
    private ReceiptRepository receiptRepository;

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        reservationRepository = mock(ReservationRepository.class);
        userRepository = mock(UserRepository.class);
        carRepository = mock(CarRepository.class);
        invoiceRepository = mock(InvoiceRepository.class);
        receiptRepository = mock(ReceiptRepository.class);

        reservationService = new ReservationService(
                reservationRepository,
                userRepository,
                carRepository,
                invoiceRepository,
                receiptRepository
        );
    }

    @Test
    void shouldCreateReservation() {
        Car car = new Car(); car.setId(1L); car.setAvailable(true);
        User user = new PrivateUser(); user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(reservationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        Reservation result = reservationService.createReservation(
                1L, 1L, LocalDateTime.now(), LocalDateTime.now().plusDays(1)
        );

        assertEquals(ReservationStatus.ACTIVE, result.getStatus());
        assertFalse(car.isAvailable());
    }

    @Test
    void shouldThrowWhenCarUnavailable() {
        Car car = new Car(); car.setAvailable(false);
        User user = new PrivateUser();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(carRepository.findById(2L)).thenReturn(Optional.of(car));

        assertThrows(RuntimeException.class, () ->
                reservationService.createReservation(1L, 2L, LocalDateTime.now(), LocalDateTime.now().plusDays(1))
        );
    }

    @Test
    void shouldReturnUserReservations() {
        List<Reservation> reservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findByUserId(1L)).thenReturn(reservations);
        assertEquals(2, reservationService.getUserReservations(1L).size());
    }

    @Test
    void shouldReturnActiveUserReservations() {
        List<Reservation> reservations = List.of(new Reservation());
        when(reservationRepository.findByUserIdAndStatus(1L, ReservationStatus.ACTIVE)).thenReturn(reservations);
        assertEquals(1, reservationService.getActiveReservationsByUser(1L).size());
    }

    @Test
    void shouldReturnAllActiveReservations() {
        when(reservationRepository.findByStatus(ReservationStatus.ACTIVE))
                .thenReturn(List.of(new Reservation()));
        assertEquals(1, reservationService.getAllActiveReservations().size());
    }

    @Test
    void shouldCancelReservation() {
        Reservation res = new Reservation();
        Car car = new Car(); car.setAvailable(false);
        res.setId(1L); res.setStatus(ReservationStatus.ACTIVE);
        res.setCar(car);
        User user = new PrivateUser(); user.setUsername("jan");
        res.setUser(user);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(res));
        when(reservationRepository.save(any())).thenReturn(res);

        Reservation result = reservationService.cancelReservation(1L, "jan");
        assertEquals(ReservationStatus.CANCELLED, result.getStatus());
        assertTrue(car.isAvailable());
    }

    @Test
    void shouldThrowIfCancelNotOwnReservation() {
        Reservation res = new Reservation();
        User user = new PrivateUser(); user.setUsername("adam");
        res.setUser(user);
        res.setStatus(ReservationStatus.ACTIVE);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(res));

        assertThrows(RuntimeException.class, () -> reservationService.cancelReservation(1L, "wronguser"));
    }

    @Test
    void shouldThrowIfCancelInactiveReservation() {
        Reservation res = new Reservation();
        User user = new PrivateUser(); user.setUsername("jan");
        res.setUser(user);
        res.setStatus(ReservationStatus.COMPLETED);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(res));

        assertThrows(RuntimeException.class, () -> reservationService.cancelReservation(1L, "jan"));
    }

    @Test
    void shouldCompleteReservationWithInvoice() {
        CompanyUser company = new CompanyUser(); company.setId(1L);
        Car car = new Car(); car.setAvailable(false); car.setPricePerDay(BigDecimal.valueOf(100));
        Reservation res = new Reservation();
        res.setUser(company);
        res.setStatus(ReservationStatus.ACTIVE);
        res.setCar(car);
        res.setStartDate(LocalDateTime.now().minusDays(2));
        res.setEndDate(LocalDateTime.now());

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(res));
        when(invoiceRepository.count()).thenReturn(0L);
        when(reservationRepository.save(any())).thenReturn(res);

        Reservation result = reservationService.completeReservation(1L);
        assertEquals(ReservationStatus.COMPLETED, result.getStatus());
        assertTrue(car.isAvailable());
    }

    @Test
    void shouldCompleteReservationWithReceipt() {
        PrivateUser user = new PrivateUser(); user.setId(1L);
        Car car = new Car(); car.setAvailable(false); car.setPricePerDay(BigDecimal.valueOf(50));
        Reservation res = new Reservation();
        res.setUser(user);
        res.setStatus(ReservationStatus.ACTIVE);
        res.setCar(car);
        res.setStartDate(LocalDateTime.now().minusDays(1));
        res.setEndDate(LocalDateTime.now());

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(res));
        when(receiptRepository.count()).thenReturn(0L);
        when(reservationRepository.save(any())).thenReturn(res);

        Reservation result = reservationService.completeReservation(1L);
        assertEquals(ReservationStatus.COMPLETED, result.getStatus());
        assertTrue(car.isAvailable());
    }

    @Test
    void shouldGetCompletedHistory() {
        CarModel carModel = new CarModel();
        carModel.setBrand("Toyota"); carModel.setModel("Yaris");

        Car car = new Car();
        car.setCarModel(carModel);
        car.setRegistrationNumber("KR12345");

        Reservation res = new Reservation();
        res.setId(1L); res.setCar(car);
        res.setStartDate(LocalDateTime.now().minusDays(3));
        res.setEndDate(LocalDateTime.now().minusDays(1));
        res.setStatus(ReservationStatus.COMPLETED);

        Receipt receipt = new Receipt(); receipt.setReceiptNumber("R123");
        res.setReceipt(receipt);

        User user = new PrivateUser(); user.setId(1L);

        when(reservationRepository.findByUserIdAndStatus(1L, ReservationStatus.COMPLETED))
                .thenReturn(List.of(res));

        List<ReservationHistoryDto> history = reservationService.getCompletedReservationHistory(user);
        assertEquals(1, history.size());
        assertEquals("RECEIPT", history.get(0).getDocumentType());
    }
}
