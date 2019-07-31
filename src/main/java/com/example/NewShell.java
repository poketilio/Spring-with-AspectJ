package com.example;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NewShell extends Composite {

    public NewShell(Composite parent, int style) {
        super(parent, style);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns= 2;
        setLayout(gridLayout);
    }
}
