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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationRestController.class)
class ReservationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation reservation;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .idReservation("RES001")
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .build();
    }

    @Test
    void testAddOrUpdate() throws Exception {
        Mockito.when(reservationService.addOrUpdate(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/reservation/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("RES001"));
    }

    @Test
    void testFindAll() throws Exception {
        Mockito.when(reservationService.findAll()).thenReturn(Arrays.asList(reservation));

        mockMvc.perform(get("/reservation/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idReservation").value("RES001"));
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(reservationService.findById("RES001")).thenReturn(reservation);

        mockMvc.perform(get("/reservation/findById").param("id", "RES001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("RES001"));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        Mockito.when(reservationService.findById("NOTFOUND")).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/reservation/findById").param("id", "NOTFOUND"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/reservation/deleteById/RES001"))
                .andExpect(status().isOk());

        Mockito.verify(reservationService).deleteById("RES001");
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/reservation/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());

        Mockito.verify(reservationService).delete(any(Reservation.class));
    }

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() throws Exception {
        Mockito.when(reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(anyLong(), anyLong()))
                .thenReturn(reservation);

        mockMvc.perform(post("/reservation/ajouterReservationEtAssignerAChambreEtAEtudiant")
                        .param("numChambre", "101")
                        .param("cin", "12345678"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("RES001"));
    }

    @Test
    void testGetReservationParAnneeUniversitaire() throws Exception {
        LocalDate debutAnnee = LocalDate.of(2024, 1, 1);
        LocalDate finAnnee = LocalDate.of(2024, 12, 31);
        long expectedCount = 5L;

        Mockito.when(reservationService.getReservationParAnneeUniversitaire(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(expectedCount);

        mockMvc.perform(get("/reservation/getReservationParAnneeUniversitaire")
                        .param("debutAnnee", "2024-01-01")
                        .param("finAnnee", "2024-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testAnnulerReservation() throws Exception {
        long cinEtudiant = 12345678L;
        String expectedMessage = "Réservation annulée pour l'étudiant avec CIN: " + cinEtudiant;

        Mockito.when(reservationService.annulerReservation(anyLong())).thenReturn(expectedMessage);

        mockMvc.perform(delete("/reservation/annulerReservation")
                        .param("cinEtudiant", "12345678"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedMessage));
    }
} 