package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;


public class TabChangeListener implements ChangeListener {

//  FIELDS
	
	TabbedPane tp;
	JFrame frame;
	
//  CONSTRUCTOR
	
	public TabChangeListener( TabbedPane tp, JFrame frame ){
		
		this.tp = tp;
		this.frame = frame;
	}
	
	
//  LISTENER
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		
	// Resizing frame to fit new tab
		/*
		if ( tp.day_panel[tp.getSelectedIndex()].header_panel.week_Aweek_A_button.isSelected() ) {
			ActionEvent event = new ActionEvent(this,0,"test");
			ActionListener[] al = tp.day_panel[0].header_panel.week_A.getActionListeners();
			al[0].actionPerformed( event );
		}
		else if ( week_B_rbutton.isSelected() ) {
			ActionEvent event = new ActionEvent(this,0,"test");
			ActionListener[] al = day_panel[0].header_panel.week_B.getActionListeners();
			al[0].actionPerformed( event );
		}
		else {
			// do nothing
		}
		*/
		// getting tab information
		int from_tab = tp.previous_tab;
		int to_tab = tp.getSelectedIndex();
		
		// getting effective screen size
		Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();		

		int base = frame.getHeight() - tp.day_panel[ from_tab ].jsp.getHeight();		
		int remaining_space = (int) effectiveScreenSize.getHeight() - (frame.getLocation().y + base);
		int jsp_constant = 6; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
		int full_jsp_panel_height = tp.day_panel[to_tab].jsp_panel.getHeight() + jsp_constant;
		
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
	
	public static void resize(TabbedPane tp, JFrame frame, int from_tab, int to_tab) {
		
		// getting effective screen size
		Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();		

		int base = frame.getHeight() - tp.day_panel[ from_tab ].jsp.getHeight();		
		int remaining_space = (int) effectiveScreenSize.getHeight() - (frame.getLocation().y + base);
		int jsp_constant = 6; // this is difference between size of jsp (scrollpane) and jsp_panel (jpanel on scrollpane)
		int full_jsp_panel_height = tp.day_panel[to_tab].jsp_panel.getHeight() + jsp_constant;
		
		int new_height = base + Math.min( remaining_space, full_jsp_panel_height );
		int new_width = frame.getWidth();
		
		Dimension newSize = new Dimension( new_width, new_height  );
		
		// resizing, revalidating, repainting frame
		frame.setSize(newSize);
		frame.revalidate();
		frame.repaint();
		
		// changing previous tab to current tab for next tab change
		tp.changePreviousTab(to_tab);
		
	}

	public static void nw_resize(TabbedPane tp, JFrame frame, int from_tab, int to_tab) {
		
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
		tp.changePreviousTab(to_tab);
		
	}
}
