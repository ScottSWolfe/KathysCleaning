package src.java.general.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CountDownLatch;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import src.java.general.controller.StaticMethods;
import src.java.submit.view.DayPanel;

@SuppressWarnings("serial")
public class ConfirmFrame extends JFrame {

	// FIELDS
	public int selection = 0;
	CountDownLatch latch;
	
	public static final int CANCEL = 0;
	public static final int CONFIRM = 1;
	
	
	// CONSTRUCTOR
	public ConfirmFrame ( String message, CountDownLatch latch ) {
		
		this.latch = latch;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);  // TODO: change?
		setResizable( false );
		
		JPanel panel = panel( message, this );
		
		add(panel);
		pack();
		
		StaticMethods.findSetLocation(this);
		setVisible(true);
		
	}
	
	// PRIVATE CONSTRUCTION METHODS
	
	private JPanel panel( String s, ConfirmFrame f ) {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("insets 0") );
		panel.setBackground( DayPanel.BACKGROUND_COLOR );
		
		JLabel message = new JLabel();
		message.setText(s);
		message.setFont( message.getFont().deriveFont( DayPanel.FONT_SIZE ));
		
		System.out.println("test 2");
		System.out.println(s);
		
		JPanel p = continuePanel( f );
		
		
		panel.add(message, "gapleft 10, gapright 10, wrap 20");
		panel.add(p, "grow");
		
		return panel;
		
	}
	
	private JPanel continuePanel( ConfirmFrame f ) {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("align right") );
		panel.setBackground( DayPanel.HEADER_BACKGROUND );
		
		JButton cancel_button = new JButton();
		cancel_button.setText( "Cancel" );
		cancel_button.setFont( cancel_button.getFont().deriveFont(DayPanel.FONT_SIZE) );
		cancel_button.setBackground( DayPanel.MAIN_COLOR );
		cancel_button.setForeground( DayPanel.FOREGROUND_COLOR );
		cancel_button.addActionListener( new CancelListener( f ) );
		
		JButton submit_button = new JButton();
		submit_button.setText( "Confirm" );
		submit_button.setFont( submit_button.getFont().deriveFont(DayPanel.FONT_SIZE) );
		submit_button.setBackground( DayPanel.MAIN_COLOR );
		submit_button.setForeground( DayPanel.FOREGROUND_COLOR );
		submit_button.addActionListener( new SubmitListener( f ) );
		
		panel.add(cancel_button);
		panel.add(submit_button);
		
		System.out.println("test 3");
		
		return panel;
		
	}

	
	
	// PRIVATE LISTENERS
	
	private class CancelListener implements ActionListener {
		
		ConfirmFrame f;
		
		public CancelListener( ConfirmFrame f ) {
			this.f = f;
		}
		
		public void actionPerformed( ActionEvent e ) {
			
			f.setVisible(false);
			f.selection = 0;
			f.dispose();
			latch.countDown();
		}
		
	}
	
	private class SubmitListener implements ActionListener {
		
		ConfirmFrame f;
		
		public SubmitListener( ConfirmFrame f ) {
			this.f = f;
		}
		
		public void actionPerformed( ActionEvent e ) {
			
			f.setVisible(false);
			f.selection = 1;
			f.dispose();
			latch.countDown();
		}
		
	}
	
}
