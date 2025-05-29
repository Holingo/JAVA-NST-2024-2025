package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.dto.ReservationHistoryDto;
import org.example.projectmanagerapp.entity.enums.UserRole;
import org.example.projectmanagerapp.entity.reservation.Reservation;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.service.ReservationService;
import org.example.projectmanagerapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReservationControllerTest {

    private MockMvc mockMvc;
    private ReservationService reservationService;
    private UserService userService;
    private ObjectMapper objectMapper;

    private User sampleUser;
    private Reservation sampleReservation;
    private Authentication auth;

    @BeforeEach
    void setUp() {
        reservationService = mock(ReservationService.class);
        userService = mock(UserService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ReservationController(reservationService, userService)).build();
        objectMapper = new ObjectMapper();

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setUsername("jan");
        sampleUser.setPassword("pass");
        sampleUser.setRole(UserRole.USER);

        sampleReservation = new Reservation();
        sampleReservation.setId(1L);

        auth = new TestingAuthenticationToken("jan", null);
    }

    @Test
    void shouldCreateReservation() throws Exception {
        when(userService.getByUsername("jan")).thenReturn(sampleUser);
        when(reservationService.createReservation(anyLong(), anyLong(), any(), any()))
                .thenReturn(sampleReservation);

        mockMvc.perform(post("/reservations")
                        .param("carId", "2")
                        .param("start", "2025-06-01T10:00:00")
                        .param("end", "2025-06-02T10:00:00")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldGetMyReservations() throws Exception {
        when(userService.getByUsername("jan")).thenReturn(sampleUser);
        when(reservationService.getActiveReservationsByUser(1L)).thenReturn(List.of(sampleReservation));

        mockMvc.perform(get("/reservations/my")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldGetMyActiveReservations() throws Exception {
        when(userService.getByUsername("jan")).thenReturn(sampleUser);
        when(reservationService.getActiveReservationsByUser(1L)).thenReturn(List.of(sampleReservation));

        mockMvc.perform(get("/reservations/my/active")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldGetMyReservationHistory() throws Exception {
        ReservationHistoryDto dto = new ReservationHistoryDto();
        dto.setReservationId(1L);

        when(userService.getByUsername("jan")).thenReturn(sampleUser);
        when(reservationService.getCompletedReservationHistory(sampleUser)).thenReturn(List.of(dto));

        mockMvc.perform(get("/reservations/my/history")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reservationId").value(1L));
    }

    @Test
    void shouldCancelReservation() throws Exception {
        when(reservationService.cancelReservation(eq(1L), eq("jan"))).thenReturn(sampleReservation);

        mockMvc.perform(patch("/reservations/reservations/1/cancel")
                        .principal(auth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void shouldGetAllActiveReservationsAsAdmin() throws Exception {
        when(reservationService.getAllActiveReservations()).thenReturn(List.of(sampleReservation));

        mockMvc.perform(get("/reservations/admin/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void shouldCompleteReservationAsAdmin() throws Exception {
        when(reservationService.completeReservation(1L)).thenReturn(sampleReservation);

        mockMvc.perform(post("/reservations/admin/1/complete"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}
