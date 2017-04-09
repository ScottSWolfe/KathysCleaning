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
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;

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
	    askUserIfSaveBeforeClose(controller, true);
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean askUserIfSaveBeforeClose(Controller controller, boolean initializeMenu) {
	    String[] options = {"Save", "Cancel", "Do not Save"};
        int SAVE = 0; int CANCEL = 1; int NOT_SAVE = 2;
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
                closeProgram(controller, initializeMenu);
                return true;
            } else {
                return false;
            }
        } else if (response == CANCEL) {
            return false;
        } else if (response == NOT_SAVE) {
            closeProgram(controller, initializeMenu);
            return true;
        } else {
            return false;
        }
	}
	
    @SuppressWarnings("rawtypes")
    private static void closeProgram(Controller controller, boolean initializeMenu) {
        controller.eliminateWindow();
        if (initializeMenu) {
            MenuPanelController.initializeMenuPanelFrame();
        }
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
    
