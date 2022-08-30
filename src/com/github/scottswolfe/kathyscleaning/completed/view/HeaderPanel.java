package com.github.scottswolfe.kathyscleaning.completed.view;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.component.ChangeDayPanel;
import com.github.scottswolfe.kathyscleaning.component.CopyWorkersPanel;
import com.github.scottswolfe.kathyscleaning.component.DatePanel;
import com.github.scottswolfe.kathyscleaning.component.SubmitFormPanel;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class HeaderPanel extends JPanel implements FocusableCollection {

    private final DatePanel datePanel;
    private final CopyWorkersPanel copyWorkersPanel;
    private final ChangeDayPanel changeDayPanel;
    private final SubmitFormPanel submitFormPanel;

    public HeaderPanel(
        final Controller<TabbedPane, CompletedModel> controller,
        final WorkerList dwd,
        final Consumer<List<List<Pair<String, Boolean>>>> onCopyWorkersButtonPress,
        final ActionListener previousDayButtonListener,
        final ActionListener nextDayButtonListener,
        final WindowListener popUpWindowListener
    ) {
        setLayout(new MigLayout("gap 0 px, insets 1","[grow][grow][grow][grow][grow]","[grow]"));
        setBackground(Settings.HEADER_BACKGROUND);
        setBorder(BorderFactory.createLineBorder(null, 2));

        datePanel = DatePanel.from(SessionModel.getCompletedStartDay());
        copyWorkersPanel = CopyWorkersPanel.from(
            dwd,
            HousePanel.WORKER_SELECTION_ROW_COUNT,
            HousePanel.WORKER_SELECTION_COLUMN_COUNT,
            onCopyWorkersButtonPress,
            popUpWindowListener
        );
        changeDayPanel = ChangeDayPanel.from(previousDayButtonListener, nextDayButtonListener);
        submitFormPanel = SubmitFormPanel.from(controller);

        int day_panel_width_min = 133 + (int) Settings.FONT_SIZE; // temp fix

        add(datePanel,"growy, growx, wmin " + day_panel_width_min +", ay center");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(copyWorkersPanel, "growy");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(changeDayPanel, "growy");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(submitFormPanel, "growy");

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
    public List<List<? extends JComponent>> getComponentsAsGrid() {
        return Collections.singletonList(
            Arrays.asList(copyWorkersPanel, changeDayPanel, submitFormPanel)
        );
    }
}
