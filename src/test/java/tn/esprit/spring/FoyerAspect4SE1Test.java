package tn.esprit.spring;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tn.esprit.spring.AOP.FoyerAspect4SE1;

class FoyerAspect4SE1Test {

    FoyerAspect4SE1 aspect = new FoyerAspect4SE1();

    @Test
    void testHello() {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);

        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn("testMethod");

        aspect.hello(joinPoint);
    }

    @Test
    void testBye() {
        JoinPoint joinPoint = Mockito.mock(JoinPoint.class);
        Signature signature = Mockito.mock(Signature.class);

        Mockito.when(joinPoint.getSignature()).thenReturn(signature);
        Mockito.when(signature.getName()).thenReturn("testMethod");

        aspect.bye(joinPoint);
    }

    @Test
    void testExecution() throws Throwable {
        ProceedingJoinPoint pjp = Mockito.mock(ProceedingJoinPoint.class);
        Mockito.when(pjp.proceed()).thenReturn("testResult");

        Object result = aspect.execution(pjp);

        assert result.equals("testResult");
    }
}
