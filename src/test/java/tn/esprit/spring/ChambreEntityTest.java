package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChambreEntityTest {

    @Test
    void testChambreBuilder() {
        // Test avec Builder
        Chambre chambre = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(101L)
                .typeC(TypeChambre.SIMPLE)
                .build();

        assertEquals(1L, chambre.getIdChambre());
        assertEquals(101L, chambre.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, chambre.getTypeC());
    }

    @Test
    void testChambreNoArgsConstructor() {
        // Test constructeur par défaut
        Chambre chambre = new Chambre();
        assertNotNull(chambre);
    }

    @Test
    void testChambreAllArgsConstructor() {
        // Test constructeur avec tous les paramètres
        Bloc bloc = new Bloc();
        List<Reservation> reservations = new ArrayList<>();
        
        Chambre chambre = new Chambre(1L, 101L, TypeChambre.SIMPLE, bloc, reservations);
        
        assertEquals(1L, chambre.getIdChambre());
        assertEquals(101L, chambre.getNumeroChambre());
        assertEquals(TypeChambre.SIMPLE, chambre.getTypeC());
        assertEquals(bloc, chambre.getBloc());
        assertEquals(reservations, chambre.getReservations());
    }

    @Test
    void testChambreSettersAndGetters() {
        Chambre chambre = new Chambre();
        
        // Test setters
        chambre.setIdChambre(1L);
        chambre.setNumeroChambre(101L);
        chambre.setTypeC(TypeChambre.DOUBLE);
        
        Bloc bloc = new Bloc();
        chambre.setBloc(bloc);
        
        List<Reservation> reservations = new ArrayList<>();
        chambre.setReservations(reservations);
        
        // Test getters
        assertEquals(1L, chambre.getIdChambre());
        assertEquals(101L, chambre.getNumeroChambre());
        assertEquals(TypeChambre.DOUBLE, chambre.getTypeC());
        assertEquals(bloc, chambre.getBloc());
        assertEquals(reservations, chambre.getReservations());
    }

    @Test
    void testChambreToString() {
        Chambre chambre = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(101L)
                .typeC(TypeChambre.SIMPLE)
                .build();
        
        String toString = chambre.toString();
        assertNotNull(toString);
        // Lombok génère un toString par défaut, on vérifie juste qu'il n'est pas null
    }

    @Test
    void testChambreEqualsAndHashCode() {
        // Sans @EqualsAndHashCode, les objets ne sont égaux que s'ils sont la même instance
        Chambre chambre1 = Chambre.builder().idChambre(1L).numeroChambre(101L).build();
        Chambre chambre2 = Chambre.builder().idChambre(1L).numeroChambre(101L).build();
        Chambre chambre3 = Chambre.builder().idChambre(2L).numeroChambre(102L).build();
        
        // Test equals - sans @EqualsAndHashCode, les objets ne sont égaux que s'ils sont la même instance
        assertNotEquals(chambre1, chambre2);
        assertNotEquals(chambre1, chambre3);
        assertNotEquals(chambre1, null);
        assertEquals(chambre1, chambre1);
        
        // Test hashCode - sans @EqualsAndHashCode, hashCode peut être différent même pour des objets identiques
        assertNotEquals(chambre1.hashCode(), chambre2.hashCode());
        assertNotEquals(chambre1.hashCode(), chambre3.hashCode());
    }

    @Test
    void testChambreWithDifferentTypes() {
        Chambre chambreSimple = Chambre.builder()
                .idChambre(1L)
                .numeroChambre(101L)
                .typeC(TypeChambre.SIMPLE)
                .build();

        Chambre chambreDouble = Chambre.builder()
                .idChambre(2L)
                .numeroChambre(102L)
                .typeC(TypeChambre.DOUBLE)
                .build();

        Chambre chambreTriple = Chambre.builder()
                .idChambre(3L)
                .numeroChambre(103L)
                .typeC(TypeChambre.TRIPLE)
                .build();

        assertEquals(TypeChambre.SIMPLE, chambreSimple.getTypeC());
        assertEquals(TypeChambre.DOUBLE, chambreDouble.getTypeC());
        assertEquals(TypeChambre.TRIPLE, chambreTriple.getTypeC());
    }
}

