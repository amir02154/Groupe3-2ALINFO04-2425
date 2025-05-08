package tn.esprit.spring.Services.Chambre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.TypeChambre;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {

    private final ChambreRepository chambreRepository;
    private final BlocRepository blocRepository;

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return chambreRepository.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return chambreRepository.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return chambreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre not found with id: " + id));
    }

    @Override
    public void deleteById(long id) {
        chambreRepository.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        chambreRepository.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return chambreRepository.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        return chambreRepository.findAll().stream()
                .filter(c -> c.getBloc().getIdBloc() == idBloc && c.getTypeC().equals(type))
                .count();
    }

    @Override
    public List<Chambre> getChambresNonReserveParNomFoyerEtTypeChambre(String nomFoyer, TypeChambre type) {
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

        List<Chambre> listChambreDispo = new ArrayList<>();
        for (Chambre c : chambreRepository.findAll()) {
            if (c.getTypeC().equals(type) && c.getBloc().getFoyer().getNomFoyer().equals(nomFoyer)) {
                long countAU = c.getReservations().stream()
                        .filter(r -> r.getAnneeUniversitaire().isAfter(dateDebutAU)
                                && r.getAnneeUniversitaire().isBefore(dateFinAU))
                        .count();

                if ((type == TypeChambre.SIMPLE && countAU == 0)
                        || (type == TypeChambre.DOUBLE && countAU < 2)
                        || (type == TypeChambre.TRIPLE && countAU < 3)) {
                    listChambreDispo.add(c);
                }
            }
        }
        return listChambreDispo;
    }

    @Scheduled(cron = "0 * * * * *")
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() + " ayant une capacité " + b.getCapaciteBloc());
            if (!b.getChambres().isEmpty()) {
                log.info("La liste des chambres pour ce bloc :");
                for (Chambre c : b.getChambres()) {
                    log.info("NumChambre: " + c.getNumeroChambre() + ", Type: " + c.getTypeC());
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long total = chambreRepository.count();
        if (total == 0) {
            log.warn("Aucune chambre trouvée.");
            return;
        }

        double pSimple = (double) chambreRepository.countChambreByTypeC(TypeChambre.SIMPLE) * 100 / total;
        double pDouble = (double) chambreRepository.countChambreByTypeC(TypeChambre.DOUBLE) * 100 / total;
        double pTriple = (double) chambreRepository.countChambreByTypeC(TypeChambre.TRIPLE) * 100 / total;

        log.info("Total chambres: " + total);
        log.info("SIMPLE: " + pSimple + "%");
        log.info("DOUBLE: " + pDouble + "%");
        log.info("TRIPLE: " + pTriple + "%");
    }

    @Override
    public void nbPlacesDisponibleParChambreAnneeEnCours() {
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

        for (Chambre c : chambreRepository.findAll()) {
            long nbReservation = chambreRepository.countReservationsByIdChambreAndReservationsEstValideAndReservationsAnneeUniversitaireBetween(
                    c.getIdChambre(), true, dateDebutAU, dateFinAU);

            int placesDispo = 0;
            switch (c.getTypeC()) {
                case SIMPLE -> placesDispo = 1 - (int) nbReservation;
                case DOUBLE -> placesDispo = 2 - (int) nbReservation;
                case TRIPLE -> placesDispo = 3 - (int) nbReservation;
            }

            if (placesDispo > 0) {
                log.info("Chambre " + c.getNumeroChambre() + " (" + c.getTypeC() + ") : " + placesDispo + " places disponibles");
            } else {
                log.info("Chambre " + c.getNumeroChambre() + " (" + c.getTypeC() + ") : complète");
            }
        }
    }

    @Override
    public List<Chambre> getChambresParNomBlocJava(String nomBloc) {
        Bloc b = blocRepository.findByNomBloc(nomBloc);
        return b != null ? b.getChambres() : new ArrayList<>();
    }

    @Override
    public List<Chambre> getChambresParNomBlocKeyWord(String nomBloc) {
        return chambreRepository.findByBlocNomBloc(nomBloc);
    }

    @Override
    public List<Chambre> getChambresParNomBlocJPQL(String nomBloc) {
        return chambreRepository.getChambresParNomBlocJPQL(nomBloc);
    }

    @Override
    public List<Chambre> getChambresParNomBlocSQL(String nomBloc) {
        return chambreRepository.getChambresParNomBlocSQL(nomBloc);
    }
}
