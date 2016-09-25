package src.java.general.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class TimeKeyListener implements KeyListener {

	
//  FIELDS
	
	TimeDocFilter tdf;
	
	
	
//  CONSTRUCTOR
	
	public TimeKeyListener( TimeDocFilter tdf ){
		this.tdf = tdf;
	}
	
	
//  LISTENER

	public void keyPressed(KeyEvent pressed){
		tdf.lastkey = pressed.getKeyCode();
	}
		
		
	public void keyReleased(KeyEvent released){
			
	}
	
	
	public void keyTyped(KeyEvent typed){
		
	}
	
}
