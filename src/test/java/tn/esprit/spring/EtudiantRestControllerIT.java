package tn.esprit.spring;



import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // démarre tout le contexte Spring avec H2
@ActiveProfiles("test")
@AutoConfigureMockMvc

public class EtudiantRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        etudiantRepository.deleteAll(); // clean base H2 avant chaque test
    }

    @Test
    void testAddOrUpdateEtudiant() throws Exception {
        Etudiant e = Etudiant.builder()
                .nomEt("Ben Salah")
                .prenomEt("Amine")
                .cin(12345678L)
                .ecole("ENIT")
                .build();

        mockMvc.perform(post("/api/etudiants/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(e)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nomEt").value("Ben Salah"));
    }

    @Test
    void testFindAllEtudiants() throws Exception {
        Etudiant e = Etudiant.builder()
                .nomEt("Ayari")
                .prenomEt("Nour")
                .cin(87654321L)
                .ecole("INSAT")
                .build();
        etudiantRepository.save(e);

        mockMvc.perform(get("/api/etudiants/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testFindById() throws Exception {
        Etudiant e = Etudiant.builder()
                .nomEt("Gharbi")
                .prenomEt("Salma")
                .cin(14725836L)
                .ecole("ENSI")
                .build();
        Etudiant saved = etudiantRepository.save(e);

        mockMvc.perform(get("/api/etudiants/findById?id=" + saved.getIdEtudiant()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prenomEt").value("Salma"));
    }

    @Test
    void testDeleteById() throws Exception {
        Etudiant e = Etudiant.builder()
                .nomEt("Bouzid")
                .prenomEt("Omar")
                .cin(11223344L)
                .ecole("ISG")
                .build();
        Etudiant saved = etudiantRepository.save(e);

        mockMvc.perform(delete("/api/etudiants/deleteById?id=" + saved.getIdEtudiant()))
                .andExpect(status().isOk());
    }
}

