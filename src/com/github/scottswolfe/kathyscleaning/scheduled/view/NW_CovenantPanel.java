package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.model.Worker;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_NoteListener;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class NW_CovenantPanel extends JPanel {

    public static final int WORKER_SELECT_ROW_COUNT = 2;
    public static final int WORKER_SELECT_COLUMN_COUNT = 6;

    // FIELDS
    JLabel header_label;
    WorkerList dwd;
    public WorkerSelectPanel dwp;
    JButton edit_button;
    JButton note_button;

    NW_DayPanel day_panel;
    JFrame container_frame;


    // CONSTRUCTOR
    public NW_CovenantPanel(NW_DayPanel day_panel, WorkerList notNeeded, JFrame container_frame ) {

        // TODO temporary hack
        WorkerList allWorkers = new WorkerList();
        WorkerList covWorkers = new WorkerList(WorkerList.COVENANT_WORKERS);
        WorkerList houseWorkers = new WorkerList(WorkerList.HOUSE_WORKERS);
        for (Worker worker : covWorkers.getWorkers()) {
            if (!allWorkers.containsName(worker)) {
                allWorkers.add(worker);
            }
        }
        for (Worker worker : houseWorkers.getWorkers()) {
            if (!allWorkers.containsName(worker)) {
                allWorkers.add(worker);
            }
        }

        this.day_panel = day_panel;
        this.container_frame = container_frame;

        setLayout( new MigLayout( "insets 10, fillx", "[]10[]10[]10[]30[]", "[grow]" ) );
        setBackground( Settings.BACKGROUND_COLOR );
        MatteBorder mborder = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK);
        setBorder( mborder );

        header_label = new JLabel();
        header_label.setText( "Covenant" );
        header_label.setFont( header_label.getFont().deriveFont( Settings.HEADER_FONT_SIZE ));

        dwp = WorkerSelectPanel.from(
            covWorkers,
            WORKER_SELECT_ROW_COUNT,
            WORKER_SELECT_COLUMN_COUNT,
            Settings.BACKGROUND_COLOR,
            null,
            null
        );

        edit_button = new JButton();
        edit_button.setText( "Edit");
        edit_button.setFont( edit_button.getFont().deriveFont(Settings.FONT_SIZE));
        edit_button.addActionListener( new EditWorkersListener(this, covWorkers, dwp) );

        note_button = new JButton();
        note_button.setText("Note");
        note_button.setFont(note_button.getFont().deriveFont(Settings.FONT_SIZE));
        note_button.addActionListener(new NW_NoteListener(day_panel, allWorkers, day_panel.getNoteData(), container_frame));

        add(header_label, "grow" );
        add(dwp, "grow");
        add(edit_button, "growx");
        add( new JSeparator(SwingConstants.VERTICAL), "growy" );
        add(note_button, "growx, hmin 50");
    }


    // PUBLIC METHODS

    public void changeWorkerPanel( WorkerList new_dwd ) {
        dwp.setWorkers( new_dwd );
    }

    public List<String> getSelectedWorkers() {
        return dwp.getSelectedWorkerNames();
    }

    public void setNoteButtonColor(Color color) {
        note_button.setBackground(color);
    }



    // PRIVATE METHODS



    // PRIVATE LISTENERS

    private class EditWorkersListener implements ActionListener {

        // FIELDS
        NW_CovenantPanel cov_panel;
        WorkerList dwd;
        WorkerSelectPanel dwp;


        // CONSTRUCTOR

        private EditWorkersListener(NW_CovenantPanel cov_panel, WorkerList dwd, WorkerSelectPanel dwp) {
            this.cov_panel = cov_panel;
            this.dwd = dwd;
            this.dwp = dwp;
        }

        public void actionPerformed(ActionEvent e){

            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setResizable(false);

            frame.addWindowListener( new FrameCloseListener( container_frame ));
            container_frame.setEnabled(false);

            frame.add(new EditCovenantWorkersPanel(cov_panel, frame, dwd, dwp));

            frame.pack();
            StaticMethods.findSetLocation(frame);

            frame.setVisible(true);

        }

    }


}
