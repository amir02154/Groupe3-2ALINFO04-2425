package tn.esprit.spring.DAO.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "T_FOYER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Foyer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    long idFoyer;

    String nomFoyer;
    long capaciteFoyer;

    @OneToOne(mappedBy = "foyer")
    Universite universite;

    @JsonIgnore
    @OneToMany(mappedBy = "foyer")
    @Builder.Default
    private List<Bloc> blocs = new ArrayList<>();
}