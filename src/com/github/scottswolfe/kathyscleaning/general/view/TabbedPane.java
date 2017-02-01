package com.github.scottswolfe.kathyscleaning.general.view;


import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedController;
import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedPersistanceManager;
import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.persistance.Savable;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;


@SuppressWarnings("serial")
public class TabbedPane extends JTabbedPane implements Savable {

    //  FIELDS	
    public int previous_tab;
	Controller controller;
	
	// COMPONENTS
    JScrollPane[] jsp;
    public DayPanel[] day_panel;
    public NW_DayPanel[] nw_day_panel;
    
	// CONSTRUCTOR
    public TabbedPane(Controller controller) {
        this.controller = controller;
    }
    
    public TabbedPane() {
        
    }
    
    // PUBLIC METHODS
    @Override
    public boolean saveToFile() {
        if (nw_day_panel == null) {            
            Data data = ((CompletedController)controller).readUserInput();
            CompletedPersistanceManager.saveToFile(data);
        } else {
            System.out.println("Scheduled Cleaning Testing: saveToFile()");
            // TODO implement this method
        }
        return false;
    }

    @Override
    public boolean loadFromFile() {
        // TODO Auto-generated method stub
        if (nw_day_panel == null) {
            System.out.println("Completed Cleaning Testing: loadFromFile()");
        } else {
            System.out.println("Scheduled Cleaning Testing: loadFromFile()");
        }
        return false;
    }
	    
    
	
    // GETTERS/SETTERS
    public void changePreviousTab(int index){
        this.previous_tab = index;
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
    
}
