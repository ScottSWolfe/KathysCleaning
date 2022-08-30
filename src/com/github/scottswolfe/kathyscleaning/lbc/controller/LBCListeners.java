package com.github.scottswolfe.kathyscleaning.lbc.controller;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.EditWorkersPanelLauncher;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.lbc.view.LBCPanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Listeners of LBCPanel
 */
public class LBCListeners {

/* FIELDS =================================================================== */

    /**
     * TODO temporary
     */
    LBCModel lbcModel;

    /**
     * Panel to which this is listening
     */
    LBCPanel lbcPanel;

    private GeneralController<LBCPanel, LBCModel> controller;


/* LISTENERS ================================================================ */

    /**
     * Listener for the Edit button.
     */
    public class EditListener implements ActionListener {
        public void actionPerformed (ActionEvent e) {
            lbcPanel.getFrame().setEnabled(false);

            EditWorkersPanelLauncher.from().launchPanel(
                lbcPanel.getWorkerNames(),
                lbcPanel.getController().getLbcModel().getDwd().getWorkerNames(),
                false,
                () -> {}, (updateWorkerNames) -> onEditWorkersSubmit(lbcPanel, updateWorkerNames),
                new FrameCloseListener(lbcPanel.getFrame())
            );
        }
    }

    private static void onEditWorkersSubmit(LBCPanel lbcPanel, List<List<String>> updatedWorkerNames) {

        // We know that updatedWorkerNames is a single column with many rows
        final List<String> flatListUpdatedWorkerNames = updatedWorkerNames.stream()
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

        if (StaticMethods.isRepeatWorker(flatListUpdatedWorkerNames)) {
            StaticMethods.shareRepeatWorker();
            throw new IllegalArgumentException("Cannot submit the same worker name more than once");
        }

        WorkerList workers = new WorkerList(flatListUpdatedWorkerNames);
        lbcPanel.getController().getLbcModel().getDwd().setWorkers(workers);
        for (int i=0; i<workers.size(); i++) {
            lbcPanel.getNameLabels()[i].setText(workers.getName(i));
        }

        lbcPanel.getParent().revalidate();
        lbcPanel.getFrame().pack();
        lbcPanel.getParent().repaint();
    }

    /**
     * Listener for the Submit button.
     */
    public class SubmitListener implements ActionListener {

        public void actionPerformed (ActionEvent e) {
            controller.readInputAndWriteToFile(null);
            LBCControllerHelper.saveChosenWorkers(lbcPanel, lbcModel);
            LBCControllerHelper.saveAmountsEarned(lbcPanel);

            lbcPanel.getFrame().setVisible(false);
            lbcPanel.getFrame().dispose();

            GeneralController<WeekendPanel, WeekendModel> weekendController = new GeneralController<>(Form.WEEKEND);
            weekendController.initializeForm();
        }
    }



/* GETTERS/SETTERS ========================================================== */

    /**
     * @return the lbcModel
     */
    public LBCModel getLbcModel() {
        return lbcModel;
    }

    /**
     * @param lbcModel the lbcModel to set
     */
    public void setLbcModel(LBCModel lbcModel) {
        this.lbcModel = lbcModel;
    }

    /**
     * @return the lbcPanel
     */
    public LBCPanel getLbcPanel() {
        return lbcPanel;
    }

    /**
     * @param lbcPanel the lbcPanel to set
     */
    public void setLbcPanel(LBCPanel lbcPanel) {
        this.lbcPanel = lbcPanel;
    }

    public void setController(GeneralController<LBCPanel, LBCModel> controller) {
        this.controller = controller;
    }

}
