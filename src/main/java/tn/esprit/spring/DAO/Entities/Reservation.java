package tn.esprit.spring.DAO.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "T_RESERVATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id_reservation", updatable = false, nullable = false)
    String idReservation;

    LocalDate anneeUniversitaire;
    boolean estValide;
    @ManyToOne
    @JoinColumn(name = "id_chambre")  // FK en base
    Chambre chambre;
    @ManyToMany
    @JsonIgnore
    @Builder.Default

    private List<Etudiant> etudiants = new ArrayList<>();
    @ManyToOne
    Etudiant etudiant;


}
