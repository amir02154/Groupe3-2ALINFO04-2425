package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private ChambreService chambreService;

    private Chambre chambre;
    private Bloc bloc;

    @BeforeEach
    void setUp() {
        Foyer foyer = Foyer.builder()
                .idFoyer(1L)
                .nomFoyer("Foyer A")
                .capaciteFoyer(100)
                .build();

        bloc = Bloc.builder()
                .idBloc(1L)
                .nomBloc("Bloc A")
                .capaciteBloc(50)
                .foyer(foyer)
                .chambres(new ArrayList<>())
                .build();

        chambre = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(101L)
                .typeC(TypeChambre.SIMPLE)
                .bloc(bloc)
                .reservations(new ArrayList<>())
                .build();

        // Ajouter la chambre au bloc
        bloc.getChambres().add(chambre);
    }

    @Test
    void testAddOrUpdate() {
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Chambre result = chambreService.addOrUpdate(chambre);

        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreRepository).save(chambre);
    }

    @Test
    void testFindAll() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(chambreRepository).findAll();
    }

    @Test
    void testFindById() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre result = chambreService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdChambre());
        verify(chambreRepository).findById(1L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(chambreRepository).deleteById(1L);

        chambreService.deleteById(1L);

        verify(chambreRepository).deleteById(1L);
    }

    @Test
    void testDelete() {
        doNothing().when(chambreRepository).delete(chambre);

        chambreService.delete(chambre);

        verify(chambreRepository).delete(chambre);
    }

    @Test
    void testGetChambresParNomBloc() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findByBlocNomBloc("Bloc A")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBloc("Bloc A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(chambreRepository).findByBlocNomBloc("Bloc A");
    }

    @Test
    void testNbChambreParTypeEtBloc() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);

        long result = chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L);

        assertEquals(1, result);
        verify(chambreRepository).findAll();
    }

    @Test
    void testGetChambresNonReserveParNomFoyerEtTypeChambre() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findAll()).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("Foyer A", TypeChambre.SIMPLE);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(chambreRepository).findAll();
    }

    @Test
    void testListeChambresParBloc() {
        when(blocRepository.findAll()).thenReturn(List.of(bloc));

        // Cette méthode ne retourne rien, on teste juste qu'elle ne lance pas d'exception
        assertDoesNotThrow(() -> chambreService.listeChambresParBloc());
    }

    @Test
    void testPourcentageChambreParTypeChambre() {
        when(chambreRepository.count()).thenReturn(10L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.SIMPLE)).thenReturn(4L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.DOUBLE)).thenReturn(3L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.TRIPLE)).thenReturn(3L);

        // Cette méthode ne retourne rien, on teste juste qu'elle ne lance pas d'exception
        assertDoesNotThrow(() -> chambreService.pourcentageChambreParTypeChambre());
    }

    @Test
    void testNbPlacesDisponibleParChambreAnneeEnCours() {
        when(chambreRepository.findAll()).thenReturn(List.of(chambre));
        when(chambreRepository.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                anyLong(), anyBoolean(), any(), any()
        )).thenReturn(0L);

        // Cette méthode ne retourne rien, on teste juste qu'elle ne lance pas d'exception
        assertDoesNotThrow(() -> chambreService.nbPlacesDisponibleParChambreAnneeEnCours());
    }

    @Test
    void testGetChambresParNomBlocJava() {
        List<Chambre> chambres = List.of(chambre);
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(bloc);

        List<Chambre> result = chambreService.getChambresParNomBlocJava("Bloc A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(blocRepository).findByNomBloc("Bloc A");
    }

    @Test
    void testGetChambresParNomBlocJava_BlocNotFound() {
        when(blocRepository.findByNomBloc("Bloc Inexistant")).thenReturn(null);

        List<Chambre> result = chambreService.getChambresParNomBlocJava("Bloc Inexistant");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(blocRepository).findByNomBloc("Bloc Inexistant");
    }

    @Test
    void testGetChambresParNomBlocKeyWord() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.findByBlocNomBloc("Bloc A")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBlocKeyWord("Bloc A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(chambreRepository).findByBlocNomBloc("Bloc A");
    }

    @Test
    void testGetChambresParNomBlocJPQL() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.getChambresParNomBlocJPQL("Bloc A")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBlocJPQL("Bloc A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(chambreRepository).getChambresParNomBlocJPQL("Bloc A");
    }

    @Test
    void testGetChambresParNomBlocSQL() {
        List<Chambre> chambres = List.of(chambre);
        when(chambreRepository.getChambresParNomBlocSQL("Bloc A")).thenReturn(chambres);

        List<Chambre> result = chambreService.getChambresParNomBlocSQL("Bloc A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chambre, result.get(0));
        verify(chambreRepository).getChambresParNomBlocSQL("Bloc A");
    }

    @Test
    void testFindById_NotFound() {
        when(chambreRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chambreService.findById(999L));
        verify(chambreRepository).findById(999L);
    }
}
