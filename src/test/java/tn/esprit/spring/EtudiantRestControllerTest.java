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
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.RestControllers.EtudiantRestController;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EtudiantRestController.class)
public class EtudiantRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEtudiantService etudiantService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(etudiantService);
    }

    @Test
    void testGetAllEtudiants() throws Exception {
        Mockito.when(etudiantService.findAll()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/etudiant/getAll"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddEtudiant() throws Exception {
        Etudiant etudiant = new Etudiant();
        Mockito.when(etudiantService.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant);
        mockMvc.perform(post("/etudiant/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteEtudiant() throws Exception {
        mockMvc.perform(delete("/etudiant/delete")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetEtudiantById() throws Exception {
        Etudiant etudiant = new Etudiant();
        Mockito.when(etudiantService.findById(anyLong())).thenReturn(etudiant);
        mockMvc.perform(get("/etudiant/getById").param("id", "1"))
                .andExpect(status().isOk());
    }
} 