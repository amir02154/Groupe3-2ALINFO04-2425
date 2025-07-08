package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Reservation;

import static org.junit.jupiter.api.Assertions.*;

class ReservationEntityTest {
    @Test
    void testReservationEntity() {
        Reservation r1 = new Reservation();
        Reservation r2 = new Reservation();
        assertNotNull(r1);
        assertNotNull(r2);
    }
} 