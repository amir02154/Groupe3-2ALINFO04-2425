package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Universite;

import static org.junit.jupiter.api.Assertions.*;

public class UniversiteEntityTest {

    @Test
    void testSetAndGetNomUniversite() {
        Universite u = new Universite();
        u.setNomUniversite("ESPRIT");
        assertEquals("ESPRIT", u.getNomUniversite());
    }
}