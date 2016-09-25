package src.java;

import java.awt.Font;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import src.java.menu.view.MenuPanel;
import src.java.menu.view.SettingsPanel;
import src.java.submit.view.DayPanel;


public class RunProgram {

	public static void main(String[] args) throws Exception{
			
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			// if nimbus is not available, use system default
			try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
			catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
				//...
            }
		}
		
		try {
			File file = SettingsPanel.settings_save_file;
			Scanner input = new Scanner(file);
			input.nextLine(); input.nextLine();
			int size = input.nextInt();
			input.close();

			DayPanel.FONT_SIZE = SettingsPanel.FONT_SIZE_BASE + SettingsPanel.FONT_SIZE_MULTIPLIER*size;
			DayPanel.HEADER_FONT_SIZE = SettingsPanel.HEADER_FONT_SIZE_BASE + SettingsPanel.FONT_SIZE_MULTIPLIER*size;
			DayPanel.TAB_FONT_SIZE = SettingsPanel.FONT_SIZE_BASE + SettingsPanel.FONT_SIZE_MULTIPLIER*size;
			
			UIManager.put("OptionPane.messageFont", new Font("System", Font.PLAIN, SettingsPanel.FONT_SIZE_BASE + SettingsPanel.FONT_SIZE_MULTIPLIER*size));
			UIManager.put("OptionPane.buttonFont", new Font("System", Font.PLAIN, SettingsPanel.FONT_SIZE_BASE + SettingsPanel.FONT_SIZE_MULTIPLIER*size));
		}
		catch (Exception exc) {
			UIManager.put("OptionPane.messageFont", new Font("System", Font.PLAIN, 24));
			UIManager.put("OptionPane.buttonFont", new Font("System", Font.PLAIN, 24));
		}

		
		SwingUtilities.invokeLater( new Runnable() {
			
			public void run() {
				
				JFrame menu_frame = new JFrame();
				MenuPanel menu_panel = new MenuPanel( menu_frame );
				
				menu_frame.add( menu_panel );
				menu_frame.setResizable( false );
				menu_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				
				menu_frame.pack();
	
				
				menu_frame.setLocationRelativeTo( null );
				menu_frame.setVisible( true );
			
			}
			
		});
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
}
