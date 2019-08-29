package com.example.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Widget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Aspect
public class MonitorWidgets {
    private static Logger logger = LoggerFactory.getLogger(MonitorWidgets.class);
    private static final int MOUSE_HOVER = 32;
    private static final int MOUSE_EXIT = 7;

    private Widget lastWidget;
    private ReadWriteLock lock = new ReentrantReadWriteLock();


    @Pointcut("execution(* org.eclipse.swt.widgets.EventTable.sendEvent(org.eclipse.swt.widgets.Event)) && args(event)")
    public void sendEventCut(Event event) {
    }

    @Pointcut("call(org.eclipse.swt.widgets.Control+.new(..))")
    public void newControl() {
    }

    /**
     * Write to logger name class of the widget with mouseover
     *
     * @param joinPoint
     * @param event
     * @return
     * @throws Throwable
     */
    @Around("sendEventCut(event)")
    public Object sendEventAspect(ProceedingJoinPoint joinPoint, Event event) throws Throwable {
        logger.trace("Invoked event: type:{}, character: {}", event.type, event.character);
        lock.writeLock().lock();
        try {
            if (event.type == MOUSE_HOVER) {
                lastWidget = event.widget;
            }
            if (event.type == MOUSE_EXIT) {
                lastWidget = null;
            }
            if ((event.stateMask & (SWT.CTRL | SWT.SHIFT)) == (SWT.CTRL | SWT.SHIFT) && (event.keyCode == 'a')) {
                if (lastWidget != null) {
                    List<String> lParent = getParentHierarchy();
                    StringBuilder tabulatorsSb = new StringBuilder();
                    lParent.stream().sorted(Collections.reverseOrder()).forEach(nameClass -> {
                        logger.debug("####### MOUSE HOVER COMPOSITE #######");
                        logger.debug("{}{}", tabulatorsSb.toString(), nameClass);
                        tabulatorsSb.append("\t");
                    });
                    if (lParent.size() > 1) {
                        tabulatorsSb.append("\t");
                    }
                    logger.debug("{}{} -> string {}", tabulatorsSb.toString(), lastWidget.getClass(), lastWidget.toString());
                    logger.debug("#####################################\n");
                }
            }
            Object ignoredToStringResult = joinPoint.proceed();
            return ignoredToStringResult;
        } finally {
            lock.writeLock().unlock();
        }

    }

    /**
     * Add {@link MouseTrackListener} to Control instances
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("newControl()")
    public Object newControlAround(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.trace("Invoked event: newControlAround");
        Object result = joinPoint.proceed();
        ((Control) result).addMouseTrackListener(new MouseTrackListener() {
            @Override
            public void mouseEnter(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExit(MouseEvent mouseEvent) {
                lock.writeLock().lock();
                try {
                    lastWidget = null;
                } finally {
                    lock.writeLock().unlock();
                }
            }

            @Override
            public void mouseHover(MouseEvent mouseEvent) {
                lock.writeLock().lock();
                try {
                    lastWidget = mouseEvent.widget;
                } finally {
                    lock.writeLock().unlock();
                }
            }
        });
        return result;
    }

    /**
     * Get list with parent hierarchy
     *
     * @return
     */
    private List<String> getParentHierarchy() {
        lock.readLock().lock();
        try {
            List<String> result = new ArrayList<>();
            Control parent = null;
            //Init parent variable
            if (lastWidget instanceof Control && ((Control) lastWidget).getParent() != null) {
                parent = ((Control) lastWidget).getParent();
            }
            while (parent != null) {
                result.add(parent.getClass().toString());
                if (parent.getParent() instanceof Control) {
                    parent = parent.getParent();
                } else {
                    parent = null;
                }
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }
}
