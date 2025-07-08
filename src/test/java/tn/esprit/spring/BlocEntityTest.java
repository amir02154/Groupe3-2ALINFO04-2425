package tn.esprit.spring;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.DAO.Entities.Bloc;

import static org.junit.jupiter.api.Assertions.*;

class BlocEntityTest {

    @Test
    void testBlocFields() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        bloc.setNomBloc("Bloc A");
        bloc.setCapaciteBloc(100);

        assertEquals(1L, bloc.getIdBloc());
        assertEquals("Bloc A", bloc.getNomBloc());
        assertEquals(100, bloc.getCapaciteBloc());
    }

    @Test
    void testBlocEntity() {
        Bloc b1 = new Bloc();
        Bloc b2 = new Bloc();
        assertEquals(b1, b2);
        assertEquals(b1.hashCode(), b2.hashCode());
    }
}
