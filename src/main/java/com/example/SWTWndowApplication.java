package com.example;


import com.example.aspectj.EventTableAspect;
import com.example.beans.User;
import org.aspectj.lang.Aspects;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.base.AbstractDateTime;

import java.awt.*;


public class SWTWndowApplication {


    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);

        NewShell composite = new NewShell(shell, SWT.NONE);


        Text helloWorldTest = new Text(composite, SWT.NONE);
        helloWorldTest.setText("Hello World SWT");
        helloWorldTest.pack();

        helloWorldTest.addMouseTrackListener(new MouseTrackListener() {
            @Override
            public void mouseEnter(MouseEvent mouseEvent) {
                System.out.println("ENTER");
            }

            @Override
            public void mouseExit(MouseEvent mouseEvent) {
                System.out.println("EXIT");
            }

            @Override
            public void mouseHover(MouseEvent mouseEvent) {
                System.out.println("HOVER");
            }
        });

        //new SwtSpyView(shell, display.getCursorControl());

//        helloWorldTest.addKeyListener(new KeyAdapter() {
//            public void keyPressed(KeyEvent e) {
//                if (e.stateMask == SWT.ALT && (e.character == 'a')) {
//                    shell.setFullScreen(!shell.getFullScreen());
//                }
//            }
//        });


        User user = new User();
        user.setName("Name");
        user.getName();

        System.out.println("DATE1: "+new DateTime().toString());

        System.out.println("DATE2: "+new AbstractDateTime(){
            @Override
            public long getMillis() {
                return 0;
            }

            @Override
            public Chronology getChronology() {
                return null;
            }
        }.toString());


//        ToolTip tooltip = new ToolTip(helloWorldTest.getShell(), SWT.NONE);
//        tooltip.setText("Esto es una prueba de texto");
//
//        MouseTrackListener mouseTrackListener = new MouseTrackListener() {
//            @Override
//            public void mouseEnter(MouseEvent e) {
//                tooltip.setLocation(Display.getCurrent().getCursorLocation().x,
//                        Display.getCurrent().getCursorLocation().y + 0);
//                tooltip.setVisible(true);
//            }
//
//            @Override
//            public void mouseExit(MouseEvent mouseEvent) {
//
//            }
//
//            @Override
//            public void mouseHover(MouseEvent mouseEvent) {
//                MouseInfo.getPointerInfo().getLocation();
//            }
//        };

        EventTableAspect aspect = Aspects.aspectOf(EventTableAspect.class);
        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
