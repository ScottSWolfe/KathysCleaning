package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import com.github.scottswolfe.kathyscleaning.component.Button;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.ButtonColors;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_NoteListener;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class NW_CovenantPanel extends JPanel {

    public static final int WORKER_SELECT_ROW_COUNT = 2;
    public static final int WORKER_SELECT_COLUMN_COUNT = 7;

    private final Button note_button;

    NW_DayPanel day_panel;

    private final WorkerSelectPanel workerSelectPanel;

    public NW_CovenantPanel(NW_DayPanel day_panel) {

        final WorkerList workers = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());

        this.day_panel = day_panel;

        setLayout(new MigLayout( "insets 0, fill"));
        setBackground(Settings.BACKGROUND_COLOR);
        MatteBorder mborder = BorderFactory.createMatteBorder(0, 1, 2, 1, Color.BLACK);
        setBorder(mborder);

        final RowLabelPanel covenantLabelPanel = RowLabelPanel.from("Covenant");

        workerSelectPanel = WorkerSelectPanel.from(
            workers,
            WORKER_SELECT_ROW_COUNT,
            WORKER_SELECT_COLUMN_COUNT,
            Settings.BACKGROUND_COLOR
        );

        // todo: rewrite code from NW_NoteListener so we are not triggering an action on a listener
        note_button = Button.from(
            "Note",
            Settings.QUIET_BUTTON_COLORS,
            () -> new NW_NoteListener(day_panel, workers, day_panel.getNoteData()).actionPerformed(null)
        );

        final JPanel notePanel = new JPanel();
        notePanel.setLayout(new MigLayout("fill, insets 0"));
        notePanel.setBackground(Settings.BACKGROUND_COLOR);
        notePanel.add(note_button, "center");

        add(covenantLabelPanel, "grow");
        add(workerSelectPanel, "grow");
        add(notePanel, "grow");
    }

    public WorkerList getWorkers() {
        return workerSelectPanel.getWorkers();
    }

    public void setWorkers(final WorkerList workers) {
        workerSelectPanel.setWorkers(workers);
    }

    public boolean isWorkerSelected(final String workerName) {
        return workerSelectPanel.getWorkerSelectionGrid().stream()
            .flatMap(Collection::stream)
            .anyMatch(workerSelection -> workerName.equals(workerSelection.getLeft()) && workerSelection.getRight());
    }

    public List<String> getSelectedWorkers() {
        return workerSelectPanel.getWorkerSelectionGrid().stream()
            .flatMap(Collection::stream)
            .filter(Pair::getRight)
            .map(Pair::getLeft)
            .collect(Collectors.toList());
    }

    public void setNoteButtonColor(@Nonnull final ButtonColors colors) {
        note_button.setColors(colors);
    }
}
