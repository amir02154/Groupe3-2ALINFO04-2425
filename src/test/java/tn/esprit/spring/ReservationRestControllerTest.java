package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.RestControllers.ReservationRestController;
import tn.esprit.spring.Services.Reservation.IReservationService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
public class ReservationRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(reservationService);
    }

    @Test
    void testGetAllReservations() throws Exception {
        Mockito.when(reservationService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/reservation/findAll"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddReservation() throws Exception {
        Reservation reservation = new Reservation();
        Mockito.when(reservationService.addOrUpdate(any(Reservation.class))).thenReturn(reservation);
        mockMvc.perform(post("/reservation/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteReservation() throws Exception {
        Reservation reservation = new Reservation();
        mockMvc.perform(delete("/reservation/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservationById() throws Exception {
        Reservation reservation = new Reservation();
        Mockito.when(reservationService.findById(anyString())).thenReturn(reservation);
        mockMvc.perform(get("/reservation/findById").param("id", "1"))
                .andExpect(status().isOk());
    }
} 