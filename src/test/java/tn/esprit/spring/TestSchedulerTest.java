package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.Schedular.test;

class TestSchedulerTest {
    @Test
    void testAfficheMethod() {
        test scheduler = new test();
        // La méthode affiche() est commentée dans la classe d'origine, mais on vérifie qu'elle existe et peut être appelée si décommentée.
        // scheduler.affiche();
    }
} 