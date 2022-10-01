package com.github.scottswolfe.kathyscleaning.lbc.view;

import com.github.scottswolfe.kathyscleaning.component.CopyWorkersPanel;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.component.SubmitFormPanel;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class LBCHeaderPanel extends JPanel implements FocusableCollection {

    private final CopyWorkersPanel copyWorkersPanel;
    private final SubmitFormPanel submitFormPanel;

    public static LBCHeaderPanel from(
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final ActionListener submitFormListener
    ) {
        return new LBCHeaderPanel(
            onCopyWorkersButtonPress,
            submitFormListener
        );
    }

    private LBCHeaderPanel(
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final ActionListener submitFormListener
    ) {
        super();

        final RowLabelPanel lbcLabel = RowLabelPanel.from("LBC");

        copyWorkersPanel = CopyWorkersPanel.from(
            new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames()),
            LBCPanel.LBC_WORKER_ROW_COUNT,
            LBCPanel.LBC_WORKER_COLUMN_COUNT,
            onCopyWorkersButtonPress
        );

        submitFormPanel = SubmitFormPanel.from(submitFormListener);

        setLayout(new MigLayout("fillx, gapx 0, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);
        setBorder(BorderFactory.createLineBorder(null, 1));

        add(lbcLabel, "growx");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(copyWorkersPanel, "growx");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(submitFormPanel, "growx");

        connectFocusableComponents();
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return copyWorkersPanel.getWorkerSelectionGrid();
    }

    public void setWorkerSelectionGrid(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        copyWorkersPanel.setWorkers(workerSelectionGrid);
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(copyWorkersPanel, submitFormPanel)
        );
    }
}
