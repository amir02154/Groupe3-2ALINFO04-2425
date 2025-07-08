package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.Schedular._4SE1ClassTest;

class _4SE1ClassTest {
    @Test
    void testFixedDelayMethod() {
        _4SE1ClassTest scheduler = new _4SE1ClassTest();
        scheduler.fixedDelayMethod(); // Devrait logger sans erreur
    }

    @Test
    void testFixedRateMethod() {
        _4SE1ClassTest scheduler = new _4SE1ClassTest();
        scheduler.fixedRateMethod(); // Devrait logger sans erreur
    }
}
