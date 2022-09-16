package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.EditWorkersPanelLauncher;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EditableWorkerSelectPanel extends JPanel implements FocusableCollection {

    private final WorkerSelectPanel workerSelectPanel;
    private final KcButton editWorkersButton;

    public static EditableWorkerSelectPanel from(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final WindowListener popUpWindowListener
    ) {
        return new EditableWorkerSelectPanel(
            workerList,
            rowCount,
            columnCount,
            popUpWindowListener
        );
    }

    private EditableWorkerSelectPanel(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final WindowListener popUpWindowListener
    ) {
        workerSelectPanel = WorkerSelectPanel.from(
            workerList, rowCount, columnCount, Settings.BACKGROUND_COLOR
        );
        editWorkersButton = new KcButton(
            "Edit",
            (event) -> EditWorkersPanelLauncher.from().launchPanel(
                workerSelectPanel.getWorkers().getWorkerNames(),
                workerList.getWorkerNames(),
                rowCount,
                columnCount,
                false,
                () -> {},
                workerSelectPanel::updateWorkerNames,
                popUpWindowListener
            )
        );

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);
        add(workerSelectPanel, "pushy");
        add(editWorkersButton, "growx, pushy");

        connectFocusableComponents();
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return workerSelectPanel.getWorkerSelectionGrid();
    }

    public void setWorkerSelectionGrid(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        workerSelectPanel.setWorkers(workerSelectionGrid);
    }

    public WorkerList getWorkers() {
        return workerSelectPanel.getWorkers();
    }

    public void setWorkers(final WorkerList workers) {
        workerSelectPanel.setWorkers(workers);
    }

    public void setWorkerSelections(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        workerSelectPanel.setWorkers(workerSelectionGrid);
    }

    public void setWorkers(final List<List<String>> workerNames) {
        workerSelectPanel.updateWorkerNames(workerNames);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Arrays.asList(workerSelectPanel, editWorkersButton));
    }
}
