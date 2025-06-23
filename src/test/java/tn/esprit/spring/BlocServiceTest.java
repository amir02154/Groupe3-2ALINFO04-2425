package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Repositories.BlocRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import tn.esprit.spring.Services.Bloc.BlocService;

@ExtendWith(MockitoExtension.class)
class BlocServiceTest {

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    BlocService blocService;

    @Test
    void testGetBlocById() {
        Bloc bloc = new Bloc();
        bloc.setIdBloc(1L);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.findById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
    }
}