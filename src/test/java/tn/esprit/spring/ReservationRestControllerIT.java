package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReservationRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
    }

    @Test
    void testAddOrUpdateAndFindById() throws Exception {
        Reservation res = Reservation.builder()
                .idReservation("res123")
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .build();

        // Ajouter la réservation
        mockMvc.perform(post("/reservation/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(res)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("res123"));

        // Rechercher par id
        mockMvc.perform(get("/reservation/findById")
                        .param("id", "res123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value("res123"));
    }

    @Test
    void testFindAll() throws Exception {
        Reservation res1 = Reservation.builder()
                .idReservation("res1")
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .build();
        Reservation res2 = Reservation.builder()
                .idReservation("res2")
                .anneeUniversitaire(LocalDate.of(2023, 9, 1))
                .estValide(true)
                .build();

        reservationRepository.save(res1);
        reservationRepository.save(res2);

        mockMvc.perform(get("/reservation/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }

    @Test
    void testDeleteById() throws Exception {
        Reservation res = Reservation.builder()
                .idReservation("resDel")
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .build();

        reservationRepository.save(res);

        mockMvc.perform(delete("/reservation/deleteById/{id}", "resDel"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reservation/findById")
                        .param("id", "resDel"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteByEntity() throws Exception {
        Reservation res = Reservation.builder()
                .idReservation("resDelEntity")
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .build();

        reservationRepository.save(res);

        mockMvc.perform(delete("/reservation/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(res)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reservation/findById")
                        .param("id", "resDelEntity"))
                .andExpect(status().isNotFound());
    }

    // Pour les méthodes nécessitant une base ou logique métier plus complète,
    // tu peux mocker ou ajouter plus tard.
    // Exemples d’appel sans validation des retours :

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() throws Exception {
        mockMvc.perform(post("/reservation/ajouterReservationEtAssignerAChambreEtAEtudiant")
                        .param("numChambre", "1")
                        .param("cin", "12345678"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservationParAnneeUniversitaire() throws Exception {
        mockMvc.perform(get("/reservation/getReservationParAnneeUniversitaire")
                        .param("debutAnnee", "2023-09-01")
                        .param("finAnnee", "2024-08-31"))
                .andExpect(status().isOk());
    }

    @Test
    void testAnnulerReservation() throws Exception {
        mockMvc.perform(delete("/reservation/annulerReservation")
                        .param("cinEtudiant", "12345678"))
                .andExpect(status().isOk());
    }
}
