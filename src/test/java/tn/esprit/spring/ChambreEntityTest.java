package tn.esprit.spring;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import static org.junit.jupiter.api.Assertions.*;

class ChambreEntityTest {

    @Test
    void testChambreFields() {
        Chambre c = new Chambre();
        c.setIdChambre(2L);
        c.setNumeroChambre(101);
        c.setTypeC(TypeChambre.SIMPLE);

        assertEquals(2L, c.getIdChambre());
        assertEquals(101, c.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, c.getTypeC());
    }

    @Test
    void testChambreEntity() {
        Chambre c1 = new Chambre();
        Chambre c2 = new Chambre();
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}

