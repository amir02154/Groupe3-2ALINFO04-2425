package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.Config.SpringDocConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class SpringDocConfigTest {
    @Test
    void testSpringDocConfigInstantiation() {
        SpringDocConfig config = new SpringDocConfig();
        assertNotNull(config);
    }
}