package com.example.aspectj;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UserAspect {
    private static Logger logger = LogManager.getLogger();
    public static final String TO_STRING_RESULT = "test";

    @Pointcut("execution(* com.example.beans.User.getName())")
    public void userGetName() {
    }

    @Around("userGetName()")
    public Object toLowerCase(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ignoredToStringResult = joinPoint.proceed();
        logger.debug("User#getName() has been invoked: " + ignoredToStringResult);
        return TO_STRING_RESULT;
    }
}
