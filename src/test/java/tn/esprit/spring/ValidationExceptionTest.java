package tn.esprit.spring;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ValidationException ex = new ValidationException("Validation failed");
        assertEquals("Validation failed", ex.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Exception cause = new RuntimeException("Cause");
        ValidationException ex = new ValidationException("Validation failed", cause);
        assertEquals("Validation failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}