package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import static org.junit.jupiter.api.Assertions.*;

class TypeChambreEnumTest {

    @Test
    void testEnumValues() {
        assertNotNull(TypeChambre.valueOf("SIMPLE"));
        assertEquals(3, TypeChambre.values().length);
    }
}