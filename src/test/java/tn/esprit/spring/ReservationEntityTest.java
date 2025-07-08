package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Reservation;

import static org.junit.jupiter.api.Assertions.*;

class ReservationEntityTest {
    @Test
    void testReservationEntity() {
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();
        assertEquals(r1, r2); // equals par défaut ou Lombok
        assertEquals(r1.hashCode(), r2.hashCode());
    }
} 