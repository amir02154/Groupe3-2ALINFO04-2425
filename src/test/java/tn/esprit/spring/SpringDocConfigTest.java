package tn.esprit.spring;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import tn.esprit.spring.Config.SpringDocConfig;

import static org.junit.jupiter.api.Assertions.*;

public class SpringDocConfigTest {

    @Test
    public void testOpenAPIBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringDocConfig.class);
        OpenAPI openAPI = context.getBean(OpenAPI.class);
        assertNotNull(openAPI);
        context.close();
    }
}