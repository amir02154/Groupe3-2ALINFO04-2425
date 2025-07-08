package tn.esprit.spring.RestControllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.DAO.Entities.Etudiant;
import tn.esprit.spring.Services.Etudiant.IEtudiantService;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("etudiant")
@AllArgsConstructor
public class EtudiantRestController {
    IEtudiantService service;

    @PostMapping("addOrUpdate")
   public Etudiant addOrUpdate(@RequestBody Etudiant e) {
        return service.addOrUpdate(e);
    }

    @GetMapping("findAll")
    public List<Etudiant> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    public Etudiant findById(@RequestParam long id) {
        try {
            return service.findById(id);
        } catch (RuntimeException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @DeleteMapping("delete")
    public void delete(@RequestBody Etudiant e) {
        service.delete(e);
    }

    @DeleteMapping("deleteById")
    public void deleteById(@RequestParam long id) {
        service.deleteById(id);
    }

    @GetMapping("selectJPQL")
   public List<Etudiant> selectJPQL(@RequestParam String nom){
        return service.selectJPQL(nom);
    }
}
