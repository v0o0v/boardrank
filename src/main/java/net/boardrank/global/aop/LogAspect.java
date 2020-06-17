package net.boardrank.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around("execution(public * net.boardrank..*Service.*(..))")
    public Object controllerLogging(ProceedingJoinPoint point) throws Throwable {
        boolean show = true;
        if (point.getSignature().getDeclaringTypeName().contains("org.springframework")) show = false;

        if (show)
            log.debug("start - " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "()");

        Object result = point.proceed();

        if (show)
            log.debug("finished - " + point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName() + "()");

        return result;
    }
}
