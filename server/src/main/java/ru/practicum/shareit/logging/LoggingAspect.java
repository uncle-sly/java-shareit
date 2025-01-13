package ru.practicum.shareit.logging;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(public * ru.practicum.shareit.user.UserController.*(..)) || " +
              "execution(public * ru.practicum.shareit.item.ItemController.*(..))")
    private void publicMethodsFromUserPackage() {
    }

    @Before(value = "publicMethodsFromUserPackage()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info(">> {}() - {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "publicMethodsFromUserPackage()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("<< {}() - {}", methodName, result);
    }

    @AfterThrowing(pointcut = "publicMethodsFromUserPackage()", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        log.error("<< {}() - {}", methodName, exception.getMessage());
    }

}
