package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CopyWorkersPanel extends JPanel implements FocusableCollection {

    private final WorkerSelectPanel workerSelectPanel;
    private final KcButton copyWorkersButton;
    private final KcButton editWorkersButton;

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

        workerSelectPanel = WorkerSelectPanel.from(
            workerList, rowCount, columnCount, Settings.HEADER_BACKGROUND
        );
        copyWorkersButton = new KcButton("Copy", (e) -> onCopyWorkersButtonPress.run());
        editWorkersButton = new KcButton("Edit", (e) -> onEditWorkersButtonPress.run());

        add(workerSelectPanel, "span 1 2, pushy");
        add(copyWorkersButton, "growx, pushy");
        add(editWorkersButton, "growx, pushy");

        connectFocusableComponents();
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(Arrays.asList(workerSelectPanel, copyWorkersButton, editWorkersButton));
    }
}
