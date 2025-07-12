package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantEntityTest {

    @Test
    void testEtudiantBuilder() {
        // Test avec Builder
        Etudiant etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Doe")
                .prenomEt("John")
                .cin(12345678L)
                .ecole("ESPRIT")
                .dateNaissance(LocalDate.of(1995, 1, 1))
                .build();

        assertEquals(1L, etudiant.getIdEtudiant());
        assertEquals("Doe", etudiant.getNomEt());
        assertEquals("John", etudiant.getPrenomEt());
        assertEquals(12345678L, etudiant.getCin());
        assertEquals("ESPRIT", etudiant.getEcole());
        assertEquals(LocalDate.of(1995, 1, 1), etudiant.getDateNaissance());
    }

    @Test
    void testEtudiantNoArgsConstructor() {
        // Test constructeur par défaut
        Etudiant etudiant = new Etudiant();
        assertNotNull(etudiant);
    }

    @Test
    void testEtudiantAllArgsConstructor() {
        // Test constructeur avec tous les paramètres
        List<Reservation> reservations = new ArrayList<>();
        
        Etudiant etudiant = new Etudiant(1L, "Doe", "John", 12345678L, "ESPRIT", LocalDate.of(1995, 1, 1), reservations);
        
        assertEquals(1L, etudiant.getIdEtudiant());
        assertEquals("Doe", etudiant.getNomEt());
        assertEquals("John", etudiant.getPrenomEt());
        assertEquals(12345678L, etudiant.getCin());
        assertEquals("ESPRIT", etudiant.getEcole());
        assertEquals(LocalDate.of(1995, 1, 1), etudiant.getDateNaissance());
        assertEquals(reservations, etudiant.getReservations());
    }

    @Test
    void testEtudiantSettersAndGetters() {
        Etudiant etudiant = new Etudiant();
        
        // Test setters
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Doe");
        etudiant.setPrenomEt("John");
        etudiant.setCin(12345678L);
        etudiant.setEcole("ESPRIT");
        etudiant.setDateNaissance(LocalDate.of(1995, 1, 1));
        
        List<Reservation> reservations = new ArrayList<>();
        etudiant.setReservations(reservations);
        
        // Test getters
        assertEquals(1L, etudiant.getIdEtudiant());
        assertEquals("Doe", etudiant.getNomEt());
        assertEquals("John", etudiant.getPrenomEt());
        assertEquals(12345678L, etudiant.getCin());
        assertEquals("ESPRIT", etudiant.getEcole());
        assertEquals(LocalDate.of(1995, 1, 1), etudiant.getDateNaissance());
        assertEquals(reservations, etudiant.getReservations());
    }

    @Test
    void testEtudiantToString() {
        Etudiant etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Doe")
                .prenomEt("John")
                .cin(12345678L)
                .build();
        
        String toString = etudiant.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Doe"));
        assertTrue(toString.contains("John"));
    }

    @Test
    void testEtudiantEqualsAndHashCode() {
        Etudiant etudiant1 = Etudiant.builder().idEtudiant(1L).cin(12345678L).build();
        Etudiant etudiant2 = Etudiant.builder().idEtudiant(1L).cin(12345678L).build();
        Etudiant etudiant3 = Etudiant.builder().idEtudiant(2L).cin(87654321L).build();
        
        // Test equals
        assertEquals(etudiant1, etudiant2);
        assertNotEquals(etudiant1, etudiant3);
        assertNotEquals(etudiant1, null);
        assertEquals(etudiant1, etudiant1);
        
        // Test hashCode
        assertEquals(etudiant1.hashCode(), etudiant2.hashCode());
        assertNotEquals(etudiant1.hashCode(), etudiant3.hashCode());
    }

    @Test
    void testEtudiantWithReservations() {
        Etudiant etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Doe")
                .prenomEt("John")
                .build();

        List<Reservation> reservations = new ArrayList<>();
        Reservation reservation1 = Reservation.builder().idReservation("RES001").build();
        Reservation reservation2 = Reservation.builder().idReservation("RES002").build();
        reservations.add(reservation1);
        reservations.add(reservation2);

        etudiant.setReservations(reservations);

        assertEquals(2, etudiant.getReservations().size());
        assertTrue(etudiant.getReservations().contains(reservation1));
        assertTrue(etudiant.getReservations().contains(reservation2));
    }
} 