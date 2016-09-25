package src.java.general.controller;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;


public class FrameSizeListener implements ComponentListener {
	
	
	JFrame frame;
	
	public FrameSizeListener(JFrame frame){
		this.frame = frame;
	}
	
	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
		/*
		if(frame.getSize().getHeight() > frame.getMaximumSize().getHeight() ){
			Dimension newSize = new Dimension( (int)frame.getSize().getWidth(), (int)frame.getMaximumSize().getHeight() );
			frame.setSize(newSize);
		}
		*/
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
