package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;

public class NW_MoveUpListener implements ActionListener {

	NW_DayPanel day_panel;
	NW_HousePanel house_panel;
	WorkerList dwd;

	public NW_MoveUpListener(NW_DayPanel day_panel, NW_HousePanel house_panel, WorkerList dwd) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
	}

	public void actionPerformed(ActionEvent e){

		// initializing variables
		int num = day_panel.getNumHousePanels();
		int index = -1;
		for(int i = 0; i < num; i++) {
			if (day_panel.house_panels.get(i) == house_panel) {
				index = i;
			}
		}

		// only move up if it is not the first panel
		if ( index <= 0) {
		    return;
		}

		//remove panels from scroll pane
        for(int i = 0; i < num; i++){
            day_panel.jsp_panel.remove(day_panel.house_panels.get(i));
        }

        Collections.swap(day_panel.house_panels, index, index - 1);

        // resetting focus listeners
        day_panel.addFlexibleFocusListeners();

        // add panels back to scroll pane
        for(NW_HousePanel house_panel : day_panel.house_panels) {
            day_panel.jsp_panel.add(house_panel, new String("wrap " + DayPanel.PANEL_PADDING + ", growx") );
        }

        day_panel.revalidate();
        day_panel.repaint();
	}
}
