package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.scottswolfe.kathyscleaning.completed.view.CompletedTabbedPane;

public class TabChangeListener implements ChangeListener {

    CompletedTabbedPane tp;
	JFrame frame;

	public TabChangeListener(CompletedTabbedPane tp, JFrame frame) {
		this.tp = tp;
		this.frame = frame;
	}

	@Override
	public void stateChanged(ChangeEvent arg0) {

		// getting tab information
		int from_tab = tp.previous_tab;
		int to_tab = tp.getSelectedIndex();

		// getting effective screen size
		Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

		int base = frame.getHeight() - tp.day_panel[ from_tab ].scrollPane.getHeight();
		int remaining_space = (int) effectiveScreenSize.getHeight() - (frame.getLocation().y + base);
		int jsp_constant = 6; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
		int full_jsp_panel_height = tp.day_panel[to_tab].getScrollPanePanelHeight() + jsp_constant;

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
