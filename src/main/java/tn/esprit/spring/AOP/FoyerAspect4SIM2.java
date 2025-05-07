package tn.esprit.spring.Aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class FoyerAspect4SIM2 {

    @After("execution(* tn.esprit.spring.Services..*.*(..))")
    public void afterAdvice(JoinPoint jp) {
        log.info("Sortie de la méthode " + jp.getSignature().getName());
    }

    @Before("execution(* tn.esprit.spring.Services..*.*(..))")
    public void beforeAdvice(JoinPoint jp) {
        log.info("Entrée dans la méthode " + jp.getSignature().getName());
    }

    @Before("execution(* tn.esprit.spring.Services..*.ajouter*(..))")
    public void beforeAdvice2(JoinPoint jp) {
        log.info("Méthode ajoutée détectée");
    }

    @Around("execution(* tn.esprit.spring.Services..*.*(..))")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = pjp.proceed();
        long elapsedTime = System.currentTimeMillis() - start;
        log.info("Temps d'exécution de la méthode : " + elapsedTime + " millisecondes.");
        return result;
    }
}
