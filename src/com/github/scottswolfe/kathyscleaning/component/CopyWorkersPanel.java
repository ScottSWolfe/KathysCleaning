package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JPanel;

public class CopyWorkersPanel extends JPanel {

    public static CopyWorkersPanel from(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final Runnable onCopyWorkersButtonPress,
        final Runnable onEditWorkersButtonPress
    ) {
        return new CopyWorkersPanel(
            workerList,
            rowCount,
            columnCount,
            onCopyWorkersButtonPress,
            onEditWorkersButtonPress
        );
    }

    private CopyWorkersPanel(
        final WorkerList workerList,
        int rowCount,
        int columnCount,
        final Runnable onCopyWorkersButtonPress,
        final Runnable onEditWorkersButtonPress
    ) {
        setLayout(new MigLayout());
        setBackground(Settings.HEADER_BACKGROUND);

        final WorkerSelectPanel workerSelectPanel = WorkerSelectPanel.from(
            workerList, rowCount, columnCount, Settings.HEADER_BACKGROUND
        );
        final KcButton copyWorkersButton = new KcButton("Copy", (e) -> onCopyWorkersButtonPress.run());
        final KcButton editWorkersButton = new KcButton("Edit", (e) -> onEditWorkersButtonPress.run());

        add(workerSelectPanel, "span 1 2, pushy");
        add(copyWorkersButton, "growx, pushy");
        add(editWorkersButton, "growx, pushy");
    }
}
