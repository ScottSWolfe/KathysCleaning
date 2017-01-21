package com.github.scottswolfe.kathyscleaning.general.view;


import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.persistance.FileManager;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;


@SuppressWarnings("serial")
public class TabbedPane extends JTabbedPane implements FileManager {

    //  FIELDS	
	DayData[] day_data;
	public int previous_tab;
	
	
	// COMPONENTS
    JScrollPane[] jsp;
    public DayPanel[] day_panel;
    public NW_DayPanel[] nw_day_panel;
    
	
    // PUBLIC METHODS
    @Override
    public boolean saveToFile() {
        // TODO Auto-generated method stub
        if (nw_day_panel == null) {
            System.out.println("Completed Cleaning Testing: saveToFile()");
        } else {
            System.out.println("Scheduled Cleaning Testing: saveToFile()");
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
	
    
    // PRIVATE METHODS
    
    
	
    // GETTERS/SETTERS
    public void changePreviousTab(int index){
        this.previous_tab = index;
    }
    
}
