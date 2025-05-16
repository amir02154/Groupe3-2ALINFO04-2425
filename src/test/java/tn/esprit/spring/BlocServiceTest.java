package tn.esprit.spring;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.DAO.Entities.Bloc;
import tn.esprit.spring.DAO.Entities.Chambre;
import tn.esprit.spring.DAO.Repositories.BlocRepository;
import tn.esprit.spring.DAO.Repositories.ChambreRepository;
import tn.esprit.spring.Services.Bloc.BlocService;


import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BlocServiceTest {
    @Mock
    BlocRepository blocRepository;
    @Mock
    ChambreRepository chambreRepository;

    @InjectMocks
    BlocService blocService;
    @Test
    @Order(1)
    public void testAddOrUpdate() {
        Chambre chambre1 = new Chambre();
        chambre1.setNumeroChambre(101L);
        Chambre chambre2 = new Chambre();
        chambre2.setNumeroChambre(102L);

        List<Chambre> chambres = new ArrayList<>();
        chambres.add(chambre1);
        chambres.add(chambre2);
        //Créer un bloc avec ces chambres
        //Le bloc s'appelle "Bloc A" et contient les 2 chambres.
        Bloc bloc = new Bloc();
        bloc.setNomBloc("Bloc A");
        bloc.setChambres(chambres);

        Bloc savedBloc = new Bloc();
        savedBloc.setIdBloc(1L);
        savedBloc.setNomBloc("Bloc A");
        savedBloc.setChambres(chambres);

        //On simule blocRepository.save(...) pour qu’il retourne un Bloc déjà sauvegardé.
        //On simule chambreRepository.save(...) pour qu’il retourne la chambre passée
        // en paramètre (pas de vrai enregistrement, c’est un mock !).

        when(blocRepository.save(any(Bloc.class))).thenReturn(savedBloc);
        when(chambreRepository.save(any(Chambre.class))).thenAnswer(i -> i.getArgument(0));

        // Appeler la méthode testée
        //On appelle la méthode addOrUpdate avec notre bloc simulé.

        Bloc result = blocService.addOrUpdate(bloc);

        // Le bloc retourné ne doit pas être null.
        // Le nom du bloc retourné doit être "Bloc A".
        // On vérifie que la méthode save(...) de chambreRepository a bien été appelée
        // deux fois, une pour chaque chambre.

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Bloc A", result.getNomBloc());
        verify(chambreRepository, times(2)).save(any(Chambre.class));
    }
}