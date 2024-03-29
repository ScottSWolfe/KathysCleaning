package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_CopyWorkersListener;

import net.miginfocom.swing.MigLayout;

public class ScheduledHousesCopyWorkersPanel extends JPanel {

    private WorkerSelectPanel worker_panel;
    JButton copy_workers;

    public ScheduledHousesCopyWorkersPanel(JTabbedPane tp, WorkerList dwd, NW_DayPanel day_panel) {

        setLayout(new MigLayout("fill, insets 0"));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        setBackground(Settings.BACKGROUND_COLOR);
        setBackground(Settings.HEADER_BACKGROUND);

        JPanel worker_panel = workerPanel(dwd, tp, day_panel);

        add(worker_panel, "ax center");
    }

    private JPanel workerPanel(
        final WorkerList dwd,
        final JTabbedPane tp,
        final NW_DayPanel day_panel
    ) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("insets 0"));
        panel.setBackground(Settings.BACKGROUND_COLOR);
        panel.setBackground(Settings.HEADER_BACKGROUND);

        worker_panel = WorkerSelectPanel.from(
            dwd,
            ScheduledLBCPanel.LBC_SCHEDULED_WORKER_ROW_COUNT,
            ScheduledLBCPanel.LBC_SCHEDULED_WORKER_COLUMN_COUNT,
            Settings.HEADER_BACKGROUND
        );

        copy_workers = new JButton("Copy");
        copy_workers.addActionListener(new NW_CopyWorkersListener(tp, day_panel));
        copy_workers.setFont( copy_workers.getFont().deriveFont( Settings.FONT_SIZE ) );

        panel.add(worker_panel);
        panel.add(copy_workers);

        return panel;
    }

    public void setWorkers(WorkerList workers) {
        worker_panel.setWorkers(workers);
    }

    public WorkerList getWorkerList() {
        return worker_panel.getWorkers();
    }

    public List<String> getSelectedWorkers() {
        return worker_panel.getSelectedWorkerNames();
    }
}
