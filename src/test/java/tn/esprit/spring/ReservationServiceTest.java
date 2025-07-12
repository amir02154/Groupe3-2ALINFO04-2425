package tn.esprit.spring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import jakarta.persistence.EntityNotFoundException;

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

    private Reservation reservation;
    private Chambre chambre;
    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        reservation = Reservation.builder()
                .idReservation("RES001")
                .anneeUniversitaire(LocalDate.now())
                .estValide(true)
                .build();

        chambre = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(101L)
                .typeC(TypeChambre.SIMPLE)
                .reservations(new ArrayList<>())
                .build();

        etudiant = Etudiant.builder()
                .cin(12345678L)
                .nomEt("Doe")
                .prenomEt("John")
                .build();
    }

    @Test
    void testAddOrUpdate() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.addOrUpdate(reservation);

        assertNotNull(result);
        assertEquals("RES001", result.getIdReservation());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void testFindAll() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> result = reservationService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(reservation, result.get(0));
        verify(reservationRepository).findAll();
    }

    @Test
    void testFindById() {
        when(reservationRepository.findById("RES001")).thenReturn(Optional.of(reservation));

        Reservation result = reservationService.findById("RES001");

        assertNotNull(result);
        assertEquals("RES001", result.getIdReservation());
        verify(reservationRepository).findById("RES001");
    }

    @Test
    void testDeleteById() {
        doNothing().when(reservationRepository).deleteById("RES001");

        reservationService.deleteById("RES001");

        verify(reservationRepository).deleteById("RES001");
    }

    @Test
    void testDelete() {
        doNothing().when(reservationRepository).delete(reservation);

        reservationService.delete(reservation);

        verify(reservationRepository).delete(reservation);
    }

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
    void testGetReservationParAnneeUniversitaire() {
        LocalDate debutAnnee = LocalDate.of(2024, 1, 1);
        LocalDate finAnnee = LocalDate.of(2024, 12, 31);
        int expectedCount = 5;

        when(reservationRepository.countByAnneeUniversitaireBetween(debutAnnee, finAnnee)).thenReturn(expectedCount);

        long result = reservationService.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);

        assertEquals(expectedCount, result);
        verify(reservationRepository).countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Test
    void testAnnulerReservation() {
        long cinEtudiant = 12345678L;
        Reservation reservation = Reservation.builder()
                .idReservation("RES001")
                .estValide(true)
                .build();

        when(reservationRepository.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation("RES001")).thenReturn(chambre);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);
        doNothing().when(reservationRepository).delete(any(Reservation.class));

        String result = reservationService.annulerReservation(cinEtudiant);

        assertNotNull(result);
        assertTrue(result.contains("La réservation RES001 est annulée avec succès"));
        verify(reservationRepository).findByEtudiantsCinAndEstValide(cinEtudiant, true);
        verify(reservationRepository).delete(any(Reservation.class));
    }

    @Test
    void testAnnulerReservation_NotFound() {
        long cinEtudiant = 99999999L;
        when(reservationRepository.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> reservationService.annulerReservation(cinEtudiant));
        verify(reservationRepository).findByEtudiantsCinAndEstValide(cinEtudiant, true);
    }

    @Test
    void testAnnulerReservations() {
        List<Reservation> reservations = List.of(reservation);
        when(reservationRepository.findByEstValideAndAnneeUniversitaireBetween(anyBoolean(), any(), any()))
                .thenReturn(reservations);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        reservationService.annulerReservations();

        verify(reservationRepository).findByEstValideAndAnneeUniversitaireBetween(anyBoolean(), any(), any());
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    void testAffectReservationAChambre() {
        String idRes = "RES001";
        long idChambre = 1L;

        when(reservationRepository.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        reservationService.affectReservationAChambre(idRes, idChambre);

        verify(reservationRepository).findById(idRes);
        verify(chambreRepository).findById(idChambre);
        verify(chambreRepository).save(any(Chambre.class));
    }

    @Test
    void testDeaffectReservationAChambre() {
        String idRes = "RES001";
        long idChambre = 1L;

        when(reservationRepository.findById(idRes)).thenReturn(Optional.of(reservation));
        when(chambreRepository.findById(idChambre)).thenReturn(Optional.of(chambre));
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        reservationService.deaffectReservationAChambre(idRes, idChambre);

        verify(reservationRepository).findById(idRes);
        verify(chambreRepository).findById(idChambre);
        verify(chambreRepository).save(any(Chambre.class));
    }

    @Test
    void testDeleteById_NotFound() {
        doThrow(new RuntimeException("Reservation not found")).when(reservationRepository).deleteById("notfound");
        assertThrows(RuntimeException.class, () -> reservationService.deleteById("notfound"));
    }
}
