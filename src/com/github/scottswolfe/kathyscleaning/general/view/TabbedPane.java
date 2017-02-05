package com.github.scottswolfe.kathyscleaning.general.view;


import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;


@SuppressWarnings("serial")
public class TabbedPane extends JTabbedPane {

    //  FIELDS	
    public int previous_tab;
	Controller controller;
	
	
	// COMPONENTS
    JScrollPane[] jsp;
    public DayPanel[] day_panel;
    public NW_DayPanel[] nw_day_panel;
    
    
	// CONSTRUCTORS
    public TabbedPane(Controller controller) {
        this.controller = controller;
    }
    
    public TabbedPane() {
        
    }
    
    
    // GETTERS/SETTERS
    public void changePreviousTab(int index){
        this.previous_tab = index;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
    
}
