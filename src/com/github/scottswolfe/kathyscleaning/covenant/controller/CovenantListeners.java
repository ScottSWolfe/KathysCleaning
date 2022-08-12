package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.EditWorkersPanelLauncher;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;

/**
 * Listeners of CovenantPanel 
 */
public class CovenantListeners {

/* FIELDS =================================================================== */
    
    /**
     * TODO temporary
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

            EditWorkersPanelLauncher.launchPanel(
                covPanel.getWorkerNames(),
                covPanel.getController().getCovModel().getDwd().getWorkerNames(),
                (updateWorkerNames) -> onEditWorkersSubmit(covPanel, updateWorkerNames),
                () -> {},
                new FrameCloseListener(covPanel.getCovFrame())
            );
        }
    }

    private static void onEditWorkersSubmit(CovenantPanel covPanel, List<String> updatedWorkerNames) {
        if (StaticMethods.isRepeatWorker(updatedWorkerNames)) {
            StaticMethods.shareRepeatWorker();
            return;
        }

        WorkerList workers = new WorkerList(updatedWorkerNames);
        covPanel.getController().getCovModel().getDwd().setWorkers(workers);
        for (int i=0; i<workers.size(); i++) {
            covPanel.getNameLabels()[i].setText(workers.getName(i));
        }

        covPanel.getParent().revalidate();
        covPanel.getFrame().pack();
        covPanel.getParent().repaint();
    }

    /**
     * Listener for the Submit button.
     */
    public class SubmitListener implements ActionListener {
        
        public void actionPerformed (ActionEvent e) {            
            controller.readInputAndWriteToFile(null);
            CovenantControllerHelper.saveChosenWorkers(covPanel, covModel);
            CovenantControllerHelper.saveAmountsEarned(covPanel);

            covPanel.getFrame().setVisible(false);
            covPanel.getFrame().dispose();

            GeneralController<WeekendPanel, WeekendModel> weekendController = new GeneralController<>(Form.LBC);
            weekendController.initializeForm(weekendController);
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
