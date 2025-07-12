package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Repositories.BlocRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BlocRepositoryTest {

    @Autowired
    private BlocRepository blocRepository;

    @Test
    void testSaveBloc() {
        // Arrange
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(50)
                .build();

        // Act
        Bloc savedBloc = blocRepository.save(bloc);

        // Assert
        assertNotNull(savedBloc);
        assertNotNull(savedBloc.getIdBloc());
        assertEquals("Bloc A", savedBloc.getNomBloc());
        assertEquals(50, savedBloc.getCapaciteBloc());
    }

    @Test
    void testFindById() {
        // Arrange
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc B")
                .capaciteBloc(60)
                .build();
        Bloc savedBloc = blocRepository.save(bloc);

        // Act
        Optional<Bloc> foundBloc = blocRepository.findById(savedBloc.getIdBloc());

        // Assert
        assertTrue(foundBloc.isPresent());
        assertEquals("Bloc B", foundBloc.get().getNomBloc());
        assertEquals(60, foundBloc.get().getCapaciteBloc());
    }

    @Test
    void testFindAll() {
        // Arrange
        Bloc bloc1 = Bloc.builder().nomBloc("Bloc A").capaciteBloc(50).build();
        Bloc bloc2 = Bloc.builder().nomBloc("Bloc B").capaciteBloc(60).build();
        blocRepository.save(bloc1);
        blocRepository.save(bloc2);

        // Act
        List<Bloc> blocs = blocRepository.findAll();

        // Assert
        assertNotNull(blocs);
        assertTrue(blocs.size() >= 2);
        assertTrue(blocs.stream().anyMatch(b -> "Bloc A".equals(b.getNomBloc())));
        assertTrue(blocs.stream().anyMatch(b -> "Bloc B".equals(b.getNomBloc())));
    }

    @Test
    void testDeleteById() {
        // Arrange
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc C")
                .capaciteBloc(70)
                .build();
        Bloc savedBloc = blocRepository.save(bloc);

        // Act
        blocRepository.deleteById(savedBloc.getIdBloc());

        // Assert
        Optional<Bloc> deletedBloc = blocRepository.findById(savedBloc.getIdBloc());
        assertFalse(deletedBloc.isPresent());
    }

    @Test
    void testFindByNomBloc() {
        // Arrange
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc D")
                .capaciteBloc(80)
                .build();
        blocRepository.save(bloc);

        // Act
        Bloc foundBloc = blocRepository.findByNomBloc("Bloc D");

        // Assert
        assertNotNull(foundBloc);
        assertEquals("Bloc D", foundBloc.getNomBloc());
        assertEquals(80, foundBloc.getCapaciteBloc());
    }

    @Test
    void testFindByNomBloc_NotFound() {
        // Act
        Bloc foundBloc = blocRepository.findByNomBloc("NonExistentBloc");

        // Assert
        assertNull(foundBloc);
    }

    @Test
    void testSelectByNomBJPQL1() {
        // Arrange
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc F")
                .capaciteBloc(100)
                .build();
        blocRepository.save(bloc);

        // Act
        Bloc foundBloc = blocRepository.selectByNomBJPQL1("Bloc F");

        // Assert
        assertNotNull(foundBloc);
        assertEquals("Bloc F", foundBloc.getNomBloc());
        assertEquals(100, foundBloc.getCapaciteBloc());
    }

    @Test
    void testGetByNomBloc() {
        // Arrange
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc G")
                .capaciteBloc(110)
                .build();
        blocRepository.save(bloc);

        // Act
        List<Bloc> blocs = blocRepository.getByNomBloc("Bloc G");

        // Assert
        assertNotNull(blocs);
        assertFalse(blocs.isEmpty());
        assertEquals("Bloc G", blocs.get(0).getNomBloc());
        assertEquals(110, blocs.get(0).getCapaciteBloc());
    }

    @Test
    void testFindByCapaciteBloc() {
        // Arrange
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc H")
                .capaciteBloc(120)
                .build();
        blocRepository.save(bloc);

        // Act
        Bloc foundBloc = blocRepository.findByCapaciteBloc(120);

        // Assert
        assertNotNull(foundBloc);
        assertEquals("Bloc H", foundBloc.getNomBloc());
        assertEquals(120, foundBloc.getCapaciteBloc());
    }
} 