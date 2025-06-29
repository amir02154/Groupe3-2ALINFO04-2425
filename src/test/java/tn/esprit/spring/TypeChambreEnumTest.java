package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import static org.junit.jupiter.api.Assertions.*;

public class TypeChambreEnumTest {

    @Test
    void testEnumValues() {
        for (TypeChambre type : TypeChambre.values()) {
            assertNotNull(type.name());
        }
    }

    @Test
    void testValueOf() {
        assertEquals(TypeChambre.SIMPLE, TypeChambre.valueOf("SIMPLE"));
    }
}