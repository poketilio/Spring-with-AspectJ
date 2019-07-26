package com.example.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class UserAspect {

    public static final String TO_STRING_RESULT = "test";

    @Pointcut("execution(* com.example.beans.User.getName())")
    public void userGetName() {
    }

    @Around("userGetName()")
    public Object toLowerCase(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ignoredToStringResult = joinPoint.proceed();
        System.out.println("User#getName() has been invoked: " + ignoredToStringResult);
        return TO_STRING_RESULT;
    }
}
