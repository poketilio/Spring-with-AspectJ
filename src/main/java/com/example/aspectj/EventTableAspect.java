package com.example.aspectj;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;

@Aspect
public class EventTableAspect {
    private static Logger logger = LogManager.getLogger();
    public static final String TO_STRING_RESULT = "test";
    private static Widget lastWidget;

    public EventTableAspect(){
        System.out.println("a");
    }

    @Pointcut("execution(* org.eclipse.swt.widgets.EventTable.sendEvent(org.eclipse.swt.widgets.Event)) && args(event)")
    public void sendEventCut(Event event) {
    }

    @Around("sendEventCut(event)")
    public Object sendEventAspect(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        logger.debug("Invoked event: type:{}, character: {}", event.type, event.character);
        if (event.type ==1){
            System.out.println("a");
        }
        if ((event.stateMask & (SWT.CTRL | SWT.SHIFT )) == (SWT.CTRL | SWT.SHIFT ) && (event.keyCode =='a')){
            lastWidget = event.widget;
            logger.debug("Last Widget: {}", lastWidget);
        }
        Object ignoredToStringResult = joinPoint.proceed();
        return ignoredToStringResult;
    }
}
