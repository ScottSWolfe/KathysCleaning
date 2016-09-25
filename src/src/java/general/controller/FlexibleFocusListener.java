package src.java.general.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;




	
public class FlexibleFocusListener implements FocusListener {
		
	// FIELDS
	Component this_component;
	Component right_component;
	Component left_component;
	Component up_component;
	Component down_component;
	Component enter_component;
		
	FlexibleKeyListener kl;
	
	private int component_type; 
	
	public final static int BUTTON = 0;
	public final static int TEXTFIELD = 1;
	public final static int CHECKBOX = 2;
	public final static int COMBOBOX = 3;
		
		
	// CONSTRUCTOR
	public FlexibleFocusListener (Component this_component,
			int component_type,
			Component left, Component right,
			Component up, Component down,
			Component if_enter_pressed) {
		
		if ( component_type == COMBOBOX ) {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>) this_component;
			this.this_component = ( cb.getEditor().getEditorComponent() );
		}
		else {
			this.this_component = this_component;
		}
		this.component_type = component_type;
		
		this.left_component = left;
		this.right_component = right;
		this.up_component = up;
		this.down_component = down;
		this.enter_component = if_enter_pressed;
		
	}
		
		
	@Override
	public void focusGained(FocusEvent arg0) {
		
		kl = new FlexibleKeyListener();		
		this_component.addKeyListener( kl );
		
		if ( component_type == TEXTFIELD ) {
			JTextField tf = (JTextField) this_component;
			tf.selectAll();
		}
		else if ( component_type == COMBOBOX ) {
			@SuppressWarnings("unchecked")
			JComboBox<String> cb = (JComboBox<String>) this_component.getParent();
			cb.getEditor().selectAll();
		}
		
	}

	@Override
	public void focusLost(FocusEvent arg0) {

		this_component.removeKeyListener( kl );

	}
		
		
		
		
	
	
	private class FlexibleKeyListener implements KeyListener {

		// FIELDS

		
		// CONSTRUCTOR
		public FlexibleKeyListener () {
	
		}
		
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			
			// RIGHT ARROW
			if ( arg0.getKeyCode() == KeyEvent.VK_RIGHT ) {
				if ( right_component != null ) {
					right_component.requestFocusInWindow();
				}
				else {
					// do nothing
				}
			}
			
			
			// LEFT ARROW
			if ( arg0.getKeyCode() == KeyEvent.VK_LEFT ) {
				if ( left_component != null ) {
					left_component.requestFocusInWindow();
				}
				else {
					// do nothing
				}
			}
			
			
			// UP ARROW
			if ( arg0.getKeyCode() == KeyEvent.VK_UP ) {
				if ( up_component != null ) {
					up_component.requestFocusInWindow();
				}
				else {
					// do nothing
				}
			}
			
			
			// DOWN ARROW
			if ( arg0.getKeyCode() == KeyEvent.VK_DOWN ) {
				if ( down_component != null ) {
					down_component.requestFocusInWindow();
				}
				else {
					// do nothing
				}
			}
			
			
			// ENTER
			if ( arg0.getKeyCode() == KeyEvent.VK_ENTER ) {
				if ( component_type == BUTTON ) {
						try {
							JButton button = (JButton) this_component;
							ActionEvent e = new ActionEvent(this, 0, "");
							ActionListener[] al = button.getActionListeners();
							al[0].actionPerformed(e);
						}
						catch(Exception exc){
							exc.printStackTrace();
						}
					}
					else if ( component_type == CHECKBOX ) {
						try {
							JCheckBox checkbox = (JCheckBox) this_component;
							checkbox.setSelected( !checkbox.isSelected() );
							if ( right_component != null ) {
								right_component.requestFocusInWindow();
							}
						}
						catch(Exception exc){
							exc.printStackTrace();
						}
						
						if ( enter_component != null ) {
							enter_component.requestFocusInWindow();
						}
					}
					else if ( enter_component != null ) {
						enter_component.requestFocusInWindow();
					}
					else if ( right_component != null ) {
						right_component.requestFocusInWindow();
					}
					else if ( down_component != null ) {
						down_component.requestFocusInWindow();
					}
					else {
						// do nothing
					}
				}
			
			
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		
		
	}
	
}
