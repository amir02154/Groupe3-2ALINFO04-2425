package tn.esprit.spring;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.RestControllers.UniversiteRestController;
import tn.esprit.spring.Services.Universite.IUniversiteService;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UniversiteRestController.class)
public class UniversiteRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUniversiteService universiteService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Universite universite;

    @BeforeEach
    void setup() {
        universite = new Universite();
        universite.setIdUniversite(1L);
        universite.setNomUniversite("ENIT");
        universite.setFoyer(new Foyer());
    }

    @Test
    void testAddOrUpdate() throws Exception {
        when(universiteService.addOrUpdate(any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/universite/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUniversite").value(1L))
                .andExpect(jsonPath("$.nomUniversite").value("ENIT"));
    }

    @Test
    void testFindAll() throws Exception {
        when(universiteService.findAll()).thenReturn(Arrays.asList(universite));

        mockMvc.perform(get("/universite/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomUniversite").value("ENIT"));
    }

    @Test
    void testFindById() throws Exception {
        when(universiteService.findById(1L)).thenReturn(universite);

        mockMvc.perform(get("/universite/findById")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ENIT"));
    }

    @Test
    void testDelete() throws Exception {
        // Just test that delete returns OK (void)
        mockMvc.perform(delete("/universite/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/universite/deleteById")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testAjouterUniversiteEtSonFoyer() throws Exception {
        when(universiteService.ajouterUniversiteEtSonFoyer(any(Universite.class))).thenReturn(universite);

        mockMvc.perform(post("/universite/ajouterUniversiteEtSonFoyer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(universite)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomUniversite").value("ENIT"));
    }
}

