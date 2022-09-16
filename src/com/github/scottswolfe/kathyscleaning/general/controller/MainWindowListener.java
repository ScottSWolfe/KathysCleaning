package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.helper.FileChooserHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.menu.controller.MenuPanelController;

public class MainWindowListener implements WindowListener {

    public enum Action {
        CLOSE, OPEN
    }

    FormController<?, ?> controller;

    public MainWindowListener(FormController<?, ?> controller) {
        this.controller = controller;
    }

    @Override
    public void windowActivated(WindowEvent e) {}

    @Override
    public void windowClosed(WindowEvent e) {}

    @Override
    public void windowClosing(WindowEvent e) {
        askUserIfSaveBeforeAction(controller, Action.CLOSE, true);
    }

    public static boolean askUserIfSaveBeforeAction(FormController<?, ?> controller, Action action, boolean shouldInitializeMenu) {
        String[] options = {"Save",  "Don't Save", "Cancel"};
        int SAVE = 0; int NOT_SAVE = 1; int CANCEL = 2;
        String message;
        if (action == Action.OPEN) {
            message = "<html>Would you like to save the current file before opening another?";
        } else {
            message = "<html>Would you like to save your file before closing?";
        }
        int response = JOptionPane.showOptionDialog(new JFrame(), message,
                                     null, JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, 0);
        if (response == SAVE) {
            File file = null;
            if (SessionModel.isSaveFileChosen() == false) {
                file = FileChooserHelper.saveAs(
                       FileChooserHelper.SAVE_FILE_DIR, FileNameHelper.createDatedFileName(
                       FileChooserHelper.SAVE_FILE_DIR.getAbsolutePath(),
                       FileChooserHelper.KC), FileChooserHelper.KC);
            } else {
                file = SessionModel.getSaveFile();
            }
            if (file != null) {
                MenuBarController.prepareFileName(file);
                SessionModel.setSaveFile(file);
                controller.readInputAndWriteToFile();
                if (action == Action.CLOSE) {
                    closeProgram(controller, shouldInitializeMenu);
                }
                return true;
            } else {
                return false;
            }
        } else if (response == CANCEL) {
            return false;
        } else if (response == NOT_SAVE) {
            if (action == Action.CLOSE) {
                closeProgram(controller, shouldInitializeMenu);
            }
            return true;
        } else {
            return false;
        }
    }

    private static void closeProgram(FormController<?, ?> controller, boolean initializeMenu) {
        controller.hideWindow();
        if (initializeMenu) {
            MenuPanelController.initializeMenuPanelFrame();
        }
    }

    @Override
    public void windowDeactivated(WindowEvent e) {}

    @Override
    public void windowDeiconified(WindowEvent e) {}

    @Override
    public void windowIconified(WindowEvent e) {}

    @Override
    public void windowOpened(WindowEvent e) {}
}
