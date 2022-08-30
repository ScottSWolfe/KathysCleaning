package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
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
import java.util.function.Consumer;

public class CopyWorkersPanel extends JPanel implements FocusableCollection {

    private final WorkerSelectPanel workerSelectPanel;
    private final KcButton copyWorkersButton;
    private final KcButton editWorkersButton;

    public static CopyWorkersPanel from(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final WindowListener popUpWindowListener
    ) {
        return new CopyWorkersPanel(
            workerList,
            rowCount,
            columnCount,
            onCopyWorkersButtonPress,
            popUpWindowListener
        );
    }

    private CopyWorkersPanel(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final WindowListener popUpWindowListener
    ) {
        workerSelectPanel = WorkerSelectPanel.from(
            workerList, rowCount, columnCount, Settings.HEADER_BACKGROUND
        );
        copyWorkersButton = new KcButton(
            "Copy",
            (event) -> onCopyWorkersButtonPress.accept(workerSelectPanel.getWorkerSelectionGrid())
        );
        editWorkersButton = new KcButton(
            "Edit",
            (event) -> EditWorkersPanelLauncher.from().launchPanel(
                workerSelectPanel.getWorkers().getWorkerNames(),
                new WorkerList(WorkerList.HOUSE_WORKERS).getWorkerNames(),
                HousePanel.WORKER_SELECTION_ROW_COUNT,
                HousePanel.WORKER_SELECTION_COLUMN_COUNT,
                false,
                () -> {},
                workerSelectPanel::updateWorkerNames,
                popUpWindowListener
            )
        );

        setLayout(new MigLayout());
        setBackground(Settings.HEADER_BACKGROUND);
        add(workerSelectPanel, "span 1 2, pushy");
        add(copyWorkersButton, "growx, pushy");
        add(editWorkersButton, "growx, pushy");

        connectFocusableComponents();
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return workerSelectPanel.getWorkerSelectionGrid();
    }

    public WorkerList getWorkers() {
        return workerSelectPanel.getWorkers();
    }

    public void setWorkers(final WorkerList workers) {
        workerSelectPanel.setWorkers(workers);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Arrays.asList(workerSelectPanel, copyWorkersButton, editWorkersButton));
    }
}
