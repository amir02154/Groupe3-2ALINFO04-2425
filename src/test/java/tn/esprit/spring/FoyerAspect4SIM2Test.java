package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.AOP.FoyerAspect4SIM2;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FoyerAspect4SIM2Test {
    @Test
    void testAspectInstantiation() {
        FoyerAspect4SIM2 aspect = new FoyerAspect4SIM2();
        assertNotNull(aspect);
    }
} 