package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.RestControllers.BlocRestController;
import tn.esprit.spring.Services.Bloc.IBlocService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
class BlocRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlocService blocService;

    @Test
    void getAllBlocs_shouldReturnListOfBlocs() throws Exception {
        // Préparer des données fictives
        Bloc bloc1 = new Bloc();
        bloc1.setNomBloc("Bloc A");

        Bloc bloc2 = new Bloc();
        bloc2.setNomBloc("Bloc B");

        // Simuler le comportement du service
        when(blocService.findAll()).thenReturn(List.of(bloc1, bloc2));

        // Appeler le endpoint GET /bloc/findAll
        mockMvc.perform(get("/bloc/findAll"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nomBloc").value("Bloc A"))
                .andExpect(jsonPath("$[1].nomBloc").value("Bloc B"));
    }
}
