package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.scottswolfe.kathyscleaning.scheduled.view.ScheduledTabbedPane;

public class NW_TabChangeListener implements ChangeListener {

//  FIELDS

	ScheduledTabbedPane tp;
	JFrame frame;

//  CONSTRUCTOR

	public NW_TabChangeListener( ScheduledTabbedPane tp, JFrame frame ){
		this.tp = tp;
		this.frame = frame;
	}


//  LISTENER

	@Override
	public void stateChanged(ChangeEvent arg0) {

	// Resizing frame to fit new tab

		// getting tab information
		int from_tab = tp.previous_tab;
		int to_tab = tp.getSelectedIndex();


		// getting effective screen size
		Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		int base = frame.getHeight() - tp.nw_day_panel[ from_tab ].jsp.getHeight();
		int remaining_space = (int) effectiveScreenSize.getHeight() - (frame.getLocation().y + base);
		int jsp_constant = 0; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
		int full_jsp_panel_height = tp.nw_day_panel[to_tab].jsp_panel.getHeight() + jsp_constant;

		int new_height = base + Math.min( remaining_space, full_jsp_panel_height );
		int new_width = frame.getWidth();

		Dimension newSize = new Dimension( new_width, new_height  );

		// resizing, revalidating, repainting frame
		frame.setSize(newSize);
		frame.revalidate();
		frame.repaint();

		// changing previous tab to current tab for next tab change
		tp.changePreviousTab(tp.getSelectedIndex());
	}
}
