package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Repositories.FoyerRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FoyerRepositoryTest {
    @Mock
    FoyerRepository foyerRepository;

    @Test
    void testRepositoryNotNull() {
        MockitoAnnotations.openMocks(this);
        assertNotNull(foyerRepository);
    }
} 