package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;


public class NW_AddHouseListener implements ActionListener {

//  FIELDS

    NW_DayPanel day_panel;
    NW_HousePanel house_panel;
    WorkerList dwd;
    JFrame frame;

    public static final int MAX_HOUSE_PANELS = 6;


//  CONSTRUCTOR

    public NW_AddHouseListener(NW_DayPanel day_panel, NW_HousePanel house_panel, WorkerList dwd, JFrame frame) {
        this.day_panel = day_panel;
        this.house_panel = house_panel;
        this.dwd = dwd;
        this.frame = frame;
    }



//  LISTENER

    public void actionPerformed(ActionEvent e){

        if (day_panel.getNumHousePanels() >= MAX_HOUSE_PANELS) {
            return;
        }

        // getting data for resizing at end of method
        int panel_height = day_panel.house_panels.get(0).getHeight() + DayPanel.PANEL_PADDING;

        // initializing variables
        int num = day_panel.getNumHousePanels();
        int index = -1;
        for(int i = 0; i < num; i++) {
            if (day_panel.house_panels.get(i) == house_panel){
                index = i;
            }
        }

        //remove old panels scroll pane
        for(int i = 0; i < num; i++) {
            day_panel.jsp_panel.remove(day_panel.house_panels.get(i));
        }

        // add new house panel
        day_panel.house_panels.add(index + 1, new NW_HousePanel(day_panel.header_panel.getWorkerList(), day_panel, frame));

        // reset focus listeners
        day_panel.addFlexibleFocusListeners();

        // add panels back to scroll pane
        for(NW_HousePanel house_panel : day_panel.house_panels) {
            day_panel.jsp_panel.add(house_panel, new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
        }


        // Resizing frame
        Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        Dimension newSize = new Dimension( frame.getWidth(), frame.getHeight() );
        int base = frame.getHeight() - day_panel.jsp.getHeight();
        int remaining_space = (int) effectiveScreenSize.getHeight() - (int) frame.getLocation().getY() - frame.getHeight();
        int remaining_from_base = (int) effectiveScreenSize.getHeight() - (int) frame.getLocation().getY() - base;
        int jsp_constant = 0; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
        int full_jsp_panel_height = day_panel.jsp_panel.getHeight() + jsp_constant;
        int jsp_difference = day_panel.jsp.getHeight() - full_jsp_panel_height;


        // if all house panels are visible and room to extend full panel: extend by one panel
        if ( jsp_difference >= 0  &&  remaining_space - panel_height >= 0 ) {
            newSize.setSize(frame.getWidth(), frame.getHeight() + panel_height);
        }

        // if all house panels are visible and less than one panel to extend: extend as much as possible
        else if ( jsp_difference >= 0  &&  ( remaining_space > 0 && remaining_space < panel_height ) ) {
            newSize.setSize(frame.getWidth(), frame.getHeight() + remaining_space);
        }

        // if any house panels are hidden on scroll pane: extend until no hidden panels or to bottom of screen
        else if ( jsp_difference < 0 ) {
            newSize.setSize( frame.getWidth(), base + Math.min( remaining_from_base, full_jsp_panel_height) );
        }

        frame.setSize(newSize);
        frame.revalidate();
        frame.repaint();
    }

}
