package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Foyer;

import static org.junit.jupiter.api.Assertions.*;

public class FoyerEntityEqualsHashCodeTest {

    @Test
    void testEqualsAndHashCode() {
        Foyer f1 = new Foyer();
        f1.setIdFoyer(1L);
        f1.setNomFoyer("F1");

        Foyer f2 = new Foyer();
        f2.setIdFoyer(1L);
        f2.setNomFoyer("F1");

        assertEquals(f1, f2);
        assertEquals(f1.hashCode(), f2.hashCode());
    }
}