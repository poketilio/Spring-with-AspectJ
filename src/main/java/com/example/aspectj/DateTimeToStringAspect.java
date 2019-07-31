package com.example.aspectj;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DateTimeToStringAspect {
    private static Logger logger = LogManager.getLogger();
    public static final String TO_STRING_RESULT = "test";

//    @Pointcut("execution(* org.joda.time.base.AbstractDateTime.toString())")
//    public void dateTimeToString() {
//    }

    @Pointcut("execution(* toString()) && this(org.joda.time.DateTime)")
    public void dateTime1ToString() {
    }

//    @Around("dateTimeToString()")
//    public Object toLowerCase(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object ignoredToStringResult = joinPoint.proceed();
//        logger.debug("DateTime#toString() has been invoked: " + ignoredToStringResult);
//        return TO_STRING_RESULT;
//    }

    @Around("dateTime1ToString()")
    public Object toLowerCase1(ProceedingJoinPoint joinPoint) throws Throwable {
        Object ignoredToStringResult = joinPoint.proceed();
        logger.debug("----->>DateTime11#toString() has been invoked: " + ignoredToStringResult);
        return TO_STRING_RESULT;
    }
}
