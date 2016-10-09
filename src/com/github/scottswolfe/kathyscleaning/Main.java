package com.github.scottswolfe.kathyscleaning;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.MenuPanel;
import com.github.scottswolfe.kathyscleaning.menu.view.SettingsPanel;


/**
 * This class contains the main method and launches the application.
 * 
 */
public class Main {

	public static void main(String[] args) {
	    
	    
	    // Setting the Look and Feel
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
				ex.printStackTrace();
            }
		}
		
		// Loading text size factor from Saved Settings
		int textSizeFactor;
		try {
		    File file = SettingsPanel.SETTINGS_SAVE_FILE;
		    Scanner input = new Scanner(file);
		    input.nextLine();    // consuming line
		    input.nextLine();    // consuming line
		    textSizeFactor = input.nextInt();
		    input.close();
		} catch (Exception e) {
		    textSizeFactor = 3;
		}
			
		Settings.setFontSize(textSizeFactor);
		Settings.setHeaderFontSize(textSizeFactor);
		Settings.setTabFontSize(textSizeFactor);
			
			

		// Launching Menu Panel
		SwingUtilities.invokeLater( new Runnable() {
			
			public void run() {				
				JFrame menuFrame = new JFrame();
				MenuPanel menuPanel = new MenuPanel(menuFrame);
				
				menuFrame.add(menuPanel);
				menuFrame.setResizable(false);
				menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				menuFrame.pack();
				menuFrame.setLocationRelativeTo(null);
				menuFrame.setVisible(true);
			}
			
		});
		
	}
	
}
