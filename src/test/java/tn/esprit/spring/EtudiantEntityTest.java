package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Etudiant;

import static org.junit.jupiter.api.Assertions.*;

class EtudiantEntityTest {
    @Test
    void testEtudiantEntity() {
        Etudiant e1 = new Etudiant();
        Etudiant e2 = new Etudiant();
        assertNotNull(e1);
        assertNotNull(e2);
    }
} 