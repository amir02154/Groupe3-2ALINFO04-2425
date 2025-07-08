package tn.esprit.spring.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.DAO.Entities.Reservation;
import tn.esprit.spring.DAO.Entities.Universite;
import tn.esprit.spring.Services.Reservation.IReservationService;
import tn.esprit.spring.Services.Universite.IUniversiteService;

import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("reservation")
@AllArgsConstructor
public class ReservationRestController {
    IReservationService service;

    @PostMapping("addOrUpdate")
    public Reservation addOrUpdate(@RequestBody Reservation r) {
        return service.addOrUpdate(r);
    }

    @GetMapping("findAll")
   public List<Reservation> findAll() {
        return service.findAll();
    }

    
    @GetMapping("findById")
    public Reservation findById(@RequestParam String id) {
        try {
            return service.findById(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("deleteById/{id}")
    public void deleteById(@PathVariable String id) {
        service.deleteById(id);
    }




    @DeleteMapping("delete")
   public void delete(@RequestBody Reservation r) {
        service.delete(r);
    }



    @PostMapping("ajouterReservationEtAssignerAChambreEtAEtudiant")
    public Reservation ajouterReservationEtAssignerAChambreEtAEtudiant(@RequestParam Long numChambre, @RequestParam long cin) {
        return service.ajouterReservationEtAssignerAChambreEtAEtudiant(numChambre, cin);
    }

    @GetMapping("getReservationParAnneeUniversitaire")
    public long getReservationParAnneeUniversitaire(@RequestParam LocalDate debutAnnee, @RequestParam LocalDate finAnnee) {
        return service.getReservationParAnneeUniversitaire(debutAnnee, finAnnee);
    }

    @DeleteMapping("annulerReservation")
    public String annulerReservation(@RequestParam long cinEtudiant) {
        return service.annulerReservation(cinEtudiant);
    }
}
