package src.java.nextweek.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import src.java.general.controller.FrameCloseListener;
import src.java.general.controller.StaticMethods;
import src.java.general.model.DefaultWorkerData;
import src.java.nextweek.model.NoteData;
import src.java.nextweek.view.NW_DayPanel;
import src.java.nextweek.view.NW_NotePanel;


public class NW_NoteListener implements ActionListener {

	
	// FIELDS
	
	NW_DayPanel day_panel;
	DefaultWorkerData dwd;
	JFrame container_frame;
	public NoteData note_data;
	int note_type;
	
	
	
	// CONSTRUCTOR
	public NW_NoteListener ( NW_DayPanel day_panel, DefaultWorkerData dwd, NoteData note_data, int note_type, JFrame container_frame) {
		this.day_panel = day_panel;
		this.dwd = dwd;
		this.note_data = note_data;
		this.note_type = note_type;
		this.container_frame = container_frame;
	}
	
	
	
	// LISTENER
	public void actionPerformed ( ActionEvent e ) {
		
		// TODO getting selected workers. get workers either based on covenant workers selected
		//		or based on workers selected for houses. these workers will be made default selected workers
		//		for which to write a note
		String[] selected_workers = null;
		
		// end getting selected workers
		
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setResizable( false );
		
		frame.addWindowListener( new FrameCloseListener( container_frame ));
		container_frame.setEnabled(false);
		
		NW_NotePanel np = new NW_NotePanel(frame, day_panel, dwd, selected_workers, note_data, note_type, this);
		
		frame.add( np );
		frame.pack();
		/*
		if (note_type == NW_NotePanel.DAY_NOTE) {
			frame.setLocation( new Point( (int) MouseInfo.getPointerInfo().getLocation().getX() - np.getWidth(),
										(int) MouseInfo.getPointerInfo().getLocation().getY() ) );
		}
		else {
			frame.setLocation( new Point( (int) MouseInfo.getPointerInfo().getLocation().getX() - np.getWidth(),
					(int) MouseInfo.getPointerInfo().getLocation().getY() - np.getHeight() ) );
		}
		*/
		
		StaticMethods.findSetLocation(frame);
		
		frame.setVisible( true );
		
	}
	
}
