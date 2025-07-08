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
    void testDeleteById_NotFound() {
        doThrow(new RuntimeException("Reservation not found")).when(reservationRepository).deleteById("notfound");
        assertThrows(RuntimeException.class, () -> reservationService.deleteById("notfound"));
    }
}
