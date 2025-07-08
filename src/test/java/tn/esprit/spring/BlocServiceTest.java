package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.Services.Bloc.BlocService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlocServiceTest {

    @Mock
    BlocRepository blocRepository;

    @Mock
    ChambreRepository chambreRepository;

    @Mock
    FoyerRepository foyerRepository;

    @InjectMocks
    BlocService blocService;

    Bloc bloc;
    Chambre chambre;

    @BeforeEach
    void setup() {
        chambre = Chambre.builder().numeroChambre(101L).build();
        bloc = Bloc.builder()
                .idBloc(1L)
                .nomBloc("Bloc A")
                .chambres(List.of(chambre))
                .build();
    }

    @Test
    @Order(1)
    void testAddOrUpdate() {
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Bloc savedBloc = blocService.addOrUpdate(bloc);

        assertNotNull(savedBloc);
        assertEquals("Bloc A", savedBloc.getNomBloc());
        verify(chambreRepository, times(1)).save(any(Chambre.class));
        verify(blocRepository, times(1)).save(any(Bloc.class));
    }

    @Test
    @Order(2)
    void testFindAll() {
        when(blocRepository.findAll()).thenReturn(List.of(bloc));
        List<Bloc> blocs = blocService.findAll();

        assertEquals(1, blocs.size());
    }

    @Test
    @Order(3)
    void testFindById_found() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        Bloc result = blocService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
    }

    @Test
    @Order(4)
    void testFindById_notFound() {
        when(blocRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> blocService.findById(2L));
        assertTrue(ex.getMessage().contains("Bloc not found"));
    }

    @Test
    @Order(5)
    void testDeleteById_found() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));
        blocService.deleteById(1L);

        verify(chambreRepository).deleteAll(bloc.getChambres());
        verify(blocRepository).delete(bloc);
    }

    @Test
    @Order(6)
    void testDeleteById_notFound() {
        when(blocRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> blocService.deleteById(99L));
    }

    @Test
    @Order(7)
    void testDeleteBloc() {
        blocService.delete(bloc);

        verify(chambreRepository).deleteAll(bloc.getChambres());
        verify(blocRepository).delete(bloc);
    }

    @Test
    @Order(8)
    void testAffecterChambresABloc() {
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(bloc);
        when(chambreRepository.findByNumeroChambre(101L)).thenReturn(chambre);
        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Bloc result = blocService.affecterChambresABloc(List.of(101L), "Bloc A");

        assertEquals("Bloc A", result.getNomBloc());
        verify(chambreRepository).save(any(Chambre.class));
    }

    @Test
    @Order(9)
    void testAffecterBlocAFoyer() {
        Foyer foyer = Foyer.builder().nomFoyer("Foyer X").build();
        when(blocRepository.findByNomBloc("Bloc A")).thenReturn(bloc);
        when(foyerRepository.findByNomFoyer("Foyer X")).thenReturn(foyer);
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc result = blocService.affecterBlocAFoyer("Bloc A", "Foyer X");

        assertEquals(foyer, result.getFoyer());
        verify(blocRepository).save(any(Bloc.class));
    }

    @Test
    @Order(10)
    void testAjouterBlocEtSesChambres() {
        Bloc blocWithChambres = Bloc.builder().chambres(List.of(chambre)).build();

        when(chambreRepository.save(any(Chambre.class))).thenReturn(chambre);

        Bloc result = blocService.ajouterBlocEtSesChambres(blocWithChambres);

        verify(chambreRepository).save(any(Chambre.class));
        assertNotNull(result);
    }

    @Test
    @Order(11)
    void testAjouterBlocEtAffecterAFoyer() {
        Foyer foyer = Foyer.builder().nomFoyer("Foyer Y").build();
        when(foyerRepository.findByNomFoyer("Foyer Y")).thenReturn(foyer);
        when(blocRepository.save(any(Bloc.class))).thenReturn(bloc);

        Bloc result = blocService.ajouterBlocEtAffecterAFoyer(bloc, "Foyer Y");

        assertEquals(foyer, result.getFoyer());
        verify(blocRepository).save(any(Bloc.class));
    }

    @Test
    void testDeleteById_NotFound_Exception() {
        when(blocRepository.findById(123L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> blocService.deleteById(123L));
    }
}
