package tn.esprit.spring;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
public class BlocServiceMockTest {

    @BeforeEach
    void beforeEach() {
        System.out.println("Avant chaque test");
    }

    @AfterEach
    void afterEach() {
        System.out.println("Après chaque test");
    }

    @Order(1)
    @RepeatedTest(4)
    void test() {
        int expected = 4;
        int actual = 2 + 2;
        assertEquals(expected, actual, "2 + 2 doit être égal à 4");
    }

    @Order(4)
    @Test
    void test2() {
        String nomBloc = "Bloc A";
        assertNotNull(nomBloc, "Le nom du bloc ne doit pas être null");
        assertTrue(nomBloc.startsWith("Bloc"), "Le nom du bloc doit commencer par 'Bloc'");
    }

    @Order(2)
    @Test
    void test3() {
        boolean condition = 5 > 2;
        assertTrue(condition, "5 est bien supérieur à 2");
    }

    @Order(3)
    @Test
    void test4() {
        Object obj = new Object();
        assertNotNull(obj, "L’objet ne doit pas être null");
    }
}
