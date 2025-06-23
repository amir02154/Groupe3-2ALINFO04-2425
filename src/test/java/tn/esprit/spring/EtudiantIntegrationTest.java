package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
@SpringBootTest
class EtudiantServiceTest {

    @Autowired
    private EtudiantService etudiantService;

    @Test
    void testAddEtudiant() {
        Etudiant et = Etudiant.builder()
                .nomEt("Soumaya")
                .prenomEt("Arbi")
                .build();

        Etudiant saved = etudiantService.addOrUpdate(et);

        assertNotNull(saved.getIdEtudiant());
        assertEquals("Soumaya", saved.getNomEt());
    }
}
