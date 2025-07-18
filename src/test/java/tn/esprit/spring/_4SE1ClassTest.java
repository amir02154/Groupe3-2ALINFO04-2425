package tn.esprit.spring;


import org.junit.jupiter.api.Test;

class _4SE1ClassTest {
    @Test
    void testFixedDelayMethod() {
        tn.esprit.spring.Schedular._4SE1ClassTest scheduler = new tn.esprit.spring.Schedular._4SE1ClassTest();
        scheduler.fixedDelayMethod(); // Devrait logger sans erreur
    }

    @Test
    void testFixedRateMethod() {
        tn.esprit.spring.Schedular._4SE1ClassTest scheduler = new tn.esprit.spring.Schedular._4SE1ClassTest();
        scheduler.fixedRateMethod(); // Devrait logger sans erreur
    }
}
