package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendController;

/**
 * Listeners of CovenantPanel 
 */
public class CovenantListeners {

/* FIELDS =================================================================== */
    
    /**
     * TODO temporary until CovenantControllerHelper and ExcelHelper are done
     */
    CovenantModel covModel;
    
    /**
     * Panel to which this is listening
     */
    CovenantPanel covPanel;
    
    private GeneralController<CovenantPanel, CovenantModel> controller;

    
/* CONSTRUCTORS ============================================================= */

    
/* LISTENERS ================================================================ */
    
    /**
     * Listener for the Edit button.
     */
    public class EditListener implements ActionListener {
        public void actionPerformed ( ActionEvent e ) {
            covPanel.getCovFrame().setEnabled(false);
            EditWorkersController.initializePanelFrame(covPanel);
        }
    }
    
    /**
     * Listener for the Submit button.
     */
    public class SubmitListener implements ActionListener {
        
        public void actionPerformed (ActionEvent e) {
            if (!StaticMethods.confirmSubmitWeek()) {
                return;
            }
            controller.readInputAndWriteToFile(Settings.saveFile);
            WeekendController controller = new WeekendController();
            controller.initializeWeekendPanelFrame(covPanel.getCovFrame(),
                                                   covModel.getDate(),
                                                   covModel.getMode(),
                                                   covModel.getWk());
        }
    }
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * @return the covModel
     */
    public CovenantModel getCovModel() {
        return covModel;
    }

    /**
     * @param covModel the covModel to set
     */
    public void setCovModel(CovenantModel covModel) {
        this.covModel = covModel;
    }

    /**
     * @return the covPanel
     */
    public CovenantPanel getCovPanel() {
        return covPanel;
    }

    /**
     * @param covPanel the covPanel to set
     */
    public void setCovPanel(CovenantPanel covPanel) {
        this.covPanel = covPanel;
    }
    
    public void setController(GeneralController<CovenantPanel, CovenantModel> controller) {
        this.controller = controller;
    }
    
}
