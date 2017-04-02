package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.controller.MenuBarController;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;


public class NW_SubmitWeekListener implements ActionListener {

	GeneralController<TabbedPane, NW_Data> controller;
	TabbedPane tp;
	JFrame frame;
	FileOutputStream out;
	int mode;
	int wk;
	
	public NW_SubmitWeekListener(GeneralController<TabbedPane, NW_Data> controller, 
	        TabbedPane tp, JFrame frame, int mode, int wk) {
	    
	    this.controller = controller;
		this.tp = tp;
		this.frame = frame;
		this.mode = mode;
		this.wk = wk;
	}
	
	public void actionPerformed(ActionEvent e){
		
	    controller.readInputAndWriteToFile(null);
		boolean response = MainWindowListener.askUserIfSaveBeforeClose(controller, false);
		if (response == false) {
		    return;
		}
		
		// TODO temporary hack
		JOptionPane.showMessageDialog(null, "Save the new Excel Document.");
		MenuBarController<TabbedPane, NW_Data> hack = new MenuBarController<>(controller);
		hack.menuItemGenExcel();
		
		frame.setVisible(false);
		frame.dispose();
		
		try {
			Desktop dt = Desktop.getDesktop();
			dt.open(SettingsModel.getExcelSaveLocation());
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(new JFrame(), "The Excel document could not be opened automatically.");
		}

		MenuPanelController.initializeMenuPanelFrame();
	}
		
}
