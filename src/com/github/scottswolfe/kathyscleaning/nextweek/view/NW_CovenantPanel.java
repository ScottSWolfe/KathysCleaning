package com.github.scottswolfe.kathyscleaning.nextweek.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.nextweek.controller.NW_NoteListener;

import net.miginfocom.swing.MigLayout;


public class NW_CovenantPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 780236108036077938L;
	
	
	
	// FIELDS
	JLabel header_label;
	DefaultWorkerData dwd;
	public DefaultWorkerPanel dwp;
	JButton edit_button;
	JButton covenant_note_button;
	
	NW_DayPanel day_panel;
	JFrame container_frame;
	
	
	// CONSTRUCTOR
	public NW_CovenantPanel( NW_DayPanel day_panel, DefaultWorkerData dwd, JFrame container_frame ) {
		
		this.day_panel = day_panel;
		this.dwd = dwd;
		this.container_frame = container_frame;
		
		setLayout( new MigLayout( "insets 10, fillx", "[]10[]10[]10[]30[]", "[grow]" ) );
		setBackground( Settings.BACKGROUND_COLOR );
		//setBorder( BorderFactory.createLineBorder( null ));
		/*
		Border border = BorderFactory.createLineBorder(null,1);
		MatteBorder mborder = BorderFactory.createMatteBorder(1, 0, 1, 1, Color.BLACK);
		Border eborder = BorderFactory.createEmptyBorder(1, 1, 1, 1);
		CompoundBorder border2 = BorderFactory.createCompoundBorder(mborder, eborder);
		
		CompoundBorder border3 = BorderFactory.createCompoundBorder(border2, BorderFactory.createLoweredBevelBorder());

		setBorder(border3);
		*/
		//Border border = BorderFactory.createLineBorder(null,2);
		MatteBorder mborder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
		//CompoundBorder border2 = BorderFactory.createCompoundBorder(mborder, BorderFactory.createLoweredBevelBorder());
				
		setBorder( mborder );
				
		
		header_label = new JLabel();
		header_label.setText( "Covenant" );
		header_label.setFont( header_label.getFont().deriveFont( Settings.HEADER_FONT_SIZE ));
		
		dwp = new DefaultWorkerPanel( dwd, Settings.BACKGROUND_COLOR, DefaultWorkerPanel.COV_ROWS, DefaultWorkerPanel.COV_COLUMNS,
										null, null);
		
		edit_button = new JButton();
		edit_button.setText( "Edit");
		//edit_button.setBackground( Settings.MAIN_COLOR);
		edit_button.setFont( edit_button.getFont().deriveFont(Settings.FONT_SIZE));
		//edit_button.setForeground( Settings.FOREGROUND_COLOR );
		edit_button.addActionListener( new EditWorkersListener(this, dwd, dwp) );
		
		covenant_note_button = new JButton();
		covenant_note_button.setText( "Note");
		//covenant_note_button.setBackground( Settings.MAIN_COLOR);
		covenant_note_button.setFont( covenant_note_button.getFont().deriveFont(Settings.FONT_SIZE));
		//covenant_note_button.setForeground( Settings.FOREGROUND_COLOR );
		covenant_note_button.addActionListener( new NW_NoteListener( day_panel, dwd, day_panel.covenant_note_data, NW_NotePanel.COVENANT_NOTE, container_frame ) );
		
		
		add(header_label, "grow" );
		//add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(dwp, "grow");
		add(edit_button, "growx");
		add( new JSeparator(SwingConstants.VERTICAL), "growy" );
		add(covenant_note_button, "growx, hmin 50");
		
		
	}
	
	
	// PUBLIC METHODS
	
	public void changeWorkerPanel( DefaultWorkerData new_dwd ) throws Exception{
		
		day_panel.changeCovenantWorkerPanel( new_dwd );
		
	}
	
	
	
	// PRIVATE METHODS
	
	
	
	// PRIVATE LISTENERS
	
	private class EditWorkersListener implements ActionListener {
		
		// FIELDS
		NW_CovenantPanel cov_panel;
		DefaultWorkerData dwd;
		DefaultWorkerPanel dwp;
		
		
		// CONSTRUCTOR
		
		private EditWorkersListener(NW_CovenantPanel cov_panel, DefaultWorkerData dwd, DefaultWorkerPanel dwp) {
			this.cov_panel = cov_panel;
			this.dwd = dwd;
			this.dwp = dwp;
		}
		
		public void actionPerformed(ActionEvent e){
			
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setResizable(false);
			
			frame.addWindowListener( new FrameCloseListener( container_frame ));
			container_frame.setEnabled(false);
			
			try {
			frame.add( new EditCovenantWorkersPanel( cov_panel, frame, dwd, dwp ) );
			} catch (Exception e1) {
				
			}
			
			
			frame.pack();
			StaticMethods.findSetLocation(frame);
			
			frame.setVisible(true);
			
		}
		
	}
	
	
}
