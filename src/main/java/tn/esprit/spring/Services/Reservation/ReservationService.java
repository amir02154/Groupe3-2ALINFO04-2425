package tn.esprit.spring.Services.Reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
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

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService implements IReservationService {

    ReservationRepository repo;
    ChambreRepository chambreRepository;
    EtudiantRepository etudiantRepository;

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
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
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
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            return LocalDate.of(Integer.parseInt("20" + (year - 1)), 9, 15);
        } else {
            return LocalDate.of(Integer.parseInt("20" + year), 9, 15);
        }
    }

    public LocalDate getDateFinAU() {
        int year = LocalDate.now().getYear() % 100;
        if (LocalDate.now().getMonthValue() <= 7) {
            return LocalDate.of(Integer.parseInt("20" + year), 6, 30);
        } else {
            return LocalDate.of(Integer.parseInt("20" + (year + 1)), 6, 30);
        }
    }
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(Long numChambre, long cin) {
        Chambre chambre = chambreRepository.findByNumeroChambre(numChambre);
        List<Etudiant> etudiants = entityManager.createQuery(
                        "SELECT e FROM Etudiant e WHERE e.cin = :cin", Etudiant.class)
                .setParameter("cin", cin)
                .getResultList();

        if (etudiants.isEmpty()) {
            throw new RuntimeException("Aucun étudiant trouvé avec le CIN : " + cin);
        }

        Etudiant etudiant = etudiants.get(0); // ou traiter autrement si plusieurs


        int nombreReservations = chambreRepository
                .countReservationsByIdChambreAndReservationsAnneeUniversitaireBetween(
                        chambre.getIdChambre(), getDateDebutAU(), getDateFinAU());

        int capaciteMaximale = switch (chambre.getTypeC()) {
            case SIMPLE -> 1;
            case DOUBLE -> 2;
            case TRIPLE -> 3;
        };

        if (nombreReservations >= capaciteMaximale) {
            log.info("Chambre " + chambre.getTypeC() + " remplie !");
            return null;
        }

        String idReservation = getDateDebutAU().getYear() + "/" + getDateFinAU().getYear() + "-" +
                chambre.getBloc().getNomBloc() + "-" + chambre.getNumeroChambre() + "-" + etudiant.getCin();

        Reservation reservation = Reservation.builder()
                .estValide(true)
                .anneeUniversitaire(LocalDate.now())
                .idReservation(idReservation)
                .build();

        reservation.getEtudiants().add(etudiant);
        reservation = repo.save(reservation);

        chambre.getReservations().add(reservation);
        chambreRepository.save(chambre);

        return reservation;
    }

    @Override
    public long getReservationParAnneeUniversitaire(LocalDate debutAnnee, LocalDate finAnnee) {
        return repo.countByAnneeUniversitaireBetween(debutAnnee, finAnnee);
    }

    @Override
    public String annulerReservation(long cinEtudiant) {
        Reservation r = repo.findByEtudiantsCinAndEstValide(cinEtudiant, true);

        if (r == null) {
            throw new EntityNotFoundException("Aucune réservation valide trouvée pour l'étudiant avec le CIN : " + cinEtudiant);
        }

        Chambre c = chambreRepository.findByReservationsIdReservation(r.getIdReservation());

        if (c != null) {
            c.getReservations().remove(r);
            chambreRepository.save(c);
        }

        repo.delete(r);

        return "La réservation " + r.getIdReservation() + " est annulée avec succès";
    }


    @Override
    public void affectReservationAChambre(String idRes, long idChambre) {
        Reservation r = repo.findById(idRes)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        Chambre c = chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));

        c.getReservations().add(r);
        chambreRepository.save(c);
    }

    @Override
    public void deaffectReservationAChambre(String idRes, long idChambre) {
        Reservation r = repo.findById(idRes)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        Chambre c = chambreRepository.findById(idChambre)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvée"));

        c.getReservations().remove(r);
        chambreRepository.save(c);
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
