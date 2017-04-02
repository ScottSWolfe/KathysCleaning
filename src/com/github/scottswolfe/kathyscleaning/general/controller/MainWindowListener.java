package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;

public class MainWindowListener implements WindowListener {
	
    @SuppressWarnings("rawtypes")
    Controller controller;
    
    @SuppressWarnings("rawtypes")
    public MainWindowListener(Controller controller) {
        this.controller = controller;
    }
    
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {

		String[] options = {"Save", "Cancel", "Close"};
		int SAVE = 0; int CANCEL = 1; int CLOSE = 2;
		int response = JOptionPane.showOptionDialog(new JFrame(), "<html>Would you like to save your file before closing?",
									 null, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 0);
		if (response == SAVE) {
		    // TODO this is repeat code; copied from MenuBarController
		    File file = null;
		    if (!SessionModel.isSaveFileChosen()) {
		        file = FileChooserHelper.saveAs(
		               FileChooserHelper.SAVE_FILE_DIR, FileNameHelper.createDatedFileName(
		               FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
		               FileChooserHelper.TXT), FileChooserHelper.TXT);
		        if (file != null) {
		            SessionModel.setSaveFile(file);
		        }
		    } else {
		        file = SessionModel.getSaveFile();
		    }
		    if (file != null) {
		        controller.readInputAndWriteToFile(file);
		        closeProgram();
		    }
		} else if (response == CANCEL) {
		    return;
		} else if (response == CLOSE) {
		    closeProgram();
		} else {
		    return;		
		}
	}
	
	private void closeProgram() {
        System.exit(0);
        // TODO: make it so main menu opens back up instead of completely ending the program
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
    
