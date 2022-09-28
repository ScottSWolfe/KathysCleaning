package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.view.EditWorkersPanelLauncher;
import com.github.scottswolfe.kathyscleaning.utility.FormLauncher;

import javax.annotation.Nonnull;

/**
 * Listeners of CovenantPanel
 */
public class CovenantListeners {

    /**
     * Panel to which this is listening
     */
    CovenantPanel covPanel;

    /**
     * Listener for the Edit button.
     */
    public class EditListener implements ActionListener {
        public void actionPerformed ( ActionEvent e ) {
            EditWorkersPanelLauncher.from().launchPanel(
                covPanel.getWorkerNames(),
                GlobalData.getInstance().getDefaultWorkerNames(),
                CovenantPanel.ROWS,
                1,
                false,
                () -> {},
                CovenantListeners::onEditWorkersSubmit
            );
        }
    }

    private static void onEditWorkersSubmit(@Nonnull final List<String> updatedWorkerNames) {
        ApplicationCoordinator.getInstance().updateCovenantWorkers(updatedWorkerNames);
        ApplicationCoordinator.getInstance().refreshWindow();
    }

    /**
     * Listener for the Submit button.
     */
    public class SubmitListener implements ActionListener {

        public void actionPerformed (ActionEvent e) {
            FormLauncher.from().launchNextForm(Form.COVENANT);
        }
    }

    /**
     * @param covPanel the covPanel to set
     */
    public void setCovPanel(CovenantPanel covPanel) {
        this.covPanel = covPanel;
    }
}
