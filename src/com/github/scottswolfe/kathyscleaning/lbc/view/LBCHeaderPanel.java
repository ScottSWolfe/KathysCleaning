package com.github.scottswolfe.kathyscleaning.lbc.view;

import com.github.scottswolfe.kathyscleaning.component.CopyWorkersPanel;
import com.github.scottswolfe.kathyscleaning.component.SubmitFormPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class LBCHeaderPanel extends JPanel implements FocusableCollection {

    private final CopyWorkersPanel copyWorkersPanel;
    private final SubmitFormPanel submitFormPanel;

    public static LBCHeaderPanel from(
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final ActionListener submitFormListener,
        final WindowListener popUpWindowListener
    ) {
        return new LBCHeaderPanel(
            onCopyWorkersButtonPress,
            submitFormListener,
            popUpWindowListener
        );
    }

    private LBCHeaderPanel(
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final ActionListener submitFormListener,
        final WindowListener popUpWindowListener
    ) {
        super();

        copyWorkersPanel = CopyWorkersPanel.from(
            new WorkerList(WorkerList.LBC_WORKERS),
            LBCPanel.LBC_WORKER_ROW_COUNT,
            LBCPanel.LBC_WORKER_COLUMN_COUNT,
            onCopyWorkersButtonPress,
            popUpWindowListener
        );

        submitFormPanel = SubmitFormPanel.from(submitFormListener);

        setLayout(new MigLayout("fill, insets 0", "[grow][][grow]", "[grow]"));
        setBackground(Settings.BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(null, 1));

        add(copyWorkersPanel, "center");
        add(new JSeparator(SwingConstants.VERTICAL), "grow, center, gapx 10 10");
        add(submitFormPanel, "center");

        connectFocusableComponents();
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return copyWorkersPanel.getWorkerSelectionGrid();
    }

    public void setWorkerSelectionGrid(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        copyWorkersPanel.setWorkers(workerSelectionGrid);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(copyWorkersPanel, submitFormPanel)
        );
    }
}
