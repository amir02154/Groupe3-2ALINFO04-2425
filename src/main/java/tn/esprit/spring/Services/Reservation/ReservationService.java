package tn.esprit.spring.Services.Reservation;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.DAO.Repositories.EtudiantRepository;
import tn.esprit.spring.DAO.Repositories.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {
    private final ReservationRepository repo;
    private final ChambreRepository chambreRepository;
    private final EtudiantRepository etudiantRepository;

    @Override
    public Reservation addOrUpdate(Reservation r) {
        return repo.save(r);
    }

    @Override
    public List<Reservation> findAll() {
        return repo.findAll();
    }

    @Override
    public Reservation findById(String id) {
        Optional<Reservation> reservation = repo.findById(id);
        if (reservation.isPresent()) {
            return reservation.get();
        } else {
            // Handle the case when reservation is not found
            throw new RuntimeException("Reservation not found");
        }
    }

    @Override
    public void deleteById(String id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Reservation r) {
        repo.delete(r);
    }

    public LocalDate getDateDebutAU() {
        LocalDate dateDebutAU;
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
        }
        return dateDebutAU;
    }

    public LocalDate getDateFinAU() {
        LocalDate dateFinAU;
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
        return dateFinAU;
    }

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Long numChambre, long cin) {
        // Récupération de la chambre et de l'étudiant
        Optional<Chambre> chambreOpt = chambreRepository.findByNumeroChambre(numChambre);
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByCin(cin);

        if (chambreOpt.isPresent() && etudiantOpt.isPresent()) {
            Chambre chambre = chambreOpt.get();
            Etudiant etudiant = etudiantOpt.get();

            // Compter le nombre de réservations existantes
            int nombreReservations = chambreRepository
                    .countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                            chambre.getIdChambre(), getDateDebutAU(), getDateFinAU());

            // Vérification de la capacité de la chambre
            boolean ajout = false;
            int capaciteMaximale = switch (chambre.getTypeC()) {
                case SIMPLE -> 1;
                case DOUBLE -> 2;
                case TRIPLE -> 3;
            };
            if (nombreReservations < capaciteMaximale) {
                ajout = true;
            } else {
                log.info("Chambre " + chambre.getTypeC() + " remplie !");
            }

            if (ajout) {
                // Création de la réservation
                String idReservation = "" + getDateDebutAU().getYear() + "/" + getDateFinAU().getYear() + "-"
                        + chambre.getBloc().getNomBloc() + "-" + chambre.getNumeroChambre() + "-" + etudiant.getCin();
                Reservation reservation = Reservation.builder()
                        .estValide(true)
                        .anneeUniversitaire(LocalDate.now())
                        .idReservation(idReservation)
                        .build();

                // Affectation de l'étudiant à la réservation
                reservation.getEtudiants().add(etudiant);

                // Sauvegarde de la réservation
                reservation = repo.save(reservation);

                // Affectation de la réservation à la chambre
                chambre.getReservations().add(reservation);
                chambreRepository.save(chambre);

                return reservation;
            }
        }
        return null; // Retourner null si la chambre est pleine ou les entités non trouvées
    }

    @Override
    public long getReservationParAnneeUniversitaire(LocalDate debutAnnee, LocalDate finAnnee) {
        return repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Override
    public String annulerReservation(long cinEtudiant) {
        Optional<Reservation> reservationOpt = repo.findByEtudiantsCinAndEstValide(cinEtudiant, true);

        if (reservationOpt.isPresent()) {
            Reservation r = reservationOpt.get();
            Optional<Chambre> chambreOpt = chambreRepository.findByReservationsIdReservation(r.getIdReservation());

            if (chambreOpt.isPresent()) {
                Chambre c = chambreOpt.get();
                c.getReservations().remove(r);
                chambreRepository.save(c);
                repo.delete(r);

                return "La réservation " + r.getIdReservation() + " est annulée avec succès";
            }
        }
        return "Réservation non trouvée ou déjà annulée.";
    }

    @Override
    public void affectReservationAChambre(String idRes, long idChambre) {
        Optional<Reservation> rOpt = repo.findById(idRes);
        Optional<Chambre> cOpt = chambreRepository.findById(idChambre);

        if (rOpt.isPresent() && cOpt.isPresent()) {
            Reservation r = rOpt.get();
            Chambre c = cOpt.get();

            // Parent: Chambre, Child: Reservation
            c.getReservations().add(r);
            chambreRepository.save(c);
        } else {
            // Handle the case when either reservation or chambre is not found
            throw new RuntimeException("Reservation or Chambre not found");
        }
    }

    @Override
    public void deaffectReservationAChambre(String idRes, long idChambre) {
        Optional<Reservation> rOpt = repo.findById(idRes);
        Optional<Chambre> cOpt = chambreRepository.findById(idChambre);

        if (rOpt.isPresent() && cOpt.isPresent()) {
            Reservation r = rOpt.get();
            Chambre c = cOpt.get();

            // Parent: Chambre, Child: Reservation
            c.getReservations().remove(r);
            chambreRepository.save(c);
        } else {
            // Handle the case when either reservation or chambre is not found
            throw new RuntimeException("Reservation or Chambre not found");
        }
    }

    @Override
    public void annulerReservations() {
        LocalDate dateDebutAU;
        LocalDate dateFinAU;
        int year = LocalDate.now().getYear() % 100;

        if (LocalDate.now().getMonthValue() <= 7) {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            dateDebutAU = LocalDate.of(Integer.parseInt("20" + year), 9, 15);
            dateFinAU = LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }

        for (Reservation reservation : repo.findByEstValideAndAnneeUniversitaireBetween(true, dateDebutAU, dateFinAU)) {
            reservation.setEstValide(false);
            repo.save(reservation);
            log.info("La réservation " + reservation.getIdReservation() + " est annulée automatiquement");
        }
    }
}
