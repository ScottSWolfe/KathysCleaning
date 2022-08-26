package com.github.scottswolfe.kathyscleaning.completed.view;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.completed.controller.AddHouseListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.DeleteHouseListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.ExceptionListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.HouseNameDocumentListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.MoveDownListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.MoveUpListener;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionEntry;
import com.github.scottswolfe.kathyscleaning.component.AmountEarnedPanel;
import com.github.scottswolfe.kathyscleaning.component.HouseNamePanel;
import com.github.scottswolfe.kathyscleaning.component.HouseRowButtonPanel;
import com.github.scottswolfe.kathyscleaning.component.KcButton;
import com.github.scottswolfe.kathyscleaning.component.TimeRangePanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class HousePanel extends JPanel implements FocusableCollection {

    public static final int WORKER_SELECTION_ROW_COUNT = 2;
    public static final int WORKER_SELECTION_COLUMN_COUNT = 5;

    private final JFrame parentFrame;
    private final TabbedPane parentTabbedPane;
    private final DayPanel parentDayPanel;

    private final HouseNamePanel houseNamePanel;
    private final AmountEarnedPanel amountEarnedPanel;
    private final TimeRangePanel timePanel;
    private final WorkerSelectPanel workerSelectPanel;
    public final KcButton exceptionsButton;
    private final HouseRowButtonPanel houseRowButtonPanel;

    private ExceptionData exceptionData;

    public static HousePanel from(
        final JFrame parentFrame,
        final TabbedPane parentTabbedPane,
        final DayPanel parentDayPanel
    ) {
        return new HousePanel(parentDayPanel, parentFrame, parentTabbedPane);
    }

    public HousePanel(DayPanel parentDayPanel, JFrame parentFrame, TabbedPane parentTabbedPane) {

        this.parentDayPanel = parentDayPanel;
        this.parentFrame = parentFrame;
        this.parentTabbedPane = parentTabbedPane;

        // TODO temporary hack
        final WorkerList workerList = new WorkerList(WorkerList.HOUSE_WORKERS);

        exceptionData = new ExceptionData();

        setLayout(new MigLayout("insets 0","[grow][grow][grow][grow][grow][grow]","[]"));
        setBackground(Settings.BACKGROUND_COLOR);
        setBorder(BorderFactory.createTitledBorder(new String()));

        houseNamePanel = HouseNamePanel.from(new HouseNameDocumentListener(this));
        amountEarnedPanel = AmountEarnedPanel.from();
        timePanel = TimeRangePanel.from();
        workerSelectPanel = WorkerSelectPanel.from(
            workerList,
            WORKER_SELECTION_ROW_COUNT,
            WORKER_SELECTION_COLUMN_COUNT,
            Settings.BACKGROUND_COLOR
        );
        exceptionsButton = new KcButton(
            "Exceptions",
            new ExceptionListener(
                workerList,
                parentFrame,
                () -> this.exceptionData,
                this::setExceptionData
            )
        );
        houseRowButtonPanel = HouseRowButtonPanel.from(
            new MoveUpListener(parentDayPanel,this, workerList, parentFrame, parentTabbedPane),
            new MoveDownListener(parentDayPanel,this, workerList, parentFrame, parentTabbedPane),
            new AddHouseListener(parentDayPanel,this, workerList, parentFrame, parentTabbedPane),
            new DeleteHouseListener(parentDayPanel,this, workerList, parentFrame, parentTabbedPane)
        );

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

    public HousePanel copyPanel( ) {

        HousePanel new_panel = HousePanel.from(this.parentFrame, this.parentTabbedPane, this.parentDayPanel);

        new_panel.setHouseNameText(this.getHouseNameText());
        new_panel.amountEarnedPanel.setAmountEarnedText(this.amountEarnedPanel.getAmountEarnedText());
        new_panel.setBeginTimeText(getBeginTimeText());
        new_panel.setEndTimeText(getEndTimeText());
        new_panel.exceptionData = this.exceptionData;

        // temporary hack
        WorkerList workers = this.parentDayPanel.header_panel.getWorkers();
        new_panel.workerSelectPanel.setWorkers(workers);

        return new_panel;
    }

    public HousePanel changeHouseWorkers(WorkerList dwd) {
        HousePanel new_panel = HousePanel.from(this.parentFrame, this.parentTabbedPane, this.parentDayPanel);
        new_panel.setHouseNameText(this.getHouseNameText());
        new_panel.amountEarnedPanel.setAmountEarnedText(this.amountEarnedPanel.getAmountEarnedText());
        new_panel.setBeginTimeText(this.getBeginTimeText());
        new_panel.setEndTimeText(this.getEndTimeText());
        new_panel.setWorkerList(dwd);
        return new_panel;
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

    public void setWorkerList(final WorkerList workerList) {
        workerSelectPanel.setWorkers(workerList);
    }

    public List<String> getSelectedWorkerNames() {
        return workerSelectPanel.getSelectedWorkerNames();
    }

    public void setSelectedWorkerNames(final List<String> selectedWorkerNames) {
        workerSelectPanel.setSelected(selectedWorkerNames);
    }

    /**
     * Sets the exception button to a new color if an exception exists
     *
     * @param isException true if there is an exception, otherwise false
     */
    public void setExceptionButtonColor(boolean isException) {
        exceptionsButton.setForeground(Settings.MAIN_COLOR);
    }

    public ExceptionData getExceptionData() {
        return exceptionData;
    }

    public void setExceptionData(ExceptionData exceptionData) {
        this.exceptionData = exceptionData;
        setExceptionButtonColor();
    }

    public void setExceptionDataEntries(List<ExceptionEntry> entries) {
        exceptionData.setEntries(entries);
        setExceptionButtonColor();
    }

    public boolean hasExceptionData() {
        return exceptionData.isException();
    }

    private void setExceptionButtonColor() {
        if (exceptionData.isException()) {
            exceptionsButton.setBackground(Settings.EDITED_BUTTON_COLOR);
        } else {
            exceptionsButton.setBackground(Settings.DEFAULT_BUTTON_COLOR);
        }
    }

    public JFrame getParentFrame() {
        return this.parentFrame;
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
