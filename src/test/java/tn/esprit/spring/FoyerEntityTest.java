package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Foyer;

import static org.junit.jupiter.api.Assertions.*;

class FoyerEntityTest {
    @Test
    void testFoyerEntity() {
        Foyer f1 = new Foyer();
        Foyer f2 = new Foyer();
        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
} 