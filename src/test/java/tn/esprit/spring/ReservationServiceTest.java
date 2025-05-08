package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;
import tn.esprit.spring.Services.Reservation.ReservationService;

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
        // Préparation avant chaque test
    }

    @AfterEach
    void afterEach() {
        // Nettoyage après chaque test
    }

    @Order(1)
    @Test
    void testAjouterReservationEtAssignerAChambreEtAEtudiant() {
        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.SIMPLE);
        Bloc bloc = new Bloc();
        bloc.setNomBloc("A");
        chambre.setBloc(bloc);

        Etudiant etudiant = new Etudiant();
        etudiant.setCin(12345678L);

        // Mocks des méthodes
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre);
        when(etudiantRepository.findByCin(12345678L)).thenReturn(etudiant);
        when(chambreRepository.countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                anyLong(), any(), any())).thenReturn(0);
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        // Exécution du test
        Reservation result = reservationService.ajouterReservationEtAssignerAChambreEtAEtudiant(101L, 12345678L);

        // Assertions
        assertNotNull(result);
        assertTrue(result.getEstValide());
        assertTrue(result.getEtudiants().contains(etudiant));
    }

    @Order(2)
    @Test
    void testAnnulerReservation() {
        // Test d'annulation de réservation
        long cinEtudiant = 12345678L;
        
        // Mocks
        Etudiant etudiant = new Etudiant();
        etudiant.setCin(cinEtudiant);

        Reservation reservation = new Reservation();
        reservation.setIdReservation("2023/2024-A-101-12345678");
        reservation.setEstValide(true);
        reservation.getEtudiants().add(etudiant);

        Chambre chambre = new Chambre();
        chambre.setIdChambre(1L);
        chambre.getReservations().add(reservation);

        when(reservationRepository.findByEtudiantsCinAndEstValide(cinEtudiant, true)).thenReturn(reservation);
        when(chambreRepository.findByReservationsIdReservation("2023/2024-A-101-12345678")).thenReturn(chambre);

        String result = reservationService.annulerReservation(cinEtudiant);

        assertNotNull(result);
        assertTrue(result.contains("annulée"));
    }
}