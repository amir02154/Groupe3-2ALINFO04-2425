package tn.esprit.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.AOP.FoyerAspect4SIM2;
import tn.esprit.spring.DAO.Entities.Foyer;
import tn.esprit.spring.Services.Foyer.FoyerService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FoyerAspect4SIM2Test {

    private FoyerAspect4SIM2 aspect;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private ProceedingJoinPoint proceedingJoinPoint;

    @BeforeEach
    void setUp() {
        aspect = new FoyerAspect4SIM2();
    }

    @Test
    void testAfterAdvice() throws Exception {
        // Arrange
        when(joinPoint.getSignature()).thenReturn(new org.aspectj.lang.Signature() {
            @Override
            public String toShortString() {
                return "findAll()";
            }

            @Override
            public String toLongString() {
                return "public java.util.List tn.esprit.spring.Services.Foyer.FoyerService.findAll()";
            }

            @Override
            public String getName() {
                return "findAll";
            }

            @Override
            public int getModifiers() {
                return 1;
            }

            @Override
            public Class getDeclaringType() {
                return FoyerService.class;
            }

            @Override
            public String getDeclaringTypeName() {
                return "tn.esprit.spring.Services.Foyer.FoyerService";
            }
        });

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> aspect.afterAdvice(joinPoint));
    }

    @Test
    void testBeforeAdvice() throws Exception {
        // Arrange
        when(joinPoint.getSignature()).thenReturn(new org.aspectj.lang.Signature() {
            @Override
            public String toShortString() {
                return "addOrUpdate()";
            }

            @Override
            public String toLongString() {
                return "public tn.esprit.spring.DAO.Entities.Foyer tn.esprit.spring.Services.Foyer.FoyerService.addOrUpdate(tn.esprit.spring.DAO.Entities.Foyer)";
            }

            @Override
            public String getName() {
                return "addOrUpdate";
            }

            @Override
            public int getModifiers() {
                return 1;
            }

            @Override
            public Class getDeclaringType() {
                return FoyerService.class;
            }

            @Override
            public String getDeclaringTypeName() {
                return "tn.esprit.spring.Services.Foyer.FoyerService";
            }
        });

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> aspect.beforeAdvice(joinPoint));
    }

    @Test
    void testBeforeAdvice2() throws Exception {
        // Arrange
        lenient().when(joinPoint.getSignature()).thenReturn(new org.aspectj.lang.Signature() {
            @Override
            public String toShortString() {
                return "ajouterFoyerEtAffecterAUniversite()";
            }

            @Override
            public String toLongString() {
                return "public tn.esprit.spring.DAO.Entities.Foyer tn.esprit.spring.Services.Foyer.FoyerService.ajouterFoyerEtAffecterAUniversite(tn.esprit.spring.DAO.Entities.Foyer, long)";
            }

            @Override
            public String getName() {
                return "ajouterFoyerEtAffecterAUniversite";
            }

            @Override
            public int getModifiers() {
                return 1;
            }

            @Override
            public Class getDeclaringType() {
                return FoyerService.class;
            }

            @Override
            public String getDeclaringTypeName() {
                return "tn.esprit.spring.Services.Foyer.FoyerService";
            }
        });

        // Act & Assert - should not throw exception
        assertDoesNotThrow(() -> aspect.beforeAdvice2(joinPoint));
    }

    @Test
    void testProfileAdvice() throws Throwable {
        // Arrange
        Foyer foyer = new Foyer();
        when(proceedingJoinPoint.proceed()).thenReturn(foyer);

        // Act
        Object result = aspect.profile(proceedingJoinPoint);

        // Assert
        assertNotNull(result);
        assertEquals(foyer, result);
        verify(proceedingJoinPoint).proceed();
    }

    @Test
    void testProfileAdviceWithException() throws Throwable {
        // Arrange
        RuntimeException exception = new RuntimeException("Test exception");
        when(proceedingJoinPoint.proceed()).thenThrow(exception);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> aspect.profile(proceedingJoinPoint));
        verify(proceedingJoinPoint).proceed();
    }
} 