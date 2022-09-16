package com.github.scottswolfe.kathyscleaning.general.view;

import javax.swing.JTabbedPane;

public class TabbedPane extends JTabbedPane {

    public int previous_tab;

    public TabbedPane() {}

    public void changePreviousTab(int index){
        this.previous_tab = index;
    }
}
