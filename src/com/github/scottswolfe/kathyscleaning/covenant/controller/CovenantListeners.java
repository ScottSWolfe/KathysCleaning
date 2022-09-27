package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.EditWorkersPanelLauncher;
import com.github.scottswolfe.kathyscleaning.utility.FormLauncher;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

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
            covPanel.getCovFrame().setEnabled(false);

            EditWorkersPanelLauncher.from().launchPanel(
                covPanel.getWorkerNames(),
                GlobalData.getInstance().getDefaultWorkerNames(),
                false,
                () -> {}, (updateWorkerNames) -> onEditWorkersSubmit(covPanel, updateWorkerNames),
                new FrameCloseListener(covPanel.getCovFrame())
            );
        }
    }

    private static void onEditWorkersSubmit(CovenantPanel covPanel, List<List<String>> updatedWorkerNames) {

        // We know that updatedWorkerNames is a single column with many rows
        final List<String> flatListUpdatedWorkerNames = updatedWorkerNames.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if (StaticMethods.isRepeatWorker(flatListUpdatedWorkerNames)) {
            StaticMethods.shareRepeatWorker();
            throw new IllegalArgumentException("Cannot submit the same worker name more than once");
        }

        WorkerList workers = new WorkerList(flatListUpdatedWorkerNames);
        for (int i = 0; i < workers.size(); i++) {
            covPanel.getNameLabels()[i].setText(workers.getName(i));
        }

        SharedDataManager.getInstance().setCovenantWorkerNames(flatListUpdatedWorkerNames);

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
