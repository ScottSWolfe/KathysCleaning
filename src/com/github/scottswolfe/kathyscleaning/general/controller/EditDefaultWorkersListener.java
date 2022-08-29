package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.EditWorkersPanelLauncher;

public class EditDefaultWorkersListener implements ActionListener {

    private final JFrame parentFrame;
    private final DayPanel dayPanel;

	public EditDefaultWorkersListener(final JFrame parentFrame, final DayPanel dayPanel) {
        this.parentFrame = parentFrame;
		this.dayPanel = dayPanel;
	}

	public void actionPerformed(ActionEvent e){
        EditWorkersPanelLauncher.from().launchPanel(
            dayPanel.header_panel.getWorkers().getWorkerNames(),
            new WorkerList(WorkerList.HOUSE_WORKERS).getWorkerNames(),
            HousePanel.WORKER_SELECTION_ROW_COUNT,
            HousePanel.WORKER_SELECTION_COLUMN_COUNT,
            false,
            () -> {},
            (workerSelections) -> dayPanel.header_panel.dwp.updateWorkerNames(workerSelections),
            new FrameCloseListener(parentFrame)
        );
	}
}
