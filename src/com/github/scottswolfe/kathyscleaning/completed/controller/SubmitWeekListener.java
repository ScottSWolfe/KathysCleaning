package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantController;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendController;


public class SubmitWeekListener implements ActionListener {

//  FIELDS
    GeneralController<TabbedPane, Data> controller;
	TabbedPane tp;
	JFrame frame;
	Calendar date;
	int mode;
	int wk;
	
	// for calendar date stuff
	int firstday;
	int lastday;
	
	public static File new_save;

	
	
//  CONSTRUCTOR

	public SubmitWeekListener(GeneralController<TabbedPane, Data> controller, TabbedPane tp, JFrame frame, Calendar date, int mode, int wk ){
		this.controller = controller;
	    this.tp = tp;
		this.frame = frame;
		this.date = date;
		this.mode = mode;
		this.wk = wk;
	}
	
	
	
//  METHODS
	

//  LISTENER
	public void actionPerformed(ActionEvent e) {
		if (!StaticMethods.confirmSubmitWeek()) {
			return;
		}
        controller.readInputAndWriteToFile(Settings.saveFile);
        controller.writeModelToExcel();            
        CovenantController.initializeCovenantPanelFrame(frame, date, mode, wk);
		saveHousePay();
	}
	
	
	
// PRIVATE METHODS ========================================================== */
	
	/**
	 * Method that saves the amount earned at a house as the house's default
	 * amount earned.
	 */
	private void saveHousePay() {
		
		BufferedWriter bw = null;
		try {

			Scanner input = new Scanner( HouseNameDocListener.HOUSE_PAY_FILE );
			Scanner input2 = new Scanner( HouseNameDocListener.HOUSE_PAY_FILE );
						
			int i=0;
			while (input2.hasNextLine()) {
				input2.nextLine();
				i++;
			}
			
			String[] s = new String[i];
			
			for( int j=0; j<i; j++) {
				s[j] = input.nextLine();
			}
			
			input.close();
			input2.close();
			
			// for each day
			for (int d=0; d<5; d++) {
				
				DayPanel dp = tp.day_panel[d];
				
				// for each house
				for (int h=0; h<dp.house_panel.length; h++) {
					
					boolean match = false;
					
					// for length of array
					for (int k=0; k<s.length; k++) {
						
						if (s[k].equalsIgnoreCase( dp.house_panel[h].house_name_txt.getText() )) {
							
							s[k+1] = dp.house_panel[h].pay_txt.getText();
							match = true;
							break;
						}
						
					}
					if (match == false) {
						
						String[] r = new String[s.length+2];
						
						for (int l=0; l<s.length; l++) {
							r[l] = s[l];
						}
						
						r[r.length-2] = dp.house_panel[h].house_name_txt.getText();
						r[r.length-1] = dp.house_panel[h].pay_txt.getText();
						
						s = r;
					}
				}
			}
			
			FileWriter fw = new FileWriter( HouseNameDocListener.HOUSE_PAY_FILE );
			bw = new BufferedWriter( fw );
			
			for (int m=0; m<s.length; m++) {
				
				bw.write(s[m]);
				bw.newLine();
			}
			
			bw.close();
			fw.close();
			
		}
		catch(Exception e1){
			e1.printStackTrace();
		}
	}
		
}
