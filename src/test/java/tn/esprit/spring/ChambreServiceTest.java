package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Chambre chambre = Chambre.builder()
                .numeroChambre(101)
                .typeC(TypeChambre.DOUBLE)
                .build();

        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.addOrUpdate(chambre);
        assertEquals(chambre, result);
    }

    @Test
    void testFindById() {
        Chambre chambre = Chambre.builder()
                .numeroChambre(102)
                .typeC(TypeChambre.SIMPLE)
                .build();

        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre found = chambreService.findById(1L);
        assertNotNull(found);
        assertEquals(102, found.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, found.getTypeC());
    }

    @Test
    void testFindById_NotFound() {
        when(chambreRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> chambreService.findById(99L));
    }

    @Test
    void testDelete() {
        Chambre chambre = Chambre.builder()
                .numeroChambre(103)
                .typeC(TypeChambre.TRIPLE)
                .build();

        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Optional<Chambre> optionalChambre = chambreRepository.findById(1L);
        optionalChambre.ifPresent(c -> chambreService.delete(c));

        verify(chambreRepository, times(1)).delete(chambre);
    }
    @Test
    void testNbChambreParTypeEtBloc() {
        Chambre c1 = new Chambre();
        c1.setTypeC(TypeChambre.DOUBLE);
        c1.setBloc(new Bloc());
        c1.getBloc().setIdBloc(1L);

        Chambre c2 = new Chambre();
        c2.setTypeC(TypeChambre.SIMPLE);
        c2.setBloc(new Bloc());
        c2.getBloc().setIdBloc(1L);

        when(chambreRepository.findAll()).thenReturn(List.of(c1, c2));

        long count = chambreService.nbChambreParTypeEtBloc(TypeChambre.DOUBLE, 1L);
        assertEquals(1, count);
    }
    @Test
    void testGetChambresParNomBloc() {
        Chambre c1 = new Chambre();
        Chambre c2 = new Chambre();
        when(chambreRepository.findByBlocNomBloc("BlocA")).thenReturn(List.of(c1, c2));

        List<Chambre> chambres = chambreService.getChambresParNomBloc("BlocA");
        assertEquals(2, chambres.size());
    }
    @Mock
    BlocRepository blocRepository; // à ajouter en haut aussi

    @Test
    void testGetChambresParNomBlocJava() {
        Chambre c1 = new Chambre();
        Chambre c2 = new Chambre();
        Bloc bloc = new Bloc();
        bloc.setChambres(List.of(c1, c2));

        when(blocRepository.findByNomBloc("BlocA")).thenReturn(bloc);

        List<Chambre> chambres = chambreService.getChambresParNomBlocJava("BlocA");
        assertEquals(2, chambres.size());
    }
    @Test
    void testGetChambresParNomBlocJava_BlocNotFound() {
        when(blocRepository.findByNomBloc("BlocInexistant")).thenReturn(null);
        List<Chambre> chambres = chambreService.getChambresParNomBlocJava("BlocInexistant");
        assertNotNull(chambres);
        assertTrue(chambres.isEmpty());
    }
    @Test
    void testGetChambresParNomBlocKeyWord() {
        Chambre c1 = new Chambre();
        Chambre c2 = new Chambre();
        when(chambreRepository.findByBlocNomBloc("BlocB")).thenReturn(List.of(c1, c2));

        List<Chambre> chambres = chambreService.getChambresParNomBlocKeyWord("BlocB");
        assertEquals(2, chambres.size());
    }

    @Test
    void testGetChambresParNomBlocJPQL() {
        Chambre c1 = new Chambre();
        Chambre c2 = new Chambre();
        when(chambreRepository.getChambresParNomBlocJPQL("BlocC")).thenReturn(List.of(c1, c2));

        List<Chambre> chambres = chambreService.getChambresParNomBlocJPQL("BlocC");
        assertEquals(2, chambres.size());
    }

    @Test
    void testGetChambresParNomBlocSQL() {
        Chambre c1 = new Chambre();
        Chambre c2 = new Chambre();
        when(chambreRepository.getChambresParNomBlocSQL("BlocD")).thenReturn(List.of(c1, c2));

        List<Chambre> chambres = chambreService.getChambresParNomBlocSQL("BlocD");
        assertEquals(2, chambres.size());
    }
    @Test
    void testDeleteById() {
        chambreService.deleteById(5L);
        verify(chambreRepository, times(1)).deleteById(5L);
    }

    @Test
    void testGetChambresNonReserveParNomFoyerEtTypeChambre() {
        Chambre c1 = new Chambre();
        c1.setTypeC(TypeChambre.SIMPLE);
        Bloc bloc = new Bloc();
        bloc.setFoyer(new tn.esprit.spring.DAO.Entities.Foyer());
        bloc.getFoyer().setNomFoyer("FoyerTest");
        c1.setBloc(bloc);
        c1.setReservations(List.of());

        when(chambreRepository.findAll()).thenReturn(List.of(c1));

        List<Chambre> chambres = chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("FoyerTest", TypeChambre.SIMPLE);
        assertEquals(1, chambres.size());
    }

    @Test
    void testPourcentageChambreParTypeChambre() {
        when(chambreRepository.count()).thenReturn(10L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.SIMPLE)).thenReturn(4L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.DOUBLE)).thenReturn(3L);
        when(chambreRepository.countChambreByTypeC(TypeChambre.TRIPLE)).thenReturn(3L);
        chambreService.pourcentageChambreParTypeChambre();
        // Pas d'assertion car la méthode ne retourne rien, mais on vérifie qu'elle ne lève pas d'exception
    }

    @Test
    void testNbPlacesDisponibleParChambreAnneeEnCours() {
        Chambre c1 = new Chambre();
        c1.setIdChambre(1L);
        c1.setNumeroChambre(101);
        c1.setTypeC(TypeChambre.SIMPLE);
        when(chambreRepository.findAll()).thenReturn(List.of(c1));
        when(chambreRepository.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(anyLong(), anyBoolean(), any(), any())).thenReturn(0L);
        chambreService.nbPlacesDisponibleParChambreAnneeEnCours();
        // Pas d'assertion car la méthode ne retourne rien, mais on vérifie qu'elle ne lève pas d'exception
    }
}
