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
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.DAO.Repositories.*;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ReservationRestControllerIT {
    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BlocRepository blocRepository;
@Autowired
private FoyerRepository foyerRepository;
    @BeforeEach
    void setup() {
        reservationRepository.deleteAll();
    }

    @Test
    void testAddOrUpdateAndFindById() throws Exception {
        Reservation res = Reservation.builder()
                // Ne pas fixer idReservation
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .build();

        // Ajout
        String response = mockMvc.perform(post("/api/reservations/addOrUpdate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(res)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        // Extraire l'ID généré
        String generatedId = objectMapper.readTree(response).get("idReservation").asText();

        // Rechercher par id avec l'ID récupéré
        mockMvc.perform(get("/api/reservations/findById")
                        .param("id", generatedId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idReservation").value(generatedId));
    }

    @Test
    void testFindAll() throws Exception {
        Reservation res1 = Reservation.builder()
                .idReservation("res1")
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .build();
        Reservation res2 = Reservation.builder()
                .idReservation("res2")
                .anneeUniversitaire(LocalDate.of(2023, 9, 1))
                .estValide(true)
                .build();

        reservationRepository.save(res1);
        reservationRepository.save(res2);

        mockMvc.perform(get("/api/reservations/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)));
    }



    @Test
    void testDeleteByEntity() throws Exception {
        // Créer un bloc, une chambre, un étudiant
        Bloc bloc = blocRepository.save(Bloc.builder().nomBloc("B").capaciteBloc(10).build());
        Chambre chambre = chambreRepository.save(Chambre.builder().numeroChambre(2).typeC(TypeChambre.SIMPLE).bloc(bloc).build());
        Etudiant etudiant = etudiantRepository.save(Etudiant.builder().cin(11111111L).nomEt("Test").prenomEt("Etud").ecole("ESPRIT").build());

        // Créer une réservation
        Reservation reservation = Reservation.builder()
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .chambre(chambre)
                .etudiant(etudiant)
                .build();

        reservation = reservationRepository.save(reservation); // ⚠️ Important

        // Appel de l'API de suppression avec l'ID de la réservation
        mockMvc.perform(delete("/api/reservations/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isOk());

    }

    // Pour les méthodes nécessitant une base ou logique métier plus complète,
    // tu peux mocker ou ajouter plus tard.
    // Exemples d’appel sans validation des retours :

    @Test


    void testAjouterReservationEtAssignerAChambreEtAEtudiant() throws Exception {
        // 0. Créer et sauvegarder un foyer (sinon Bloc sera associé à un foyer non persisté)
        Foyer foyer = Foyer.builder()
                .nomFoyer("Foyer Test")
                .capaciteFoyer(500)
                .build();
        foyer = foyerRepository.save(foyer);

        // 1. Créer et sauvegarder un bloc
        Bloc bloc = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(100)
                .foyer(foyer) // très important !!
                .build();
        bloc = blocRepository.save(bloc);

        // 2. Créer et sauvegarder une chambre avec le bloc persisté
        Chambre chambre = Chambre.builder()
                .numeroChambre(1)
                .typeC(TypeChambre.SIMPLE)
                .bloc(bloc)
                .build();
        chambreRepository.save(chambre);

        // 3. Créer et sauvegarder un étudiant
        Etudiant etudiant = Etudiant.builder()
                .cin(12345678L)
                .nomEt("Test")
                .prenomEt("User")
                .ecole("ESPRIT")
                .build();
        etudiantRepository.save(etudiant);

        // 4. Appeler l'API REST
        mockMvc.perform(post("/api/reservations/ajouterReservationEtAssignerAChambreEtAEtudiant")
                        .param("numChambre", "1")
                        .param("cin", "12345678"))
                .andExpect(status().isOk());
    }



    @Test
    void testGetReservationParAnneeUniversitaire() throws Exception {
        mockMvc.perform(get("/api/reservations/getReservationParAnneeUniversitaire")
                        .param("debutAnnee", "2023-09-01")
                        .param("finAnnee", "2024-08-31"))
                .andExpect(status().isOk());
    }

    @Test
    void testAnnulerReservation() throws Exception {
        // 1. Créer un étudiant
        Etudiant etudiant = Etudiant.builder()
                .cin(12345678L)
                .nomEt("Nom")
                .prenomEt("Prénom")
                .ecole("ESPRIT")
                .build();
        etudiantRepository.save(etudiant);

        // 2. Créer une chambre
        Chambre chambre = Chambre.builder()
                .numeroChambre(101)
                .typeC(TypeChambre.SIMPLE)
                .build();
        chambreRepository.save(chambre);

        // 3. Créer une réservation et la lier à l’étudiant et à la chambre
        Reservation reservation = Reservation.builder()
                .anneeUniversitaire(LocalDate.of(2024, 9, 1))
                .estValide(true)
                .chambre(chambre)
                .build();
        reservation.getEtudiants().add(etudiant);
        reservationRepository.save(reservation);

        // 4. Lier aussi la réservation à la chambre
        chambre.getReservations().add(reservation);
        chambreRepository.save(chambre);

        // 5. Appel API
        mockMvc.perform(delete("/api/reservations/annulerReservation")
                        .param("cinEtudiant", "12345678"))
                .andExpect(status().isOk());
    }

}