package src.java.general.controller;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.concurrent.CountDownLatch;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import src.java.general.view.ConfirmFrame;


public class StaticMethods {

	
	public static int confirmSubmitWeek() { 
		String message = new String( "<html>Are you sure you are ready<br>to submit the week?" );
		String[] options = {"Cancel", "Confirm"};
		JFrame f = new JFrame();
		StaticMethods.findSetLocation(f);
		return JOptionPane.showOptionDialog(f, message, null, JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, 0);
	}
	
	
	// findSetLocation divides the screen into 9 rectangles and determines
	// where a new frame should be located based on the mouses' location
	// in each of these 9 rectangles
	
	public static void findSetLocation ( JFrame frame ) {
		
		Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();		
		
		int width = (int) effectiveScreenSize.getWidth()/3;
		int height = (int) effectiveScreenSize.getHeight()/3; 
		
		Rectangle[][] rect = new Rectangle[3][3];
		
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				
				rect[i][j] = new Rectangle( new Point(j*width,i*height), new Dimension( width, height ) );
				
			}
		}
		
		Point p = MouseInfo.getPointerInfo().getLocation();
		int i_value = 0;
		int j_value = 0;
		
		for (int i=0; i<3; i++) {
			for (int j=0; j<3; j++) {
				
				if ( contains(rect[i][j], p) ) {
					
					i_value = i;
					j_value = j;
					break;
					
				}
				
			}
		}
		
		int new_x = 0;
		int new_y = 0;
		
		if ( i_value == 1 ) {
			new_y = p.y - frame.getHeight()/2;
		}
		else if ( i_value == 2 ) {
			new_y = p.y - frame.getHeight();
		}
		else {
			new_y = p.y;
		}
		
		if ( j_value == 1 ) {
			new_x = p.x - frame.getWidth()/2;
		}
		else if ( j_value == 2 ) {
			new_x = p.x - frame.getWidth();
		}
		else {
			new_x = p.x;
		}

		
		if ( new_x < 0 ) {
			new_x = 0;
		}
		else if ( new_x + frame.getWidth() > effectiveScreenSize.getWidth() ) {
			new_x = new_x - (int) (new_x + frame.getWidth() - effectiveScreenSize.getWidth());
		}
		
		if ( new_y < 0 ) {
			new_y = 0;
		}
		else if ( new_y + frame.getHeight() > effectiveScreenSize.getHeight() ) {
			new_y = new_y - (int) (new_y + frame.getHeight() - effectiveScreenSize.getHeight());
		}
		
		frame.setLocation( new_x, new_y );
		
		
	}
	
	private static boolean contains(Rectangle r, Point p) {
        return (r.getLocation().getX() < p.getX() &&
        		r.getLocation().getY() < p.getY() &&
                r.getLocation().getX() + r.getWidth() > p.getX()  &&
                r.getLocation().getY() + r.getHeight() > p.getY());
    }
	
	
	// isRepeatWorker takes in combobox[] and returns true
	// if there is a repeat selected item and false otherwise
	
	public static boolean isRepeatWorker( JComboBox<String>[] box ) {
		
		// for each combobox
		for (int i=0; i<box.length; i++) {
			
			// compare it to each combobox except itself
			for (int j=0; j<box.length; j++) {
				
				// if name matches return true
				if ( i != j &&
						String.valueOf(box[i].getSelectedItem()).equals(
						String.valueOf(box[j].getSelectedItem())) &&
						!String.valueOf(box[i].getSelectedItem()).equals("")) {
					
					return true;
				}
				
			}
			
		}
		
		return false;
	}
	
	public static boolean isRepeatWorker( JComboBox<String>[][] box ) {
		
		// for each combobox
		for (int i=0; i<box.length; i++) {
			for (int j=0; j<box[i].length; j++) {
				
				// compare to each combobox
				for (int m=0; m<box.length; m++) {
					for (int n=0; n<box[m].length; n++) {
						
						// if name matches return true
						if ( (i != m || j != n) &&
								String.valueOf(box[i][j].getSelectedItem()).equals(
								String.valueOf(box[m][n].getSelectedItem())) &&
								!String.valueOf(box[i][j].getSelectedItem()).equals("")) {
							
							return true;
						}
						
					}
					
				}
				
			}
			
		}
		
		return false;
	}

	public static void shareRepeatWorker() {
		
		String message = new String("<html>There is a repeat selection. Please choose a different\nworker in each box.");
		
		JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
	}
	
	
	// this doesn't work for some reason...
	public static int areYouSure ( String message ) {
		
		CountDownLatch latch = new CountDownLatch( 1 );
		ConfirmFrame cf = new ConfirmFrame( message, latch );
		try {
			latch.await();
		}
		catch(Exception e){
			System.out.println("Error with CountDownLatch");
		}
		
		
		return cf.selection;
		
	}
	
	
}
