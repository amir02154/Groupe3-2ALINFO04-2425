package tn.esprit.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tn.esprit.spring.AOP.FoyerAspect4SIM2;

import static org.mockito.Mockito.*;

class FoyerAspect4SIM2Test {

    @Test
    void testBeforeAdvice() {
        JoinPoint jp = mock(JoinPoint.class);
        Signature sig = mock(Signature.class);
        when(jp.getSignature()).thenReturn(sig);
        when(sig.getName()).thenReturn("maMethode");

        FoyerAspect4SIM2 aspect = new FoyerAspect4SIM2();
        aspect.beforeAdvice(jp);
        // Pas d'assertion possible ici, on vérifie juste que ça ne plante pas
    }

    @Test
    void testAfterAdvice() {
        JoinPoint jp = mock(JoinPoint.class);
        Signature sig = mock(Signature.class);
        when(jp.getSignature()).thenReturn(sig);
        when(sig.getName()).thenReturn("maMethode");

        FoyerAspect4SIM2 aspect = new FoyerAspect4SIM2();
        aspect.afterAdvice(jp);
    }

    @Test
    void testBeforeAdvice2() {
        JoinPoint jp = mock(JoinPoint.class);

        FoyerAspect4SIM2 aspect = new FoyerAspect4SIM2();
        aspect.beforeAdvice2(jp);
    }

    @Test
    void testProfile() throws Throwable {
        ProceedingJoinPoint pjp = mock(ProceedingJoinPoint.class);
        when(pjp.proceed()).thenReturn("result");

        FoyerAspect4SIM2 aspect = new FoyerAspect4SIM2();
        Object result = aspect.profile(pjp);

        assert(result.equals("result"));
    }
}
