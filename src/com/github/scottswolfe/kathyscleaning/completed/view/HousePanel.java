package com.github.scottswolfe.kathyscleaning.completed.view;

import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.completed.controller.HouseNameDocumentListener;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.component.AmountEarnedPanel;
import com.github.scottswolfe.kathyscleaning.component.Button;
import com.github.scottswolfe.kathyscleaning.component.HouseNamePanel;
import com.github.scottswolfe.kathyscleaning.component.HouseRowButtonPanel;
import com.github.scottswolfe.kathyscleaning.component.TimeRangePanel;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.general.view.ExceptionsPanelLauncher;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class HousePanel extends JPanel implements FocusableCollection {

    public static final int WORKER_SELECTION_ROW_COUNT = 2;
    public static final int WORKER_SELECTION_COLUMN_COUNT = 7;

    private final HouseNamePanel houseNamePanel;
    private final AmountEarnedPanel amountEarnedPanel;
    private final TimeRangePanel timePanel;
    private final WorkerSelectPanel workerSelectPanel;
    private final Button exceptionsButton;
    private final HouseRowButtonPanel houseRowButtonPanel;

    private ExceptionData exceptionData;

    public static HousePanel from(
        final Function<HousePanel, ActionListener> buildMoveUpActionListener,
        final Function<HousePanel, ActionListener> buildMoveDownActionListener,
        final Function<HousePanel, ActionListener> buildAddHouseActionListener,
        final Function<HousePanel, ActionListener> buildDeleteHouseActionListener
    ) {
        return new HousePanel(
            buildMoveUpActionListener,
            buildMoveDownActionListener,
            buildAddHouseActionListener,
            buildDeleteHouseActionListener
        );
    }

    private HousePanel(
        final Function<HousePanel, ActionListener> buildMoveUpActionListener,
        final Function<HousePanel, ActionListener> buildMoveDownActionListener,
        final Function<HousePanel, ActionListener> buildAddHouseActionListener,
        final Function<HousePanel, ActionListener> buildDeleteHouseActionListener
    ) {
        exceptionData = new ExceptionData();

        houseNamePanel = HouseNamePanel.from(new HouseNameDocumentListener(this));
        amountEarnedPanel = AmountEarnedPanel.from();
        timePanel = TimeRangePanel.from();
        workerSelectPanel = WorkerSelectPanel.from(
            SharedDataManager.getInstance().getAvailableWorkerNames(),
            WORKER_SELECTION_ROW_COUNT,
            WORKER_SELECTION_COLUMN_COUNT,
            Settings.BACKGROUND_COLOR
        );
        exceptionsButton = Button.from(
            "Exceptions",
            Settings.QUIET_BUTTON_COLORS,
            () -> ExceptionsPanelLauncher.from().launchPanel(
                GlobalData.getInstance().getDefaultWorkerNames(),
                () -> this.exceptionData,
                () -> {},
                this::setExceptionData
            )
        );
        houseRowButtonPanel = HouseRowButtonPanel.from(
            buildMoveUpActionListener.apply(this),
            buildMoveDownActionListener.apply(this),
            buildAddHouseActionListener.apply(this),
            buildDeleteHouseActionListener.apply(this)
        );

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);
        setBorder(BorderFactory.createTitledBorder(""));

        add(houseNamePanel, "growy");
        add(amountEarnedPanel, "growy");
        add(timePanel, "growy");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(workerSelectPanel, "pushy");
        add(exceptionsButton, "hmin 50, pushy");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(houseRowButtonPanel, "growy");

        connectFocusableComponents();
    }

    public String getHouseNameText() {
        return houseNamePanel.getHouseNameText();
    }

    public void setHouseNameText(final String houseName) {
        houseNamePanel.setHouseNameText(houseName);
    }

    public String getAmountEarnedText() {
        return amountEarnedPanel.getAmountEarnedText();
    }

    public void setAmountEarnedText(final String newText) {
        amountEarnedPanel.setAmountEarnedText(newText);
    }

    public String getBeginTimeText() {
        return timePanel.getBeginTimeText();
    }

    public void setBeginTimeText(final String beginTime) {
        timePanel.setBeginTimeText(beginTime);
    }

    public String getEndTimeText() {
        return timePanel.getEndTimeText();
    }

    public void setEndTimeText(final String endTime) {
        timePanel.setEndTimeText(endTime);
    }

    public void setWorkers(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        workerSelectPanel.setWorkers(workerSelectionGrid);
    }

    public WorkerList getWorkerList() {
        return workerSelectPanel.getWorkers();
    }

    public List<String> getSelectedWorkerNames() {
        return workerSelectPanel.getSelectedWorkerNames();
    }

    public ExceptionData getExceptionData() {
        return exceptionData;
    }

    public void setExceptionData(ExceptionData exceptionData) {
        this.exceptionData = exceptionData;
        setExceptionButtonColor();
    }

    private void setExceptionButtonColor() {
        if (exceptionData.isException()) {
            exceptionsButton.setColors(Settings.LOUD_BUTTON_COLORS);
        } else {
            exceptionsButton.setColors(Settings.QUIET_BUTTON_COLORS);
        }
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(
                houseNamePanel,
                amountEarnedPanel,
                timePanel,
                workerSelectPanel,
                exceptionsButton,
                houseRowButtonPanel
            )
        );
    }
}
