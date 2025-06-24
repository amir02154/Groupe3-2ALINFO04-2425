package tn.esprit.spring;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class EtudiantReservationIntegrationTest {

    @Autowired
    EtudiantRepository etudiantRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
        etudiantRepository.deleteAll();
    }

    @Test
    @Transactional

    void testAffecterEtDesaffecterReservation() {
        // Créer un étudiant
        Etudiant etu = Etudiant.builder()
                .nomEt("Slim")
                .prenomEt("Ali")
                .cin(99887766L)
                .ecole("ESPRIT")
                .reservations(new ArrayList<>())
                .build();
        etu = etudiantRepository.save(etu);

        // Créer une réservation avec une date valide
        Reservation res = Reservation.builder()
                .idReservation("res123")
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))  // ✅ corrigé ici
                .estValide(true)
                .build();
        res = reservationRepository.save(res);

        // Affecter
        Etudiant etBefore = etudiantRepository.getByNomEtAndPrenomEt("Slim", "Ali");
        assertThat(etBefore.getReservations()).isEmpty();

        etBefore.getReservations().add(res);
        etudiantRepository.save(etBefore);

        Etudiant etAfterAffect = etudiantRepository.getByNomEtAndPrenomEt("Slim", "Ali");
        assertThat(etAfterAffect.getReservations()).hasSize(1);

        // Désaffecter
        etAfterAffect.getReservations().remove(res);
        etudiantRepository.save(etAfterAffect);

        Etudiant etAfterDesaffect = etudiantRepository.getByNomEtAndPrenomEt("Slim", "Ali");
        assertThat(etAfterDesaffect.getReservations()).isEmpty();
    }
}
