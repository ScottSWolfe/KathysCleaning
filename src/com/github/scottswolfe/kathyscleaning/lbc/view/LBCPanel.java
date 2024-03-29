package com.github.scottswolfe.kathyscleaning.lbc.view;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JPanel;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This is the panel where the user enters beginning and ending times for workers at LBC.
 */
public class LBCPanel extends JPanel implements FocusableCollection {

    public static final int LBC_WORKER_ROW_COUNT = 2;
    public static final int LBC_WORKER_COLUMN_COUNT = 7;

    private final LBCHeaderPanel lbcHeaderPanel;
    private final LBCDaysPanel lbcDaysPanel;

    public static LBCPanel from(final ActionListener submitFormListener) {
        return new LBCPanel(submitFormListener);
    }

    private LBCPanel(final ActionListener submitFormListener) {
        super();

        lbcHeaderPanel = LBCHeaderPanel.from(
            this::setWorkerSelectionsForAllDays,
            submitFormListener
        );
        lbcDaysPanel = LBCDaysPanel.from();

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);
        add(lbcHeaderPanel, "grow, wrap");
        add(lbcDaysPanel, "grow");

        connectFocusableComponents();
    }

    public List<List<Pair<String, Boolean>>> getHeaderWorkerSelectionGrid() {
        return lbcHeaderPanel.getWorkerSelectionGrid();
    }

    public void setHeaderWorkerSelectionGrid(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        lbcHeaderPanel.setWorkerSelectionGrid(workerSelectionGrid);
    }

    public List<LBCDayPanel> getLBCDayPanels() {
        return lbcDaysPanel.getLBCDayPanels();
    }

    private void setWorkerSelectionsForAllDays(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        for (int index = 0; index < lbcDaysPanel.getLBCDayPanels().size(); index++) {
            lbcDaysPanel.getLBCDayPanels().get(index).setWorkers(workerSelectionGrid);
        }
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Arrays.asList(
            Collections.singletonList(lbcHeaderPanel),
            Collections.singletonList(lbcDaysPanel)
        );
    }
}
