package com.github.scottswolfe.kathyscleaning.lbc.view;

import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.component.KcButton;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.ExceptionsPanelLauncher;
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

public class LBCDayPanel extends JPanel implements FocusableCollection {

    private final ExceptionData exceptionData;

    private final RowLabelPanel dayOfWeekLabelPanel;
    private final WorkerSelectPanel workerSelectPanel;
    private final KcButton exceptionsButton;

    public static LBCDayPanel from(
        final DayOfWeek dayOfWeek,
        final WindowListener popUpWindowListener
    ) {
        return new LBCDayPanel(dayOfWeek, popUpWindowListener);
    }

    private LBCDayPanel(
        final DayOfWeek dayOfWeek,
        final WindowListener popUpWindowListener
    ) {
        super();

        final WorkerList workerList = new WorkerList(WorkerList.LBC_WORKERS);

        exceptionData = new ExceptionData();

        dayOfWeekLabelPanel = RowLabelPanel.from(dayOfWeek.getName());
        workerSelectPanel = WorkerSelectPanel.from(workerList, Settings.BACKGROUND_COLOR);
        exceptionsButton = new KcButton(
            "Exceptions",
            (event) -> ExceptionsPanelLauncher.from().launchPanel(
                workerList,
                () -> this.exceptionData,
                () -> {},
                this::setExceptionData,
                popUpWindowListener
            )
        );

        setLayout(new MigLayout());
        add(dayOfWeekLabelPanel);
        add(workerSelectPanel);
        add(exceptionsButton);

        connectFocusableComponents();
    }

    public void setWorkers(final List<List<Pair<String, Boolean>>> workerSelectionGrid) {
        workerSelectPanel.setWorkers(workerSelectionGrid);
    }

    private void setExceptionData(final ExceptionData exceptionData) {
        this.exceptionData.setEntries(exceptionData.getEntries());
        if (exceptionData.isException()) {
            exceptionsButton.setBackground(Settings.EDITED_BUTTON_COLOR);
        } else {
            exceptionsButton.setBackground(Settings.DEFAULT_BUTTON_COLOR);
        }
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(dayOfWeekLabelPanel, workerSelectPanel, exceptionsButton)
        );
    }
}
