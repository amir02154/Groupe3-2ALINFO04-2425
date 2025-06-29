package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.spring.Schedular.Schedular;
import tn.esprit.spring.Services.Chambre.IChambreService;
import tn.esprit.spring.Services.Reservation.IReservationService;

import static org.mockito.Mockito.*;

public class SchedularTest {

    private IChambreService chambreService;
    private IReservationService reservationService;
    private Schedular schedular;

    @BeforeEach
    void setup() {
        chambreService = mock(IChambreService.class);
        reservationService = mock(IReservationService.class);
        schedular = new Schedular(chambreService, reservationService);
    }

    @Test
    void testService1() {
        schedular.service1();
        verify(chambreService).listeChambresParBloc();
    }}