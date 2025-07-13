package tn.esprit.spring.Config;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import tn.esprit.spring.DAO.Entities.*;
import tn.esprit.spring.Services.Universite.IUniversiteService;
import tn.esprit.spring.Services.Foyer.IFoyerService;
import tn.esprit.spring.Services.Bloc.IBlocService;
import tn.esprit.spring.Services.Chambre.IChambreService;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;
import tn.esprit.spring.Services.Reservation.IReservationService;

import java.time.LocalDate;
import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private IUniversiteService universiteService;
    private IFoyerService foyerService;
    private IBlocService blocService;
    private IChambreService chambreService;
    private IEtudiantService etudiantService;
    private IReservationService reservationService;

    @Override
    public void run(String... args) throws Exception {
        try {
            // Initialiser les données seulement si la base est vide
            if (universiteService.findAll().isEmpty()) {
                System.out.println("🚀 Initialisation des données de test...");
                initializeTestData();
            } else {
                System.out.println("✅ Données déjà présentes dans la base");
            }
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de l'initialisation des données: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeTestData() {
        try {
            // Créer des universités (sans foyer d'abord)
            Universite universite1 = Universite.builder()
                    .nomUniversite("ESPRIT")
                    .adresse("Ariana")
                    .build();
            universite1 = universiteService.addOrUpdate(universite1);

            Universite universite2 = Universite.builder()
                    .nomUniversite("INSAT")
                    .adresse("Tunis")
                    .build();
            universite2 = universiteService.addOrUpdate(universite2);

            // Créer des foyers (sans université d'abord)
            Foyer foyer1 = Foyer.builder()
                    .nomFoyer("Foyer ESPRIT")
                    .capaciteFoyer(100)
                    .build();
            foyer1 = foyerService.addOrUpdate(foyer1);

            Foyer foyer2 = Foyer.builder()
                    .nomFoyer("Foyer INSAT")
                    .capaciteFoyer(80)
                    .build();
            foyer2 = foyerService.addOrUpdate(foyer2);

            // Maintenant associer les foyers aux universités
            universite1.setFoyer(foyer1);
            universiteService.addOrUpdate(universite1);

            universite2.setFoyer(foyer2);
            universiteService.addOrUpdate(universite2);

            // Créer des blocs
            Bloc bloc1 = Bloc.builder()
                    .nomBloc("Bloc A")
                    .capaciteBloc(50)
                    .foyer(foyer1)
                    .build();
            bloc1 = blocService.addOrUpdate(bloc1);

            Bloc bloc2 = Bloc.builder()
                    .nomBloc("Bloc B")
                    .capaciteBloc(50)
                    .foyer(foyer1)
                    .build();
            bloc2 = blocService.addOrUpdate(bloc2);

            // Créer des chambres
            for (int i = 1; i <= 10; i++) {
                Chambre chambre = Chambre.builder()
                        .numeroChambre(i)
                        .typeC(TypeChambre.SIMPLE)
                        .bloc(bloc1)
                        .build();
                chambreService.addOrUpdate(chambre);
            }

            for (int i = 11; i <= 20; i++) {
                Chambre chambre = Chambre.builder()
                        .numeroChambre(i)
                        .typeC(TypeChambre.DOUBLE)
                        .bloc(bloc2)
                        .build();
                chambreService.addOrUpdate(chambre);
            }

            // Créer des étudiants
            for (int i = 1; i <= 15; i++) {
                Etudiant etudiant = Etudiant.builder()
                        .nomEt("Etudiant" + i)
                        .prenomEt("Prenom" + i)
                        .cin(10000000L + i)
                        .ecole("ESPRIT")
                        .dateNaissance(LocalDate.of(2000, 1, 1))
                        .build();
                etudiantService.addOrUpdate(etudiant);
            }

            // Créer des réservations
            List<Etudiant> etudiants = etudiantService.findAll();
            List<Chambre> chambres = chambreService.findAll();

            for (int i = 0; i < Math.min(etudiants.size(), chambres.size()); i++) {
                Reservation reservation = Reservation.builder()
                        .idReservation("RES" + String.format("%03d", i + 1))
                        .anneeUniversitaire(LocalDate.of(2024, 1, 1))
                        .estValide(true)
                        .etudiant(etudiants.get(i))
                        .chambre(chambres.get(i))
                        .build();
                reservationService.addOrUpdate(reservation);
            }

            System.out.println("✅ Données de test initialisées avec succès !");
            System.out.println("📊 Statistiques :");
            System.out.println("   - Universités : " + universiteService.findAll().size());
            System.out.println("   - Foyers : " + foyerService.findAll().size());
            System.out.println("   - Blocs : " + blocService.findAll().size());
            System.out.println("   - Chambres : " + chambreService.findAll().size());
            System.out.println("   - Étudiants : " + etudiantService.findAll().size());
            System.out.println("   - Réservations : " + reservationService.findAll().size());
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création des données: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 