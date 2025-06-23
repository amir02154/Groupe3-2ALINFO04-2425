package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.RestControllers.ChambreRestController;
import tn.esprit.spring.Services.Chambre.ChambreService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChambreRestController.class)
class ChambreControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChambreService chambreService;

    @Test
    public void testGetAllChambres() throws Exception {
        mockMvc.perform(get("/chambre/findAll"))
                .andExpect(status().isOk()); // vérifie que le statut HTTP est 200
    }
}

