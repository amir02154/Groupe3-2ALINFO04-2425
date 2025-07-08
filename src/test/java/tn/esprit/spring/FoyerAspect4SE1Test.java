package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.AOP.FoyerAspect4SE1;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FoyerAspect4SE1Test {
    @Test
    void testAspectInstantiation() {
        FoyerAspect4SE1 aspect = new FoyerAspect4SE1();
        assertNotNull(aspect);
    }
}
