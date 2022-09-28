package com.github.scottswolfe.kathyscleaning.scheduled.view;

import com.github.scottswolfe.kathyscleaning.component.ButtonPanel;
import com.github.scottswolfe.kathyscleaning.component.MeetTimePanel;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.GenericPanelLauncher;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCExceptions;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScheduledLBCPanel extends JPanel implements FocusableCollection {

    public static final int LBC_SCHEDULED_WORKER_ROW_COUNT = 2;
    public static final int LBC_SCHEDULED_WORKER_COLUMN_COUNT = 7;

    public static final int LBC_SCHEDULED_EXCEPTIONS_COUNT = 3;

    private final MeetTimePanel meetTimePanel;
    private final WorkerSelectPanel workerSelectPanel;
    private final ButtonPanel exceptionsButtonPanel;

    private ScheduledLBCExceptions scheduledLBCExceptions;

    public static ScheduledLBCPanel from(final Border border) {
        return new ScheduledLBCPanel(border);
    }

    private ScheduledLBCPanel(
        final Border border
    ) {
        scheduledLBCExceptions = ScheduledLBCExceptions.from();

        final RowLabelPanel rowLabelPanel = RowLabelPanel.from("LBC");
        meetTimePanel = MeetTimePanel.from();
        workerSelectPanel = WorkerSelectPanel.from(
            new WorkerList(GlobalData.getInstance().getDefaultWorkerNames()),
            LBC_SCHEDULED_WORKER_ROW_COUNT,
            LBC_SCHEDULED_WORKER_COLUMN_COUNT,
            Settings.BACKGROUND_COLOR
        );
        exceptionsButtonPanel = ButtonPanel.from(
            "Exceptions",
            () -> GenericPanelLauncher.from().launchPanel(
                () -> ScheduledLBCExceptionsPanel.from(
                    scheduledLBCExceptions,
                    new WorkerList(GlobalData.getInstance().getDefaultWorkerNames())
                ),
                () -> {},
                this::setScheduledLBCExceptions
            )
        );

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);
        setBorder(border);
        add(rowLabelPanel);
        add(meetTimePanel);
        add(workerSelectPanel);
        add(exceptionsButtonPanel);

        connectFocusableComponents();
    }

    public String getMeetTime() {
        return meetTimePanel.getMeetTime();
    }

    public List<List<Pair<String, Boolean>>> getWorkerSelectionGrid() {
        return workerSelectPanel.getWorkerSelectionGrid();
    }

    public ScheduledLBCExceptions getScheduledLBCExceptions() {
        return scheduledLBCExceptions;
    }

    public void setScheduledLBCExceptions(final ScheduledLBCExceptions scheduledLBCExceptions) {
        this.scheduledLBCExceptions = scheduledLBCExceptions;
        if (scheduledLBCExceptions.isEmpty()) {
            exceptionsButtonPanel.setButtonColors(Settings.QUIET_BUTTON_COLORS);
        } else {
            exceptionsButtonPanel.setButtonColors(Settings.LOUD_BUTTON_COLORS);
        }
    }

    public void setLBCData(final ScheduledLBCData lbcData) {
        meetTimePanel.setMeetTime(lbcData.getMeetTime());
        workerSelectPanel.setWorkers(lbcData.getWorkerSelectionGrid());
        setScheduledLBCExceptions(lbcData.getScheduledLBCExceptions());
    }

    public void updateWorkerNames(final List<List<String>> workerNames) {
        workerSelectPanel.updateWorkerNames(workerNames);
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(meetTimePanel, workerSelectPanel, exceptionsButtonPanel)
        );
    }
}
