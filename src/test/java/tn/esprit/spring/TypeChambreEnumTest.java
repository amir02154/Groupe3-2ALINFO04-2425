package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import static org.junit.jupiter.api.Assertions.*;

class TypeChambreEnumTest {
    @Test
    void testEnumValues() {
        for (TypeChambre type : TypeChambre.values()) {
            assertNotNull(type);
        }
        assertEquals(TypeChambre.valueOf("SIMPLE"), TypeChambre.SIMPLE);
        assertEquals(TypeChambre.valueOf("DOUBLE"), TypeChambre.DOUBLE);
        assertEquals(TypeChambre.valueOf("TRIPLE"), TypeChambre.TRIPLE);
    }
}