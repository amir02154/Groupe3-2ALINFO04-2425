package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Foyer.FoyerService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FoyerServiceTest {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private FoyerService foyerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addOrUpdate(foyer);
        assertEquals(foyer, result);
    }

    @Test
    void testFindById() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.findById(1L);
        assertEquals(foyer, result);
    }

    @Test
    void testAffecterFoyerAUniversite_nom() {
        Foyer f = new Foyer();
        Universite u = new Universite();

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(f));
        when(universiteRepository.findByNomUniversite("ENIT")).thenReturn(u);
        when(universiteRepository.save(any())).thenReturn(u);

        Universite result = foyerService.affecterFoyerAUniversite(1L, "ENIT");
        assertEquals(f, result.getFoyer());
    }

    @Test
    void testDesaffecterFoyerAUniversite() {
        Universite u = new Universite();
        u.setFoyer(new Foyer());

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(u));
        when(universiteRepository.save(u)).thenReturn(u);

        Universite result = foyerService.desaffecterFoyerAUniversite(1L);
        assertNull(result.getFoyer());
    }
    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Foyer foyer = new Foyer();
        foyer.setNomFoyer("F1");

        Bloc bloc1 = new Bloc();
        Bloc bloc2 = new Bloc();
        foyer.setBlocs(List.of(bloc1, bloc2));

        Universite universite = new Universite();

        when(foyerRepository.save(foyer)).thenReturn(foyer);
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite));
        when(universiteRepository.save(any())).thenReturn(universite);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(foyer, 1L);

        assertEquals(foyer, result);
        verify(blocRepository, times(2)).save(any(Bloc.class));
    }
    @Test
    void testAjoutFoyerEtBlocs() {
        Foyer foyer = new Foyer();
        Bloc bloc = new Bloc();
        foyer.setBlocs(List.of(bloc));

        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.ajoutFoyerEtBlocs(foyer);

        assertEquals(foyer, result);
        verify(blocRepository, times(1)).save(any(Bloc.class));
    }
    @Test
    void testFindAll() {
        foyerService.findAll();
        verify(foyerRepository).findAll();
    }

    @Test
    void testDeleteById() {
        foyerService.deleteById(1L);
        verify(foyerRepository).deleteById(1L);
    }

}
