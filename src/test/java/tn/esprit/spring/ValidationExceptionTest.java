package tn.esprit.spring;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {
    @Test
    void testValidationExceptionMessage() {
        ValidationException ex = new ValidationException("Erreur de validation");
        assertEquals("Erreur de validation", ex.getMessage());
    }
}