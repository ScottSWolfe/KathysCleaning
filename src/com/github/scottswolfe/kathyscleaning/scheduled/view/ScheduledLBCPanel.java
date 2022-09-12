package com.github.scottswolfe.kathyscleaning.scheduled.view;

import com.github.scottswolfe.kathyscleaning.component.ButtonPanel;
import com.github.scottswolfe.kathyscleaning.component.EditableWorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.component.MeetTimePanel;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.GenericPanelLauncher;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCExceptions;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScheduledLBCPanel extends JPanel implements FocusableCollection {

    public static final int LBC_SCHEDULED_WORKER_ROW_COUNT = 2;
    public static final int LBC_SCHEDULED_WORKER_COLUMN_COUNT = 5;

    public static final int LBC_SCHEDULED_EXCEPTIONS_COUNT = 3;

    private final MeetTimePanel meetTimePanel;
    private final EditableWorkerSelectPanel editableWorkerSelectPanel;
    private final ButtonPanel exceptionsButtonPanel;

    private ScheduledLBCExceptions scheduledLBCExceptions;

    public static ScheduledLBCPanel from(final WindowListener popUpWindowListener) {
        return new ScheduledLBCPanel(popUpWindowListener);
    }

    private ScheduledLBCPanel(final WindowListener popUpWindowListener) {

        scheduledLBCExceptions = ScheduledLBCExceptions.from();

        final RowLabelPanel rowLabelPanel = RowLabelPanel.from("LBC");
        meetTimePanel = MeetTimePanel.from();
        editableWorkerSelectPanel = EditableWorkerSelectPanel.from(
            new WorkerList(WorkerList.LBC_WORKERS),
            LBC_SCHEDULED_WORKER_ROW_COUNT,
            LBC_SCHEDULED_WORKER_COLUMN_COUNT,
            popUpWindowListener
        );
        exceptionsButtonPanel = ButtonPanel.from(
            "Exceptions",
            () -> GenericPanelLauncher.from().launchPanel(
                () -> ScheduledLBCExceptionsPanel.from(scheduledLBCExceptions, new WorkerList(WorkerList.LBC_WORKERS)),
                () -> {},
                this::setScheduledLBCExceptions,
                popUpWindowListener
            )
        );

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);
        add(rowLabelPanel);
        add(meetTimePanel);
        add(editableWorkerSelectPanel);
        add(exceptionsButtonPanel);

        connectFocusableComponents();
    }

    public void setScheduledLBCExceptions(final ScheduledLBCExceptions scheduledLBCExceptions) {
        this.scheduledLBCExceptions = scheduledLBCExceptions;
        if (scheduledLBCExceptions.isEmpty()) {
            exceptionsButtonPanel.setButtonColors(Settings.QUIET_BUTTON_COLORS);
        } else {
            exceptionsButtonPanel.setButtonColors(Settings.LOUD_BUTTON_COLORS);
        }
    }

    @Override
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(meetTimePanel, editableWorkerSelectPanel, exceptionsButtonPanel)
        );
    }
}
