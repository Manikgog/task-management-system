package ru.gogolin.task.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(1)
public class MyAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAspect.class);

    @Before("@annotation(ru.gogolin.task.annotations.LogExecution)")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Calling method -> " + joinPoint.getSignature().getDeclaringTypeName() + " " + joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = "@annotation(ru.gogolin.task.annotations.LogException)")
    public void logAfterThrowing(JoinPoint joinPoint) {
        LOGGER.info("Exception throwing in method: " + joinPoint.getSignature().getDeclaringTypeName() + " "  + joinPoint.getSignature().getName());
    }

}
