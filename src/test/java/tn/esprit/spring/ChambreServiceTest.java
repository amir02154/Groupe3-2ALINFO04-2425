package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Chambre.ChambreService;

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
}
