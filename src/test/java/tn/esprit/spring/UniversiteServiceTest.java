package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.DAO.Repositories.UniversiteRepository;
import tn.esprit.spring.Services.Universite.UniversiteService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddOrUpdate() {
        Universite u = new Universite();
        u.setNomUniversite("ENIT");

        when(universiteRepository.save(u)).thenReturn(u);

        Universite saved = universiteService.addOrUpdate(u);
        assertEquals("ENIT", saved.getNomUniversite());

        verify(universiteRepository, times(1)).save(u);
    }

    @Test
    void testFindByIdFound() {
        Universite u = new Universite();
        u.setIdUniversite(1L);
        u.setNomUniversite("ESPRIT");

        when(universiteRepository.findById(1L)).thenReturn(Optional.of(u));

        Universite result = universiteService.findById(1L);
        assertNotNull(result);
        assertEquals("ESPRIT", result.getNomUniversite());
    }

    @Test
    void testFindByIdNotFound() {
        when(universiteRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            universiteService.findById(2L);
        });
        assertEquals("Universite not found with id 2", exception.getMessage());
    }

    @Test
    void testFindAll() {
        Universite u1 = new Universite();
        Universite u2 = new Universite();

        when(universiteRepository.findAll()).thenReturn(List.of(u1, u2));

        List<Universite> all = universiteService.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void testDelete() {
        Universite u = new Universite();
        universiteService.delete(u);
        verify(universiteRepository, times(1)).delete(u);
    }

    @Test
    void testDeleteById() {
        Long id = 5L;
        universiteService.deleteById(id);
        verify(universiteRepository, times(1)).deleteById(id);
    }

    @Test
    void testAjouterUniversiteEtSonFoyer() {
        Universite u = new Universite();
        Foyer f = new Foyer();
        u.setFoyer(f);

        when(universiteRepository.save(any(Universite.class))).thenReturn(u);

        Universite result = universiteService.ajouterUniversiteEtSonFoyer(u);

        assertNotNull(result);
        assertEquals(f, result.getFoyer());

        verify(universiteRepository, times(1)).save(u);
    }
}
