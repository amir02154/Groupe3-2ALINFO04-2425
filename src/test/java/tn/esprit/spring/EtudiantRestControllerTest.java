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

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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

    private Etudiant etudiant;

    @BeforeEach
    void setUp() {
        Mockito.reset(etudiantService);
        etudiant = new Etudiant();
        etudiant.setIdEtudiant(1L);
        etudiant.setNomEt("Doe");
        etudiant.setPrenomEt("John");
    }

    @Test
    void testGetAllEtudiants() throws Exception {
        Mockito.when(etudiantService.findAll()).thenReturn(Arrays.asList(etudiant));
        mockMvc.perform(get("/etudiant/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomEt").value("Doe"));
    }

    @Test
    void testAddEtudiant() throws Exception {
        Mockito.when(etudiantService.addOrUpdate(any(Etudiant.class))).thenReturn(etudiant);
        mockMvc.perform(post("/etudiant/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEt").value("Doe"));
    }

    @Test
    void testDeleteEtudiant() throws Exception {
        mockMvc.perform(delete("/etudiant/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(etudiant)))
                .andExpect(status().isOk());

        Mockito.verify(etudiantService).delete(any(Etudiant.class));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/etudiant/deleteById").param("id", "1"))
                .andExpect(status().isOk());

        Mockito.verify(etudiantService).deleteById(1L);
    }

    @Test
    void testGetEtudiantById() throws Exception {
        Mockito.when(etudiantService.findById(anyLong())).thenReturn(etudiant);
        mockMvc.perform(get("/etudiant/findById").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEt").value("Doe"));
    }

    @Test
    void testGetEtudiantById_NotFound() throws Exception {
        Mockito.when(etudiantService.findById(anyLong())).thenThrow(new RuntimeException("Not found"));
        mockMvc.perform(get("/etudiant/findById").param("id", "999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSelectJPQL() throws Exception {
        Mockito.when(etudiantService.selectJPQL(anyString())).thenReturn(Arrays.asList(etudiant));
        mockMvc.perform(get("/etudiant/selectJPQL").param("nom", "Doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomEt").value("Doe"));
    }

    @Test
    void testSelectJPQL_EmptyResult() throws Exception {
        Mockito.when(etudiantService.selectJPQL(anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/etudiant/selectJPQL").param("nom", "Unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
} 