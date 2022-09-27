package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import com.github.scottswolfe.kathyscleaning.component.EditableWorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.helper.SharedDataManager;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_NoteListener;

import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class NW_CovenantPanel extends JPanel {

    public static final int WORKER_SELECT_ROW_COUNT = 2;
    public static final int WORKER_SELECT_COLUMN_COUNT = 7;

    JButton note_button;

    NW_DayPanel day_panel;
    JFrame container_frame;

    private final EditableWorkerSelectPanel editableWorkerSelectPanel;

    public NW_CovenantPanel(NW_DayPanel day_panel, JFrame container_frame ) {

        final WorkerList workers = new WorkerList(SharedDataManager.getInstance().getAvailableWorkerNames());

        this.day_panel = day_panel;
        this.container_frame = container_frame;

        setLayout(new MigLayout( "insets 0, fill"));
        setBackground(Settings.BACKGROUND_COLOR);
        MatteBorder mborder = BorderFactory.createMatteBorder(0, 1, 2, 1, Color.BLACK);
        setBorder(mborder);

        final RowLabelPanel covenantLabelPanel = RowLabelPanel.from("Covenant");

        editableWorkerSelectPanel = EditableWorkerSelectPanel.from(
            workers,
            WORKER_SELECT_ROW_COUNT,
            WORKER_SELECT_COLUMN_COUNT,
            new FrameCloseListener(container_frame)
        );

        note_button = new JButton();
        note_button.setText("Note");
        note_button.setFont(note_button.getFont().deriveFont(Settings.FONT_SIZE));
        note_button.addActionListener(new NW_NoteListener(day_panel, workers, day_panel.getNoteData(), container_frame));

        final JPanel notePanel = new JPanel();
        notePanel.setLayout(new MigLayout("fill, insets 0"));
        notePanel.setBackground(Settings.BACKGROUND_COLOR);
        notePanel.add(note_button, "center");

        add(covenantLabelPanel, "grow");
        add(editableWorkerSelectPanel, "grow");
        add(notePanel, "grow");
    }

    public void changeWorkerPanel( WorkerList new_dwd ) {
        editableWorkerSelectPanel.setWorkers(new_dwd);
    }

    public WorkerList getWorkers() {
        return editableWorkerSelectPanel.getWorkers();
    }

    public void setWorkers(final WorkerList workers) {
        editableWorkerSelectPanel.setWorkers(workers);
    }

    public boolean isWorkerSelected(final String workerName) {
        return editableWorkerSelectPanel.getWorkerSelectionGrid().stream()
            .flatMap(Collection::stream)
            .anyMatch(workerSelection -> workerName.equals(workerSelection.getLeft()) && workerSelection.getRight());
    }

    public List<String> getSelectedWorkers() {
        return editableWorkerSelectPanel.getWorkerSelectionGrid().stream()
            .flatMap(Collection::stream)
            .filter(Pair::getRight)
            .map(Pair::getLeft)
            .collect(Collectors.toList());
    }

    public void setNoteButtonColor(Color color) {
        note_button.setBackground(color);
    }
}
