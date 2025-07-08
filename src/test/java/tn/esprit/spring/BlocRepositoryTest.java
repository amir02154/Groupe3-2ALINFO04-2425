package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.DAO.Repositories.BlocRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BlocRepositoryTest {
    @Mock
    BlocRepository blocRepository;

    @Test
    void testRepositoryNotNull() {
        MockitoAnnotations.openMocks(this);
        assertNotNull(blocRepository);
    }
} 