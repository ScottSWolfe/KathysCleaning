package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.covenant.view.EditWorkersPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

/**
 * Controller for the EditWorkersPanel
 */
public class EditWorkersController {

/* FIELDS =================================================================== */
    
    /**
     * The panel that this controller controls.
     */
    private EditWorkersPanel editWorkersPanel;
    
    /**
     * The CovenantPanel this controller has frozen.
     */
    private CovenantPanel covPanel;
    
    
    
    
/* CONSTRUCTOR ============================================================== */
    
    public EditWorkersController(EditWorkersPanel editWorkersPanel,
            CovenantPanel covPanel) {
        this.editWorkersPanel = editWorkersPanel;
        this.covPanel = covPanel;
    }
    
    
    
    
/* LISTENERS ================================================================ */
    
    /**
     * Listener for the Cancel button.
     */
    public class CancelListener implements ActionListener {
        
        public void actionPerformed(ActionEvent e){
                    
            editWorkersPanel.getFrame().setVisible(false);
            editWorkersPanel.getFrame().dispose();
        }
    }
    
    
    /**
     * Listener for the Submit button.
     */
    public class SubmitListener implements ActionListener {

        public void actionPerformed(ActionEvent e){
            
            WorkerList workers =
                    new WorkerList(editWorkersPanel.getSelectedWorkers());
            
            // if contains repeat selections, notify user and end method
            if (StaticMethods.isRepeatWorker(workers)) {  
                StaticMethods.shareRepeatWorker();
                return;
            }
                        
            // change workers on CovenantPanel
            covPanel.getController().getCovModel().getDwd().setWorkers(workers);
            
            for (int i=0; i<workers.size(); i++) {
                covPanel.getNameLabels()[i].setText(workers.get(i));
            }
            
            covPanel.getParent().revalidate();
            covPanel.getParent().repaint();
            
            // close EditWorkersPanel
            editWorkersPanel.getFrame().setVisible(false);
            editWorkersPanel.getFrame().dispose();
            
        }
        
    }
    
    
    
    
    /* NOT BEING USED
    public class EditWorkersWindowListener implements WindowListener {
        
        @Override
        public void windowActivated(WindowEvent arg0) {
            
        }
    
        @Override
        public void windowClosed(WindowEvent arg0) {
            
        }
    
        @Override
        public void windowClosing(WindowEvent arg0) {
            
        }
    
        @Override
        public void windowDeactivated(WindowEvent arg0) {
            
        }
    
        @Override
        public void windowDeiconified(WindowEvent arg0) {
            
        }
    
        @Override
        public void windowIconified(WindowEvent arg0) {
            
        }
    
        @Override
        public void windowOpened(WindowEvent arg0) {
            
        }
    
    }
    */
    
/* PUBLIC METHODS =========================================================== */   
    
    /**
     * Initialize and load edit workers panel and frame and freeze Covenant
     * Panel.
     */
    public static void initializePanelFrame(CovenantPanel covPanel) {
        
        JFrame editWorkerFrame = new JFrame();
        editWorkerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editWorkerFrame.setResizable(false);
        editWorkerFrame.addWindowListener(
                new FrameCloseListener(covPanel.getCovFrame()));
        
        EditWorkersPanel editWorkersPanel =
                new EditWorkersPanel(editWorkerFrame, covPanel);
        
        editWorkerFrame.add(editWorkersPanel);
        editWorkerFrame.pack();
        StaticMethods.findSetLocation(editWorkerFrame);
        editWorkerFrame.setVisible(true);
    }
    
}
