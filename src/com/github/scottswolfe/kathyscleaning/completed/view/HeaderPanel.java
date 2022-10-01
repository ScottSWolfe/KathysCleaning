package com.github.scottswolfe.kathyscleaning.completed.view;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.component.ChangeDayPanel;
import com.github.scottswolfe.kathyscleaning.component.CopyWorkersPanel;
import com.github.scottswolfe.kathyscleaning.component.DatePanel;
import com.github.scottswolfe.kathyscleaning.component.SubmitFormPanel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class HeaderPanel extends JPanel implements FocusableCollection {

    private final DatePanel datePanel;
    private final CopyWorkersPanel copyWorkersPanel;
    private final ChangeDayPanel changeDayPanel;
    private final SubmitFormPanel submitFormPanel;

    public static HeaderPanel from(
        final Calendar date,
        final WorkerList workerList,
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final ActionListener previousDayButtonListener,
        final ActionListener nextDayButtonListener,
        final ActionListener submitFormListener
    ) {
        return new HeaderPanel(
            date,
            workerList,
            onCopyWorkersButtonPress,
            previousDayButtonListener,
            nextDayButtonListener,
            submitFormListener
        );
    }

    private HeaderPanel(
        final Calendar date,
        final WorkerList workerList,
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final ActionListener previousDayButtonListener,
        final ActionListener nextDayButtonListener,
        final ActionListener submitFormListener
    ) {
        setLayout(new MigLayout("fillx, gapx 0, insets 0"));
        setBackground(Settings.HEADER_BACKGROUND);

        datePanel = DatePanel.from(date);
        copyWorkersPanel = CopyWorkersPanel.from(
            workerList,
            HousePanel.WORKER_SELECTION_ROW_COUNT,
            HousePanel.WORKER_SELECTION_COLUMN_COUNT,
            onCopyWorkersButtonPress
        );
        changeDayPanel = ChangeDayPanel.from(previousDayButtonListener, nextDayButtonListener);
        submitFormPanel = SubmitFormPanel.from(submitFormListener);

        int day_panel_width_min = 133 + (int) Settings.FONT_SIZE; // temp fix

        add(datePanel,"grow, wmin " + day_panel_width_min + ", ay center");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(copyWorkersPanel, "growx");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(changeDayPanel, "growx");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(submitFormPanel, "growx");

        connectFocusableComponents();
    }

    public WorkerList getWorkers() {
        return copyWorkersPanel.getWorkers();
    }

    public void setWorkers(WorkerList workers) {
        copyWorkersPanel.setWorkers(workers);
    }

    public void setDate(final Calendar calendar) {
        datePanel.setDate(calendar);
    }

    public Calendar getDate() {
        return datePanel.getDate();
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(copyWorkersPanel, changeDayPanel, submitFormPanel)
        );
    }
}
