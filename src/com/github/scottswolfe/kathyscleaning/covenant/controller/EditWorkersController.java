package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.util.List;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.EditWorkersPanel;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

/**
 * Controller for the EditWorkersPanel
 */
public class EditWorkersController {

    /**
     * The panel that this controller controls.
     */
    private EditWorkersPanel editWorkersPanel;
    
    /**
     * The CovenantPanel this controller has frozen.
     */
    private CovenantPanel covPanel;

    public EditWorkersController(EditWorkersPanel editWorkersPanel,
            CovenantPanel covPanel) {
        this.editWorkersPanel = editWorkersPanel;
        this.covPanel = covPanel;
    }

    /**
     * Initialize and load edit workers panel and frame and freeze Covenant
     * Panel.
     */
    public static void initializePanelFrame(CovenantPanel covPanel) {
        
        JFrame editWorkerFrame = new JFrame();
        editWorkerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editWorkerFrame.setResizable(false);
        editWorkerFrame.addWindowListener(new FrameCloseListener(covPanel.getCovFrame()));
        
        EditWorkersPanel editWorkersPanel = new EditWorkersPanel(
            covPanel.getWorkerNames(),
            covPanel.getController().getCovModel().getDwd().getWorkerNames(),
            () -> onCancel(editWorkerFrame),
            (updatedWorkerNames) -> onSubmit(editWorkerFrame, covPanel, updatedWorkerNames)
        );
        
        editWorkerFrame.add(editWorkersPanel);
        editWorkerFrame.pack();
        StaticMethods.findSetLocation(editWorkerFrame);
        editWorkerFrame.setVisible(true);
    }

    private static void onCancel(JFrame frame) {
        frame.setVisible(false);
        frame.dispose();
    }

    private static void onSubmit(JFrame frame, CovenantPanel covPanel, List<String> updatedWorkerNames) {
        WorkerList workers = new WorkerList(updatedWorkerNames);

        // if contains repeat selections, notify user and end method
        if (StaticMethods.isRepeatWorker(workers)) {
            StaticMethods.shareRepeatWorker();
            return;
        }

        // change workers on CovenantPanel
        covPanel.getController().getCovModel().getDwd().setWorkers(workers);

        for (int i=0; i<workers.size(); i++) {
            covPanel.getNameLabels()[i].setText(workers.getName(i));
        }

        covPanel.getParent().revalidate();
        covPanel.getFrame().pack();
        covPanel.getParent().repaint();

        // close EditWorkersPanel
        frame.setVisible(false);
        frame.dispose();
    }
}
