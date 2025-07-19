package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.Services.Bloc.IBlocService;
import tn.esprit.spring.RestControllers.BlocRestController;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
class BlocRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IBlocService blocService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllBlocs_shouldReturnListOfBlocs() throws Exception {
        Bloc bloc1 = Bloc.builder().idBloc(1L).nomBloc("Bloc A").build();
        Bloc bloc2 = Bloc.builder().idBloc(2L).nomBloc("Bloc B").build();

        when(blocService.findAll()).thenReturn(List.of(bloc1, bloc2));

        mockMvc.perform(get("/api/blocs/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getBlocById_shouldReturnBloc() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc A").build();

        when(blocService.findById(1L)).thenReturn(bloc);

        mockMvc.perform(get("/api/blocs/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void addOrUpdateBloc_shouldReturnBloc() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc C").build();
        when(blocService.addOrUpdate(any())).thenReturn(bloc);

        mockMvc.perform(post("/api/blocs/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc C"));
    }

    @Test
    void deleteBloc_shouldSucceed() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc X").build();

        mockMvc.perform(delete("/api/blocs/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk());

        verify(blocService).delete(any());
    }

    @Test
    void deleteBlocById_shouldSucceed() throws Exception {
        mockMvc.perform(delete("/api/blocs/deleteById").param("id", "1"))
                .andExpect(status().isOk());

        verify(blocService).deleteById(1L);
    }

    @Test
    void affecterChambresABloc_shouldReturnBloc() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc A").build();
        List<Long> chambres = List.of(101L, 102L);
        when(blocService.affecterChambresABloc(any(), eq("Bloc A"))).thenReturn(bloc);

        mockMvc.perform(put("/api/blocs/affecterChambresABloc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("nomBloc", "Bloc A")
                        .content(objectMapper.writeValueAsString(chambres)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc A"));
    }

    @Test
    void affecterBlocAFoyer_shouldReturnBloc() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc A").build();
        when(blocService.affecterBlocAFoyer("Bloc A", "Foyer A")).thenReturn(bloc);

        mockMvc.perform(put("/api/blocs/affecterBlocAFoyer")
                        .param("nomBloc", "Bloc A")
                        .param("nomFoyer", "Foyer A"))
                .andExpect(status().isOk());
    }

    @Test
    void affecterBlocAFoyer2_shouldReturnBloc() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc A").build();
        when(blocService.affecterBlocAFoyer("Bloc A", "Foyer A")).thenReturn(bloc);

        mockMvc.perform(put("/api/blocs/affecterBlocAFoyer2/Foyer A/Bloc A"))
                .andExpect(status().isOk());
    }

    @Test
    void ajouterBlocEtSesChambres_shouldReturnBloc() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc Y").build();
        when(blocService.ajouterBlocEtSesChambres(any())).thenReturn(bloc);

        mockMvc.perform(post("/api/blocs/ajouterBlocEtSesChambres")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc Y"));
    }

    @Test
    void ajouterBlocEtAffecterAFoyer_shouldReturnBloc() throws Exception {
        Bloc bloc = Bloc.builder().idBloc(1L).nomBloc("Bloc Z").build();
        when(blocService.ajouterBlocEtAffecterAFoyer(any(), eq("FoyerX"))).thenReturn(bloc);

        mockMvc.perform(post("/api/blocs/ajouterBlocEtAffecterAFoyer/FoyerX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bloc)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomBloc").value("Bloc Z"));
    }
}
