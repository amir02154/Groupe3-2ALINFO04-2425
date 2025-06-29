package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void testSaveReservation() {
        Reservation r = new Reservation();
        r.setAnneeUniversitaire(LocalDate.parse("2024/2025"));
        Reservation saved = reservationRepository.save(r);
        assertThat(saved.getIdReservation()).isNotNull();
    }
}