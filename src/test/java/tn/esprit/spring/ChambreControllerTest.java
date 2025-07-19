package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.RestControllers.ChambreRestController;
import tn.esprit.spring.Services.Chambre.IChambreService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChambreRestController.class)
class ChambreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IChambreService chambreService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddOrUpdateChambre() throws Exception {
        Chambre chambre = Chambre.builder().numeroChambre(101L).typeC(TypeChambre.SIMPLE).build();
        when(chambreService.addOrUpdate(any())).thenReturn(chambre);

        mockMvc.perform(post("/api/chambres/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chambre)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroChambre").value(101));
    }

    @Test
    void testFindAll() throws Exception {
        Chambre chambre1 = Chambre.builder().numeroChambre(101L).build();
        Chambre chambre2 = Chambre.builder().numeroChambre(102L).build();

        when(chambreService.findAll()).thenReturn(List.of(chambre1, chambre2));

        mockMvc.perform(get("/api/chambres/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindById() throws Exception {
        Chambre chambre = Chambre.builder().numeroChambre(101L).build();
        when(chambreService.findById(1L)).thenReturn(chambre);

        mockMvc.perform(get("/api/chambres/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroChambre").value(101));
    }

    @Test
    void testDeleteChambre() throws Exception {
        Chambre chambre = Chambre.builder().numeroChambre(103L).build();

        mockMvc.perform(delete("/api/chambres/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chambre)))
                .andExpect(status().isOk());

        verify(chambreService).delete(any());
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/api/chambres/deleteById").param("id", "1"))
                .andExpect(status().isOk());

        verify(chambreService).deleteById(1L);
    }

    @Test
    void testGetChambresParNomBloc() throws Exception {
        when(chambreService.getChambresParNomBloc("BlocA")).thenReturn(List.of());

        mockMvc.perform(get("/api/chambres/getChambresParNomBloc").param("nomBloc", "BlocA"))
                .andExpect(status().isOk());
    }

    @Test
    void testNbChambreParTypeEtBloc() throws Exception {
        when(chambreService.nbChambreParTypeEtBloc(TypeChambre.SIMPLE, 1L)).thenReturn(5L);

        mockMvc.perform(get("/api/chambres/nbChambreParTypeEtBloc")
                        .param("type", "SIMPLE")
                        .param("idBloc", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testGetChambresNonReserveParNomFoyerEtTypeChambre() throws Exception {
        when(chambreService.getChambresNonReserveParNomFoyerEtTypeChambre("FoyerX", TypeChambre.DOUBLE))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/chambres/getChambresNonReserveParNomFoyerEtTypeChambre")
                        .param("nomFoyer", "FoyerX")
                        .param("type", "DOUBLE"))
                .andExpect(status().isOk());
    }
}
