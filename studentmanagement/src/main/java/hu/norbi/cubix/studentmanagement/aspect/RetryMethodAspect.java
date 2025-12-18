package hu.norbi.cubix.studentmanagement.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class RetryMethodAspect {

    @Pointcut("@annotation(hu.norbi.cubix.studentmanagement.aspect.RetryMethod)")
    public void annotationRetryMethod() {}

    @Around("annotationRetryMethod() && @annotation(retryMethod)")
    public Object retryMethods(ProceedingJoinPoint pjp, RetryMethod retryMethod) throws Throwable {

        int attempts = retryMethod.attempts();
        long delay = retryMethod.delay();

        for (int i = 1; i <= attempts; i++) {
            try {
                System.out.println("Attempt " + i + "/" + attempts + " for method: " + pjp.getSignature().getName());
                return pjp.proceed();
            } catch (RuntimeException e) {
                System.out.println("Attempt " + i + " failed: " + e.getMessage());
                if (i == attempts) {
                    System.out.println("All " + attempts + " attempts failed for method: " + pjp.getSignature().getName());
                    throw e;
                }
                Thread.sleep(delay);
            }
        }
        return null;


    }


}
