package tn.esprit.spring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.*;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() {
        // --- Arrange ---
        Long numChambre = 101L;
        long cin = 12345678L;

        Bloc bloc = Bloc.builder().nomBloc("A").build();

        Chambre chambre = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(numChambre)
                .typeC(TypeChambre.SIMPLE)
                .bloc(bloc)
                .reservations(new ArrayList<>())
                .build();

        Etudiant etudiant = Etudiant.builder()
                .cin(cin)
                .build();

        List<Etudiant> etudiants = new ArrayList<>();
        etudiants.add(etudiant);

        TypedQuery<Etudiant> mockQuery = mock(TypedQuery.class);

        // Lenient stubbing pour éviter UnnecessaryStubbingException
        lenient().when(entityManager.createQuery(anyString(), eq(Etudiant.class))).thenReturn(mockQuery);
        lenient().when(mockQuery.setParameter(eq("cin"), eq(cin))).thenReturn(mockQuery);
        lenient().when(mockQuery.getResultList()).thenReturn(etudiants);

        when(chambreRepository.findByNumeroChambre(numChambre)).thenReturn(chambre);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                eq(1L), any(LocalDate.class), any(LocalDate.class)
        )).thenReturn(0);

        // Simule la sauvegarde de la réservation
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> {
            Reservation res = invocation.getArgument(0);
            if (res.getEtudiants() == null) res.setEtudiants(new ArrayList<>());
            return res;
        });

        // --- Act ---
        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);

        // --- Assert ---
        assertNotNull(result);
        assertTrue(result.isEstValide());
        assertEquals(LocalDate.now().getYear(), result.getAnneeUniversitaire().getYear());
        assertNotNull(result.getIdReservation());
        assertTrue(result.getIdReservation().contains("A"));
        assertEquals(1, chambre.getReservations().size());
        assertTrue(result.getEtudiants().contains(etudiant));
    }

    @Test
    void testAnnulerReservation() {
        Reservation r = new Reservation();
        r.setIdReservation("res1");
        r.setEstValide(true);
        Etudiant e = new Etudiant();
        e.setCin(123L);
        r.setEtudiants(List.of(e));
        Chambre c = new Chambre();
        c.setReservations(new ArrayList<>(List.of(r)));

        when(reservationRepository.findByEtudiantsCinAndEstValide(123L, true)).thenReturn(r);
        when(chambreRepository.findByReservationsIdReservation("res1")).thenReturn(c);
        doNothing().when(reservationRepository).delete(r);
        when(chambreRepository.save(any())).thenReturn(c);

        String result = reservationService.annulerReservation(123L);
        assertTrue(result.contains("annulée"));
        verify(reservationRepository).delete(r);
        verify(chambreRepository).save(c);
    }

    @Test
    void testGetReservationParAnneeUniversitaire() {
        when(reservationRepository.countByAnneeUniversitaireBetween(any(), any())).thenReturn(5);
        long count = reservationService.getReservationParAnneeUniversitaire(LocalDate.now().minusYears(1), LocalDate.now());
        assertEquals(5L, count);
    }

    @Test
    void testAffectReservationAChambre() {
        Reservation r = new Reservation();
        r.setIdReservation("res2");
        Chambre c = new Chambre();
        c.setReservations(new ArrayList<>());
        when(reservationRepository.findById("res2")).thenReturn(java.util.Optional.of(r));
        when(chambreRepository.findById(2L)).thenReturn(java.util.Optional.of(c));
        when(chambreRepository.save(any())).thenReturn(c);

        reservationService.affectReservationAChambre("res2", 2L);
        assertTrue(c.getReservations().contains(r));
        verify(chambreRepository).save(c);
    }

    @Test
    void testDeaffectReservationAChambre() {
        Reservation r = new Reservation();
        r.setIdReservation("res3");
        Chambre c = new Chambre();
        c.setReservations(new ArrayList<>(List.of(r)));
        when(reservationRepository.findById("res3")).thenReturn(java.util.Optional.of(r));
        when(chambreRepository.findById(3L)).thenReturn(java.util.Optional.of(c));
        when(chambreRepository.save(any())).thenReturn(c);

        reservationService.deaffectReservationAChambre("res3", 3L);
        assertFalse(c.getReservations().contains(r));
        verify(chambreRepository).save(c);
    }

    @Test
    void testAnnulerReservations() {
        Reservation r = new Reservation();
        r.setIdReservation("res4");
        r.setEstValide(true);
        when(reservationRepository.findByEstValideAndAnneeUniversitaireBetween(eq(true), any(), any())).thenReturn(List.of(r));
        when(reservationRepository.save(any())).thenReturn(r);
        reservationService.annulerReservations();
        verify(reservationRepository).save(r);
        assertFalse(r.isEstValide());
    }
}
