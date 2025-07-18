package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.RestControllers.FoyerRestController;
import tn.esprit.spring.Services.Foyer.IFoyerService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoyerRestController.class)
public class FoyerRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFoyerService service;

    private Foyer foyer;
    private Universite universite;

    @BeforeEach
    void setup() {
        foyer = Foyer.builder().idFoyer(1L).nomFoyer("Foyer Central").build();
        universite = Universite.builder().idUniversite(1L).nomUniversite("ESPRIT").foyer(foyer).build();
    }

    @Test
    void testAddOrUpdate() throws Exception {
        Mockito.when(service.addOrUpdate(any(Foyer.class))).thenReturn(foyer);

        mockMvc.perform(post("/foyer/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(foyer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomFoyer").value("Foyer Central"));
    }

    @Test
    void testFindAll() throws Exception {
        Mockito.when(service.findAll()).thenReturn(Collections.singletonList(foyer));

        mockMvc.perform(get("/foyer/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomFoyer").value("Foyer Central"));
    }

    @Test
    void testFindById() throws Exception {
        Mockito.when(service.findById(anyLong())).thenReturn(foyer);

        mockMvc.perform(get("/foyer/findById?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomFoyer").value("Foyer Central"));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/foyer/deleteById?id=1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAffecterFoyerAUniversite() throws Exception {
        Mockito.when(service.affecterFoyerAUniversite(anyLong(), anyString())).thenReturn(universite);

        mockMvc.perform(put("/foyer/affecterFoyerAUniversite?idFoyer=1&nomUniversite=ESPRIT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ESPRIT"));
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() throws Exception {
        Mockito.when(service.ajouterFoyerEtAffecterAUniversite(any(Foyer.class), anyLong())).thenReturn(foyer);

        mockMvc.perform(post("/foyer/ajouterFoyerEtAffecterAUniversite?idUniversite=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(foyer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomFoyer").value("Foyer Central"));
    }
}
