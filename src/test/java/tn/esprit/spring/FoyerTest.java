package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Foyer;

import static org.junit.jupiter.api.Assertions.*;

class FoyerTest {
    @Test
    void testFoyerBuilderAndEquals() {
        Foyer foyer = Foyer.builder().nomFoyer("Test").capaciteFoyer(100).build();
        assertEquals("Test", foyer.getNomFoyer());
        assertEquals(100, foyer.getCapaciteFoyer());
    }
}
