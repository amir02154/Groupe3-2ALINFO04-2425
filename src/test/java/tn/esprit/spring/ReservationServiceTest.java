package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.*;
import tn.esprit.spring.Services.Reservation.ReservationService;

import java.util.Date;
import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void beforeEach() {
        // Préparation avant chaque test si nécessaire
    }

    @AfterEach
    void afterEach() {
        // Nettoyage après chaque test si nécessaire
    }

    @Order(1)
    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() {
        // Préparation des données simulées
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        Bloc bloc = new Bloc();
        bloc.setNomBloc("A");
        chambre.setBloc(bloc);
        chambre.setReservations(new HashSet<>());

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(12345678L);

        // Mocks
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre);
        when(etudiantRepository.findByCin(12345678L)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                anyLong(), any(Date.class), any(Date.class))).thenReturn(0);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        // Appel de la méthode à tester
        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(101L, 12345678L);

        // Vérifications
        assertNotNull(result);
        assertTrue(result.isEstValide());
        assertNotNull(result.getEtudiants());
        assertTrue(result.getEtudiants().contains(etudiant));
    }

    @Order(2)
    @Test
    void testAnnulerReservation() {
        long cinEtudiant = 12345678L;

        // Préparation des données simulées
        Etudiant etudiant = new Etudiant();
        etudiant.setCin(cinEtudiant);

        Reservation reservation = new Reservation();
        reservation.setIdReservation("2023/2024-A-101-12345678");
        reservation.setEstValide(true);
        reservation.setEtudiants(new HashSet<>());
        reservation.getEtudiants().add(etudiant);

        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setReservations(new HashSet<>());
        chambre.getReservations().add(reservation);

        // Mocks
        when(reservationRepository.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation("2023/2024-A-101-12345678")).thenReturn(chambre);

        // Appel de la méthode à tester
        String result = reservationService.annulerReservation(cinEtudiant);

        // Vérifications
        assertNotNull(result);
        assertTrue(result.contains("annulée"));
    }
}
