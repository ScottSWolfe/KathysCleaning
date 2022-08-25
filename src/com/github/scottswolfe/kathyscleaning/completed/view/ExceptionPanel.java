package com.github.scottswolfe.kathyscleaning.completed.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionEntry;
import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

public class ExceptionPanel extends JPanel {

/* INSTANCE VARIABLES ======================================================= */

    JFrame frame;
    WorkerList workers;



/* CLASS VARIABLES ========================================================== */

    public final static int EXCEPTION_ROWS = 3;



/* COMPONENTS =============================================================== */

    JLabel name_label;
    JLabel time_label;

    JComboBox<String>[] name_box;

    JTextField[] time_begin;
    JTextField[] time_end;

    JButton cancel_button;
    JButton submit_button;



/* CONSTRUCTORS ============================================================= */

    @SuppressWarnings("unchecked")
    public ExceptionPanel(
        JFrame frame,
        WorkerList dwd,
        Supplier<ExceptionData> exceptionDataSupplier,
        Consumer<ExceptionData> exceptionDataConsumer
    ) {
        this.frame = frame;
        this.workers = dwd;

        // generating string for migLayout based on EXCEPTION_ROWS
        String temp = "[grow]";
        for(int i=1; i<EXCEPTION_ROWS; i++) {
            temp = new String(temp + "[grow]");
        }

        setLayout( new MigLayout("insets 5", temp, "[grow]3[grow]6[grow]6[grow]") );
        setBackground(Settings.BACKGROUND_COLOR);

        name_label = new JLabel("Name");
        name_label.setFont( name_label.getFont().deriveFont( Settings.FONT_SIZE ) );

        time_label = new JLabel("Time");
        time_label.setFont( time_label.getFont().deriveFont( Settings.FONT_SIZE ) );

        name_box = new JComboBox[EXCEPTION_ROWS];

        time_begin = new JTextField[EXCEPTION_ROWS];
        AbstractDocument[] time_begin_doc = new AbstractDocument[EXCEPTION_ROWS];

        time_end = new JTextField[EXCEPTION_ROWS];
        AbstractDocument[] time_end_doc = new AbstractDocument[EXCEPTION_ROWS];

        final ExceptionData exceptionData = exceptionDataSupplier.get();
        for (int i = 0; i < EXCEPTION_ROWS; i++) {
            name_box[i] = new JComboBox<String>();
            name_box[i].setEditable(true);
            name_box[i].setSize(10, UNDEFINED_CONDITION);
            name_box[i].setFont( name_box[i].getFont().deriveFont( Settings.FONT_SIZE ) );

            name_box[i].addItem("");
            for(int k = 0; k < dwd.size(); k++){
                name_box[i].addItem(dwd.get(k).getName());
            }

            time_begin[i] = new JTextField(5);
            time_begin[i].setFont( time_begin[i].getFont().deriveFont( Settings.FONT_SIZE ) );
            time_begin_doc[i] = (AbstractDocument)time_begin[i].getDocument();
            TimeDocumentFilter tdf_begin = new TimeDocumentFilter(time_begin[i]);
            time_begin_doc[i].setDocumentFilter( tdf_begin );
            time_begin[i].addKeyListener( new TimeKeyListener( tdf_begin ) );

            time_end[i] = new JTextField(5);
            time_end[i].setFont( time_end[i].getFont().deriveFont( Settings.FONT_SIZE ) );
            time_end_doc[i] = (AbstractDocument)time_end[i].getDocument();
            TimeDocumentFilter tdf_end = new TimeDocumentFilter(time_end[i]);
            time_end_doc[i].setDocumentFilter( tdf_end );
            time_end[i].addKeyListener( new TimeKeyListener( tdf_end) );

            if (exceptionData.isException()) {
                ExceptionEntry entry = exceptionData.getEntry(i);
                name_box[i].setSelectedItem(entry.getWorker_name());
                time_begin[i].setText(entry.getTime_begin());
                time_end[i].setText(entry.getTime_end());
            }
        }

        cancel_button = new JButton("Cancel");
        cancel_button.setFont( cancel_button.getFont().deriveFont( Settings.FONT_SIZE ) );
        cancel_button.addActionListener( new CancelListener(frame) );
        cancel_button.setBackground(Settings.MAIN_COLOR);
        cancel_button.setForeground( Settings.FOREGROUND_COLOR );

        submit_button = new JButton("Submit");
        submit_button.setFont(submit_button.getFont().deriveFont(Settings.FONT_SIZE));
        submit_button.addActionListener(new SubmitListener(frame, this, exceptionDataConsumer));
        submit_button.setBackground(Settings.MAIN_COLOR);
        submit_button.setForeground( Settings.FOREGROUND_COLOR );

        addFlexibleFocusListeners();

        add(name_label, "ax 10%");
        add(time_label, "span 2 1, wrap, ax center, gapx 15");

        for(int i=0; i<EXCEPTION_ROWS; i++){
            add(name_box[i], "ay center, growy");
            add(time_begin[i], "ay center, growy, gapx 15");
            add(time_end[i], "wrap, ay center, growy");
        }

        JPanel p = new JPanel();
        p.setLayout( new MigLayout("align right"));
        p.setBackground( Settings.BACKGROUND_COLOR );
        p.add(cancel_button, "");
        p.add(submit_button, "");

        add (p, new String ("span "+EXCEPTION_ROWS+", growx"));
    }



/* PRIVATE METHODS ========================================================== */

    // TODO make all the focus listeners their own class or find a library
    private void addFlexibleFocusListeners() {

        for (int i=0; i<EXCEPTION_ROWS; i++) {

            JComboBox<String> up_cb = null;
            JComboBox<String> down_cb = null;
            JTextField up_begin_field = null;
            JTextField down_begin_field = null;
            JTextField up_end_field = null;
            JTextField down_end_field = null;

            if ( i > 0 ) {
                up_cb = name_box[i-1];
                up_begin_field = time_begin[i-1];
                up_end_field = time_end[i-1];
            }
            else if ( i <= 0 ) {
                up_cb = null;
                up_begin_field = null;
                up_end_field = null;
            }
            if ( i < EXCEPTION_ROWS - 1 ) {
                down_cb = name_box[i+1];
                down_begin_field = time_begin[i+1];
                down_end_field = time_end[i+1];
            }
            else if ( i >= EXCEPTION_ROWS - 1 ) {
                down_cb = null;
                down_begin_field = null;
                down_end_field = null;
            }

            name_box[i].getEditor().getEditorComponent().addFocusListener( KeyboardFocusListener.from(
                name_box[i],
                null,
                time_begin[i],
                up_cb,
                down_cb,
                null
            ));

            time_begin[i].addFocusListener(KeyboardFocusListener.from(
                time_begin[i],
                name_box[i],
                time_end[i],
                up_begin_field,
                down_begin_field,
                null
            ));

            time_end[i].addFocusListener(KeyboardFocusListener.from(
                time_end[i],
                time_begin[i],
                null,
                up_end_field,
                down_end_field,
                down_cb
            ));
        }
    }



/* PRIVATE LISTENERS ======================================================== */

    // if cancel button is selected
    private class CancelListener implements ActionListener {

        //  FIELDS
        JFrame frame;

        //  CONSTRUCTORS
        private CancelListener( JFrame frame ) {
            this.frame = frame;
        }

        //  LISTENER
        public void actionPerformed(ActionEvent e) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }

    // if submit button is selected
    private class SubmitListener implements ActionListener {
        //  FIELDS
        JFrame frame;
        ExceptionPanel ep;
        Consumer<ExceptionData> exceptionDataConsumer;

        //  CONSTRUCTORS
        private SubmitListener(JFrame frame, ExceptionPanel ep, Consumer<ExceptionData> exceptionDataConsumer) {
            this.frame = frame;
            this.ep = ep;
            this.exceptionDataConsumer = exceptionDataConsumer;
        }

        //  LISTENER
        public void actionPerformed(ActionEvent e){

            String worker;
            String time_begin;
            String time_end;
            ExceptionEntry entry;
            List<ExceptionEntry> entries = new ArrayList<>();

            for(int i = 0; i < EXCEPTION_ROWS; i++) {
                worker = String.valueOf(ep.name_box[i].getSelectedItem());
                time_begin = ep.time_begin[i].getText();
                time_end = ep.time_end[i].getText();
                entry = new ExceptionEntry(worker, time_begin, time_end);
                entries.add(entry);
            }
            exceptionDataConsumer.accept(new ExceptionData(entries));

            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }
    }
}
