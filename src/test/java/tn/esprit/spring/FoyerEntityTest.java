package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FoyerEntityTest {

    @Test
    void testFoyerBuilder() {
        // Test avec Builder
        Foyer foyer = Foyer.builder()
                .idFoyer(1L)
                .nomFoyer("Foyer Central")
                .capaciteFoyer(100)
                .build();

        assertEquals(1L, foyer.getIdFoyer());
        assertEquals("Foyer Central", foyer.getNomFoyer());
        assertEquals(100, foyer.getCapaciteFoyer());
    }

    @Test
    void testFoyerNoArgsConstructor() {
        // Test constructeur par défaut
        Foyer foyer = new Foyer();
        assertNotNull(foyer);
    }

    @Test
    void testFoyerAllArgsConstructor() {
        // Test constructeur avec tous les paramètres
        List<Bloc> blocs = new ArrayList<>();
        Universite universite = new Universite();
        
        Foyer foyer = new Foyer(1L, "Foyer Central", 100, universite, blocs);
        
        assertEquals(1L, foyer.getIdFoyer());
        assertEquals("Foyer Central", foyer.getNomFoyer());
        assertEquals(100, foyer.getCapaciteFoyer());
        assertEquals(blocs, foyer.getBlocs());
        assertEquals(universite, foyer.getUniversite());
    }

    @Test
    void testFoyerSettersAndGetters() {
        Foyer foyer = new Foyer();
        
        // Test setters
        foyer.setIdFoyer(1L);
        foyer.setNomFoyer("Foyer Central");
        foyer.setCapaciteFoyer(100);
        
        List<Bloc> blocs = new ArrayList<>();
        foyer.setBlocs(blocs);
        
        Universite universite = new Universite();
        foyer.setUniversite(universite);
        
        // Test getters
        assertEquals(1L, foyer.getIdFoyer());
        assertEquals("Foyer Central", foyer.getNomFoyer());
        assertEquals(100, foyer.getCapaciteFoyer());
        assertEquals(blocs, foyer.getBlocs());
        assertEquals(universite, foyer.getUniversite());
    }

    @Test
    void testFoyerToString() {
        Foyer foyer = Foyer.builder()
                .idFoyer(1L)
                .nomFoyer("Foyer Central")
                .capaciteFoyer(100)
                .build();
        
        String toString = foyer.toString();
        assertNotNull(toString);
        // Lombok génère un toString par défaut, on vérifie juste qu'il n'est pas null
    }

    @Test
    void testFoyerEqualsAndHashCode() {
        // Avec @EqualsAndHashCode(onlyExplicitlyIncluded = true) et @EqualsAndHashCode.Include sur idFoyer
        Foyer foyer1 = Foyer.builder().idFoyer(1L).nomFoyer("Foyer A").build();
        Foyer foyer2 = Foyer.builder().idFoyer(1L).nomFoyer("Foyer A").build();
        Foyer foyer3 = Foyer.builder().idFoyer(2L).nomFoyer("Foyer B").build();
        
        // Test equals - avec @EqualsAndHashCode, les objets avec le même idFoyer sont égaux
        assertEquals(foyer1, foyer2);
        assertNotEquals(foyer1, foyer3);
        assertNotEquals(foyer1, null);
        assertEquals(foyer1, foyer1);
        
        // Test hashCode - avec @EqualsAndHashCode, hashCode est le même pour les objets égaux
        assertEquals(foyer1.hashCode(), foyer2.hashCode());
        assertNotEquals(foyer1.hashCode(), foyer3.hashCode());
    }
} 