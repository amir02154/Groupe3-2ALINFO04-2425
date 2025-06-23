package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.EtudiantService;

import static org.junit.jupiter.api.Assertions.*; // Utilisation des assertions JUnit 5

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

        assertNotNull(saved.getIdEtudiant(), "L'ID de l'étudiant ne doit pas être null après sauvegarde");
        assertEquals("Soumaya", saved.getNomEt(), "Le nom de l'étudiant doit être 'Soumaya'");
        assertEquals("Arbi", saved.getPrenomEt(), "Le prénom de l'étudiant doit être 'Arbi'");
    }
}
