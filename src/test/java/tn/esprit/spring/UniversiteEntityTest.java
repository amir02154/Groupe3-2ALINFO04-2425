package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;

import static org.junit.jupiter.api.Assertions.*;

class UniversiteEntityTest {

    @Test
    void testUniversiteBuilder() {
        // Test avec Builder
        Universite universite = Universite.builder()
                .idUniversite(1L)
                .nomUniversite("ESPRIT")
                .adresse("Tunis")
                .build();

        assertEquals(1L, universite.getIdUniversite());
        assertEquals("ESPRIT", universite.getNomUniversite());
        assertEquals("Tunis", universite.getAdresse());
    }

    @Test
    void testUniversiteNoArgsConstructor() {
        // Test constructeur par défaut
        Universite universite = new Universite();
        assertNotNull(universite);
    }

    @Test
    void testUniversiteAllArgsConstructor() {
        // Test constructeur avec tous les paramètres
        Foyer foyer = new Foyer();
        
        Universite universite = new Universite(1L, "ESPRIT", "Tunis", foyer);
        
        assertEquals(1L, universite.getIdUniversite());
        assertEquals("ESPRIT", universite.getNomUniversite());
        assertEquals("Tunis", universite.getAdresse());
        assertEquals(foyer, universite.getFoyer());
    }

    @Test
    void testUniversiteSettersAndGetters() {
        Universite universite = new Universite();
        
        // Test setters
        universite.setIdUniversite(1L);
        universite.setNomUniversite("ESPRIT");
        universite.setAdresse("Tunis");
        
        Foyer foyer = new Foyer();
        universite.setFoyer(foyer);
        
        // Test getters
        assertEquals(1L, universite.getIdUniversite());
        assertEquals("ESPRIT", universite.getNomUniversite());
        assertEquals("Tunis", universite.getAdresse());
        assertEquals(foyer, universite.getFoyer());
    }

    @Test
    void testUniversiteToString() {
        Universite universite = Universite.builder()
                .idUniversite(1L)
                .nomUniversite("ESPRIT")
                .adresse("Tunis")
                .build();
        
        String toString = universite.toString();
        assertNotNull(toString);
        // Lombok génère un toString par défaut, on vérifie juste qu'il n'est pas null
    }

    @Test
    void testUniversiteEqualsAndHashCode() {
        // Sans @EqualsAndHashCode, les objets ne sont égaux que s'ils sont la même instance
        Universite universite1 = Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").build();
        Universite universite2 = Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").build();
        Universite universite3 = Universite.builder().idUniversite(2L).nomUniversite("ENIT").build();
        
        // Test equals - sans @EqualsAndHashCode, les objets ne sont égaux que s'ils sont la même instance
        assertNotEquals(universite1, universite2);
        assertNotEquals(universite1, universite3);
        assertNotEquals(universite1, null);
        assertEquals(universite1, universite1);
        
        // Test hashCode - sans @EqualsAndHashCode, hashCode peut être différent même pour des objets identiques
        assertNotEquals(universite1.hashCode(), universite2.hashCode());
        assertNotEquals(universite1.hashCode(), universite3.hashCode());
    }

    @Test
    void testUniversiteWithFoyer() {
        Universite universite = Universite.builder()
                .idUniversite(1L)
                .nomUniversite("ESPRIT")
                .adresse("Tunis")
                .build();

        Foyer foyer = Foyer.builder()
                .idFoyer(1L)
                .nomFoyer("Foyer Central")
                .capaciteFoyer(100)
                .build();

        universite.setFoyer(foyer);

        assertEquals(foyer, universite.getFoyer());
        assertEquals("Foyer Central", universite.getFoyer().getNomFoyer());
        assertEquals(100, universite.getFoyer().getCapaciteFoyer());
    }
}