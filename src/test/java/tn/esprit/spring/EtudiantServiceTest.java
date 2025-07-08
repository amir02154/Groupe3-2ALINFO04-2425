package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Etudiant e = new Etudiant();
        when(etudiantRepository.save(e)).thenReturn(e);

        Etudiant result = etudiantService.addOrUpdate(e);
        assertEquals(e, result);
    }

    @Test
    void testFindAll() {
        List<Etudiant> etudiants = List.of(new Etudiant(), new Etudiant());
        when(etudiantRepository.findAll()).thenReturn(etudiants);

        List<Etudiant> result = etudiantService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        Etudiant e = new Etudiant();
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(e));

        Etudiant result = etudiantService.findById(1L);
        assertEquals(e, result);
    }

    @Test
    void testFindById_NotFound() {
        when(etudiantRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> etudiantService.findById(99L));
        assertTrue(ex.getMessage().contains("Étudiant non trouvé"));
    }

    @Test
    void testDeleteById() {
        etudiantService.deleteById(1L);
        verify(etudiantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDelete() {
        Etudiant e = new Etudiant();
        etudiantService.delete(e);
        verify(etudiantRepository).delete(e);
    }

    @Test
    void testSelectJPQL() {
        when(etudiantRepository.selectJPQL("Ali")).thenReturn(List.of(new Etudiant(), new Etudiant()));
        List<Etudiant> result = etudiantService.selectJPQL("Ali");

        assertEquals(2, result.size());
    }

    @Test
    void testAffecterReservationAEtudiant() {
        Reservation res = new Reservation();
        Etudiant et = new Etudiant();
        et.setReservations(new ArrayList<>());

        when(reservationRepository.findById("res1")).thenReturn(Optional.of(res));
        when(etudiantRepository.getByNomEtAndPrenomEt("Ali", "Ben Salah")).thenReturn(et);

        etudiantService.affecterReservationAEtudiant("res1", "Ali", "Ben Salah");

        assertTrue(et.getReservations().contains(res));
        verify(etudiantRepository).save(et);
    }

    @Test
    void testDesaffecterReservationAEtudiant() {
        Reservation res = new Reservation();
        Etudiant et = new Etudiant();
        et.setReservations(new ArrayList<>(List.of(res)));

        when(reservationRepository.findById("res1")).thenReturn(Optional.of(res));
        when(etudiantRepository.getByNomEtAndPrenomEt("Ali", "Ben Salah")).thenReturn(et);

        etudiantService.desaffecterReservationAEtudiant("res1", "Ali", "Ben Salah");

        assertFalse(et.getReservations().contains(res));
        verify(etudiantRepository).save(et);
    }

    @Test
    void testDeleteById_NotFound() {
        doThrow(new RuntimeException("Etudiant not found")).when(etudiantRepository).deleteById(99L);
        assertThrows(RuntimeException.class, () -> etudiantService.deleteById(99L));
    }
}
