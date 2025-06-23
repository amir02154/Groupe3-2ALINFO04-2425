package tn.esprit.spring;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.RestControllers.BlocRestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlocRestController.class)
class BlocRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllBlocs() throws Exception {
        mockMvc.perform(get("/bloc/all"))
                .andExpect(status().isOk()); // Suppose que la route existe
    }
}