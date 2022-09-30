package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;

public class NW_DeleteHouseListener implements ActionListener {

	NW_DayPanel day_panel;
	NW_HousePanel house_panel;
	WorkerList dwd;

	public NW_DeleteHouseListener(NW_DayPanel day_panel, NW_HousePanel house_panel, WorkerList dwd) {
		this.day_panel = day_panel;
		this.house_panel = house_panel;
		this.dwd = dwd;
	}

	public void actionPerformed(ActionEvent e){

	    if (day_panel.getNumHousePanels() <= 1) {
	        return;
	    }

		// initializing variables
		int num = day_panel.getNumHousePanels();
		int index = -1;
		for(int i = 0; i < num; i++){
			if (day_panel.house_panels.get(i) == house_panel) {
				index = i;
			}
		}

		//remove old panels
		for(int i=0; i<num; i++){
			day_panel.jsp_panel.remove(day_panel.house_panels.get(i));
		}

		// delete house panel
		day_panel.house_panels.remove(index);

		// resetting focus listeners
		day_panel.addFlexibleFocusListeners();

		// add panels back to scroll pane
        for (int houseCount = 0; houseCount < day_panel.house_panels.size(); houseCount++) {
            day_panel.jsp_panel.add(
                day_panel.house_panels.get(houseCount),
                getHousePanelConstraints(day_panel.house_panels.size(), houseCount)
            );
        }

        ApplicationCoordinator.getInstance().refreshWindow();
	}

    private String getHousePanelConstraints(final int housePanelCount, final int index) {
        final int padding = index < housePanelCount - 1 ? DayPanel.PANEL_PADDING : 0;
        return "wrap " + padding + ", growx";
    }
}
