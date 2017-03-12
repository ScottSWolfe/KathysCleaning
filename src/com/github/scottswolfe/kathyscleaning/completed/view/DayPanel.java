package com.github.scottswolfe.kathyscleaning.completed.view;

import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class DayPanel extends JPanel{
	
// FIELDS ------------------------------------------------------------------- */
    
    GeneralController controller;
	TabbedPane tp;
	public JScrollPane jsp;
	public JPanel jsp_panel;
	DayData day_data;
	Calendar date;
	MainFrame<?,?> frame;
	
	int wk;
	
	public HeaderPanel header_panel;
	public HousePanel[] house_panel;
	
	WorkerList dwd;
	
	public int mode;
	
	public final static int PANEL_PADDING = 6;
	public final static int TOP_INSET = 5;
	
	public final static int NUM_HOUSE_PANELS = 3;
	



	
	public DayPanel(GeneralController controller, TabbedPane tp,
	        WorkerList dwd, Calendar date, MainFrame<?,?> frame, int mode, int wk) {
		
	    this.controller = controller;
		this.tp = tp;
		this.dwd = dwd;
		this.date = date;
		this.frame = frame;
		this.mode = mode;
		this.wk = wk;
		
		header_panel = new HeaderPanel(controller, tp, dwd, this,
		                                date, frame, mode, wk);
		
		// System.out.println
		
		jsp_panel = new JPanel();
		jsp_panel.setLayout( new MigLayout() );
		jsp_panel.setBackground(Settings.BACKGROUND_COLOR);
		
		house_panel = new HousePanel[DayPanel.NUM_HOUSE_PANELS];
		for(int i=0; i<DayPanel.NUM_HOUSE_PANELS; i++) {
			String temp = new String("House " + (i+1) );
			house_panel[i] = new HousePanel(temp,dwd,this,frame,tp);
		}
		
		addFlexibleFocusListeners();
		
		setLayout( new MigLayout( /*new String("insets " + TOP_INSET + " 5 0 5"),"",""*/) );
		setBackground(Settings.BACKGROUND_COLOR);
		
		add(header_panel, "dock north"); //new String("wrap " + PANEL_PADDING + ", growx") );
		
		for(int i=0; i<house_panel.length - 1; i++) {
			jsp_panel.add(house_panel[i], new String("wrap " + PANEL_PADDING + ", grow") );
		}
		jsp_panel.add(house_panel[house_panel.length-1], new String("wrap " + PANEL_PADDING + ", grow") );
		
		jsp = new JScrollPane( jsp_panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		jsp.setBackground(Settings.BACKGROUND_COLOR);
		
		add(jsp, "grow");
		
	}
	
	
	public DayPanel() {
		
	}
	
	
	
	// PRIVATE METHODS
	
	public void addFlexibleFocusListeners(){
		

		// adding focus listeners for textfields and buttons
		for (int i=0; i<house_panel.length; i++) {
					
			HousePanel hp = house_panel[i];
			
			HousePanel hp_up;
			HousePanel hp_down;
					
			if ( i > 0 ) {
				hp_up = house_panel[i-1];
			}
			else {
				hp_up = new HousePanel();  // all null fields
			}
			if ( i < house_panel.length - 1 ) {
				hp_down = house_panel[i+1];
			}
			else {
				hp_down = new HousePanel(); // all null fields
			}
			
			hp.house_name_txt.addFocusListener( new FlexibleFocusListener(hp.house_name_txt,
					FlexibleFocusListener.TEXTFIELD,
					null, hp.pay_txt,
					hp_up.house_name_txt, hp_down.house_name_txt, 
					null) );
			
			hp.pay_txt.addFocusListener( new FlexibleFocusListener(hp.pay_txt, 
					FlexibleFocusListener.TEXTFIELD,
					hp.house_name_txt, hp.time_begin_txt,
					hp_up.pay_txt, hp_down.pay_txt,
					null));
				
			hp.time_begin_txt.addFocusListener(new FlexibleFocusListener(hp.time_begin_txt, 
					FlexibleFocusListener.TEXTFIELD,
					hp.pay_txt, hp.time_end_txt,
					hp_up.time_begin_txt, hp_down.time_begin_txt,
					null));
			
			hp.time_end_txt.addFocusListener(new FlexibleFocusListener(hp.time_end_txt, 
					FlexibleFocusListener.TEXTFIELD,
					hp.time_begin_txt, hp.worker_panel.workerCheckBoxes[0][0],
					hp_up.time_end_txt, hp_down.time_end_txt,
					hp_down.time_begin_txt));
			
			hp.exceptions.addFocusListener( new FlexibleFocusListener(hp.exceptions, 
					FlexibleFocusListener.BUTTON, null, null, null, null, null));
		}

		
	}
	
	
	
	
	
//  PUBLIC METHODS
	
	public void changeWorkerPanels(WorkerList new_dwd){
		
		//getting old size
		int header_width = header_panel.getWidth();
		int house_panel_width = house_panel[0].getWidth();
		
		//copy and edit old panels
		HeaderPanel new_header_panel = new HeaderPanel(controller, tp, new_dwd, this, date, frame, mode, wk);
		HousePanel[] new_house_panel = new HousePanel[ house_panel.length ];
		for(int i=0; i<house_panel.length; i++){
			new_house_panel[i] = house_panel[i].changeHouseWorkers(new_dwd);
		}
		
		//removing old panels
		remove( header_panel );
		for(int i=0; i<house_panel.length; i++){
			jsp_panel.remove(house_panel[i]);
		}
		
		//changing field data
		this.header_panel = new_header_panel;
		this.house_panel = new_house_panel;
		
		// add new panels
		add(header_panel, "dock north" );
		for(int i=0; i<house_panel.length - 1; i++) {
			jsp_panel.add(house_panel[i], new String("wrap " + PANEL_PADDING + ", growx") );
		}
		jsp_panel.add(house_panel[house_panel.length-1], new String("wrap " + PANEL_PADDING + ", growx") );
		
		// revalidate and repaint
		frame.revalidate();
		frame.repaint();
		
		int new_header_width = header_panel.getWidth();
		int new_house_panel_width = house_panel[0].getWidth();
		
		int header_change = new_header_width - header_width;
		int house_panel_change = new_house_panel_width - house_panel_width;
		int change = 0;
		if(header_change > house_panel_change) {
			change = header_change;
		}
		else {
			change = house_panel_change;
		}
		 
		
		frame.setSize( frame.getWidth() + change , frame.getHeight() );
		
		frame.revalidate();
		frame.repaint();
		frame.setVisible(true);
		
	}
	
	
	
}
