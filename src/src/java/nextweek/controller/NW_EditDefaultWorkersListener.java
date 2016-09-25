package src.java.nextweek.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import src.java.general.controller.FrameCloseListener;
import src.java.general.controller.StaticMethods;
import src.java.general.model.DefaultWorkerData;
import src.java.nextweek.view.NW_DayPanel;
import src.java.nextweek.view.NW_EditDefaultWorkersPanel;


public class NW_EditDefaultWorkersListener implements ActionListener {


//  FIELDS
	
	DefaultWorkerData dwd;
	NW_DayPanel day_panel;
	JFrame container_frame;
	
	
	
	
//  CONSTRUCTOR
	
	public NW_EditDefaultWorkersListener(DefaultWorkerData dwd, NW_DayPanel day_panel, JFrame container_frame) {
		this.dwd = dwd;
		this.day_panel = day_panel;
		this.container_frame = container_frame;
	}
	
	
	
	
	
//  LISTENER
	
	public void actionPerformed(ActionEvent e){
		
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.addWindowListener( new FrameCloseListener( container_frame ));
		container_frame.setEnabled(false);
		
		try {
			frame.add( new NW_EditDefaultWorkersPanel( day_panel.header_panel.dwp, frame, day_panel ) );
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		frame.pack();
		
		StaticMethods.findSetLocation(frame);
		
		frame.setVisible(true);
		
	}
	
}
