package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class CopyWorkersPanel extends JPanel implements FocusableCollection {

    private final WorkerSelectPanel workerSelectPanel;
    private final KcButton copyWorkersButton;

    public static CopyWorkersPanel from(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress
    ) {
        return new CopyWorkersPanel(
            workerList,
            rowCount,
            columnCount,
            onCopyWorkersButtonPress
        );
    }

    private CopyWorkersPanel(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress
    ) {
        workerSelectPanel = WorkerSelectPanel.from(
            workerList, rowCount, columnCount, Settings.HEADER_BACKGROUND
        );
        copyWorkersButton = new KcButton(
            "Copy",
            (event) -> onCopyWorkersButtonPress.accept(workerSelectPanel.getWorkerSelectionGrid())
        );

        setLayout(new MigLayout());
        setBackground(Settings.HEADER_BACKGROUND);
        add(workerSelectPanel, "pushy");
        add(copyWorkersButton, "growx, pushy");

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

    public void setWorkers(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        workerSelectPanel.setWorkers(workerSelectionGrid);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Arrays.asList(workerSelectPanel, copyWorkersButton));
    }
}
