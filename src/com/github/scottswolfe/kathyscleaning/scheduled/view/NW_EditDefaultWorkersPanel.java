package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;

import net.miginfocom.swing.MigLayout;

public class NW_EditDefaultWorkersPanel extends JPanel {

    WorkerSelectPanel worker_panel;
    JFrame frame;
    NW_DayPanel day_panel;

    int rows = WorkerSelectPanel.DEFAULT_ROW_COUNT;
    int columns = WorkerSelectPanel.DEFAULT_COLUMN_COUNT;

    JComboBox<String>[][] worker_combo;
    JButton cancel_button;
    JButton submit_button;

    public NW_EditDefaultWorkersPanel(WorkerSelectPanel worker_panel, JFrame frame, NW_DayPanel day_panel) {

        this.worker_panel = worker_panel;
        this.frame = frame;
        this.day_panel = day_panel;

        worker_combo = new JComboBox[rows][columns];
        WorkerList dwd = new WorkerList(GlobalData.getInstance().getDefaultWorkerNames());

        for (int i=0; i<rows; i++) {

            for(int j=0; j<columns; j++) {

                worker_combo[i][j] = new JComboBox<String>();
                worker_combo[i][j].setEditable(false);
                worker_combo[i][j].setSize(10, UNDEFINED_CONDITION);
                worker_combo[i][j].setFont( worker_combo[i][j].getFont().deriveFont( Settings.FONT_SIZE ) );
                worker_combo[i][j].setBackground(Settings.HEADER_BACKGROUND);

                worker_combo[i][j].addItem(null);   // empty choice
                for(int k=0; k<dwd.size(); k++){
                    worker_combo[i][j].addItem(dwd.getName(k));
                }

                worker_combo[i][j].setSelectedItem(worker_panel.getNameAt(i, j));

            }

        }

        addFlexibleFocusListeners();

        setLayout( new MigLayout("insets 5","[]6[]6[]6[]","[]6[]6[]") );
        setBackground(Settings.BACKGROUND_COLOR);

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {

                if (i<rows && j == columns - 1 ) {
                    add(worker_combo[i][j], "wrap");
                }
                else{
                    add(worker_combo[i][j]);
                }

            }

        }

        cancel_button = new JButton("Cancel");
        cancel_button.setFont( cancel_button.getFont().deriveFont( Settings.FONT_SIZE ) );
        cancel_button.setBackground(Settings.MAIN_COLOR);
        cancel_button.setForeground( Settings.FOREGROUND_COLOR );

        submit_button = new JButton("Submit");
        submit_button.setFont( submit_button.getFont().deriveFont( Settings.FONT_SIZE ) );
        submit_button.setBackground(Settings.MAIN_COLOR);
        submit_button.setForeground( Settings.FOREGROUND_COLOR );


        cancel_button.addActionListener( new CancelListener( frame ) );
        submit_button.addActionListener( new SubmitListener(frame, day_panel, this) );

        JPanel p = new JPanel();
        p.setLayout( new MigLayout("align right"));
        p.setBackground( Settings.BACKGROUND_COLOR );
        p.add(cancel_button, "");
        p.add(submit_button, "");

        add(p, "span " + WorkerSelectPanel.DEFAULT_COLUMN_COUNT + ", grow");

    }

    private void addFlexibleFocusListeners() {

        for (int i=0; i<rows; i++) {
            for (int j=0; j<columns; j++) {

                JComboBox<String> left_box = null;
                JComboBox<String> right_box = null;
                JComboBox<String> up_box = null;
                JComboBox<String> down_box = null;


                if ( j > 0 ) {
                    left_box = worker_combo[i][j-1];
                }
                else if ( j <= 0 ) {
                    left_box = null;
                }
                if ( j < columns - 1 ) {
                    right_box = worker_combo[i][j+1];
                }
                else if ( j >= columns - 1 ) {
                    right_box = null;
                }

                if ( i > 0 ) {
                    up_box = worker_combo[i-1][j];
                }
                else if ( i <= 0 ) {
                    up_box = null;
                }
                if ( i < rows - 1 ) {
                    down_box = worker_combo[i+1][j];
                }
                else if ( i >= rows - 1 ) {
                    down_box = null;
                }

                KeyboardFocusListener ffl = KeyboardFocusListener.from(
                    worker_combo[i][j],
                    left_box,
                    right_box,
                    up_box,
                    down_box,
                    null
                );

                worker_combo[i][j].getEditor().getEditorComponent().addFocusListener( ffl );

            }
        }

    }

    private class CancelListener implements ActionListener {

        JFrame frame;

        private CancelListener( JFrame frame ) {

            this.frame = frame;

        }

        public void actionPerformed(ActionEvent e){
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        }

    }


    private class SubmitListener implements ActionListener {

        JFrame frame;
        NW_DayPanel day_panel;
        NW_EditDefaultWorkersPanel edwp;

        private SubmitListener(JFrame frame, NW_DayPanel day_panel, NW_EditDefaultWorkersPanel edwp) {

            this.frame = frame;
            this.day_panel = day_panel;
            this.edwp = edwp;

        }

        public void actionPerformed(ActionEvent e){

            if ( StaticMethods.isRepeatWorker( worker_combo ) ) {
                StaticMethods.shareRepeatWorker();
                return;
            }

            // copy workers selected
            WorkerList workers = new WorkerList();
            for (int i=0; i<rows; i++){
                for (int j=0; j<columns; j++) {
                    workers.add(String.valueOf(
                            edwp.worker_combo[i][j].getSelectedItem()));
                }
            }

            // paste workers selected on header worker_panel and house panel dwps
            day_panel.changeWorkerPanels(workers);

            // close EditDefaultWorkersPanel
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

        }

    }

}
