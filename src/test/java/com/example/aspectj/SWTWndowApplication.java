package com.example.aspectj;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;


public class SWTWndowApplication {


    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);

        RowLayout layout = new RowLayout();
        layout.wrap = true;
        shell.setLayout(layout);

        new Button(shell, SWT.PUSH).setText("B1");
        new Button(shell, SWT.PUSH).setText("Wide Button 2");
        Button b = new Button(shell, SWT.SELECTED);
        b.setText("Button 3");
        b.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                callMethod();
            }
        });
//        Text helloWorldTest = new Text(shell, SWT.NONE);
//        helloWorldTest.setText("Hello World SWT");
//        helloWorldTest.pack();
//
//        System.out.println("DATE1: "+new DateTime().toString());
//        System.out.println("DATE2: "+new AbstractDateTime(){
//            @Override
//            public long getMillis() {
//                return 0;
//            }
//
//            @Override
//            public Chronology getChronology() {
//                return null;
//            }
//        }.toString());

        shell.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }

    private static void callMethod() {
        System.out.println("TESTSET");
    }
}
