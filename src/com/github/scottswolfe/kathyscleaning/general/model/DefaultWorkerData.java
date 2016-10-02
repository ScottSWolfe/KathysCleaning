package com.github.scottswolfe.kathyscleaning.general.model;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;




public class DefaultWorkerData {
	
	
//  FIELDS
	
	public String[] default_workers;
	public static final String HOUSE_WORKERS = (System.getProperty("user.dir") + "\\save\\HouseWorkerSaveFile");
	public static final String COVENANT_WORKERS = (System.getProperty("user.dir") + "\\save\\CovenantWorkerSaveFile");
	public static final String WEEKEND_WORKERS = (System.getProperty("user.dir") + "\\save\\WeekendWorkSaveFile");
	
	
//  CONSTRUCTORS
	
	public DefaultWorkerData( String filename ) {
		this.default_workers = readWorkers( filename );	
	}
	
	public DefaultWorkerData( String[] default_workers )  {
		this.default_workers = default_workers;
	}
	
	
	
//METHODS
	
	public String[] readWorkers(String filename) {
		
		try {
			
		
		File file = new File(filename);
		
		//Counting how many workers are in the save file
		Scanner counter = new Scanner(file);
	    int i=0;
	    while ( counter.hasNext() ) {
	    	i++;
	    	counter.nextLine();
	    }
	    counter.close();
		
	    //Creating a String array of the workers in the save file
		String[] workers = new String[i];
		Scanner input = new Scanner(file);
		int j=0;
		while( input.hasNext() ) {
			workers[j] = input.nextLine();
			j++;
		}
		input.close();
		
		
		return workers; 
		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(new JFrame(), null, "Error Reading File", 0);
			return null;
		}
		
	}
	
	
	
	
}
