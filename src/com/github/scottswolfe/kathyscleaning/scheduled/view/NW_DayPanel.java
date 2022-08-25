package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_ExceptionListener;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionEntry;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;

import net.miginfocom.swing.MigLayout;


@SuppressWarnings("serial")
public class NW_DayPanel extends JPanel{

    //  FIELDS
    GeneralController<TabbedPane, NW_Data> controller;

    NoteData noteData;
    List<BeginExceptionEntry> beginExceptionList;

    TabbedPane tp;
    DayData day_data;
    Calendar date;
    JFrame frame;


    // COMPONENTS
    public NW_HeaderPanel header_panel;
    public List<NW_HousePanel> house_panels;
    public NW_CovenantPanel cov_panel;
    public JPanel jsp_panel;
    public JScrollPane jsp;

    JPanel begin_panel;
    JLabel meet_location_label;
    public JComboBox<String> meet_location_box;
    JLabel meet_time_label;
    public JTextField meet_time_field;
    JButton exception_button;


    // CONSTRUCTORS
    public NW_DayPanel(GeneralController<TabbedPane, NW_Data> controller, TabbedPane tp,
            WorkerList workers, Calendar date, JFrame frame, int mode, int wk ) {

        this.controller = controller;
        this.date = date;
        this.frame = frame;
        this.tp = tp;

        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        header_panel = new NW_HeaderPanel(controller, tp, workers, this, date, frame, mode, wk);
        begin_panel = createBeginPanel(workers);
        house_panels = new ArrayList<NW_HousePanel>();
        for(int i = 0; i < DayPanel.NUM_HOUSE_PANELS; i++) {
            house_panels.add(new NW_HousePanel(workers, this, frame));
        }
        cov_panel = new NW_CovenantPanel(this, new WorkerList(), frame);


        // creating scroll pane and adding house panels
        jsp_panel = new JPanel();
        jsp_panel.setLayout( new MigLayout("fillx") );
        jsp_panel.setBackground( Settings.BACKGROUND_COLOR );

        for(NW_HousePanel house_panel : house_panels) {
            jsp_panel.add(house_panel, new String("wrap " + DayPanel.PANEL_PADDING + ", grow") );
        }

        jsp = new JScrollPane(jsp_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        jsp.setBackground( Settings.BACKGROUND_COLOR );

        MatteBorder mborder2 = BorderFactory.createMatteBorder(0, 1, 0, 1, Color.BLACK);

        jsp.setBorder( mborder2 );

        addFlexibleFocusListeners();

        // Adding Elements onto Panel
        add(header_panel, new String("dock north, grow") );
        add(begin_panel, new String("dock north, grow"));
        add(jsp, new String("grow") );
        add(cov_panel, "dock south, grow");
    }


    public NW_DayPanel() {

    }



    // PRIVATE CONSTRUCTION METHOD

    protected JPanel createBeginPanel(WorkerList workers) {

        JPanel panel = new JPanel();
        panel.setLayout( new MigLayout( "insets 10, fill" ) );
        panel.setBackground( Settings.BACKGROUND_COLOR );

        MatteBorder mborder = BorderFactory.createMatteBorder(0, 1, 1, 1, Color.BLACK);
        panel.setBorder(mborder);

        meet_location_label = new JLabel("Meet Location:");
        meet_location_label.setFont( meet_location_label.getFont().deriveFont(Settings.FONT_SIZE));
        meet_location_label.setForeground( Settings.MAIN_COLOR);

        meet_location_box = new JComboBox<String>();
        meet_location_box.setEditable(true);
        meet_location_box.setFont( meet_location_box.getFont().deriveFont( Settings.FONT_SIZE ));

        meet_time_label = new JLabel("Meet Time:");
        meet_time_label.setFont( meet_time_label.getFont().deriveFont(Settings.FONT_SIZE));
        meet_time_label.setForeground(Settings.MAIN_COLOR);

        meet_time_field = new JTextField();
        meet_time_field.setFont( meet_time_field.getFont().deriveFont(Settings.FONT_SIZE));
        meet_time_field.setColumns( 7 );
        TimeDocumentFilter tdf = new TimeDocumentFilter(meet_time_field);
        AbstractDocument ad = (AbstractDocument) meet_time_field.getDocument();
        ad.setDocumentFilter(tdf);
        meet_time_field.addKeyListener( new TimeKeyListener( tdf ) );

        exception_button = new JButton();
        exception_button.setText("Exceptions");
        exception_button.setFont( exception_button.getFont().deriveFont(Settings.FONT_SIZE));
        exception_button.addActionListener( new NW_ExceptionListener( this, workers, frame ) );

        panel.add(meet_location_label,"gapx 0, align right");
        panel.add(meet_location_box,"gapx 0, align left");
        panel.add(meet_time_label,"gapx 0, align right");
        panel.add(meet_time_field,"gapx 0, align left");
        panel.add(exception_button,"gapx 0, align right");

        return panel;
    }




    //  PUBLIC METHODS

    public void changeWorkerPanels(WorkerList new_workers){

        int header_width = header_panel.getWidth();
        int house_panel_width = house_panels.get(0).getWidth();

        header_panel.setWorkers(new_workers);
        for (NW_HousePanel house_panel : house_panels) {
            house_panel.changeHouseWorkers(new_workers);
        }

        frame.revalidate();
        frame.repaint();

        int new_header_width = header_panel.getWidth();
        int new_house_panel_width = house_panels.get(0).getWidth();

        int header_change = new_header_width - header_width;
        int house_panel_change = new_house_panel_width - house_panel_width;
        int change = 0;
        if(header_change > house_panel_change) {
            change = header_change;
        }
        else {
            change = house_panel_change;
        }

        frame.setSize( frame.getWidth() + change , frame.getHeight() );
        frame.revalidate();
        frame.repaint();
    }


    public void changeCovenantWorkerPanel( WorkerList dwd ) {
        remove(cov_panel);

        NW_CovenantPanel new_panel = new NW_CovenantPanel( this, dwd, frame );

        add( new_panel, "dock south, growx" );
        cov_panel = new_panel;

        frame.revalidate();
        frame.repaint();
    }

    public boolean isBeginException() {
        if (beginExceptionList == null) {
            return false;
        }
        for (BeginExceptionEntry entry : beginExceptionList) {
            if (!entry.isBlank()) {
                return true;
            }
        }
        return false;
    }

    public String getMeetLocation() {
        if ( String.valueOf(meet_location_box.getSelectedItem()) != null &&
             String.valueOf(meet_location_box.getSelectedItem()).length() > 0 ) {

            return String.valueOf(meet_location_box.getSelectedItem());
        }
        else {
            return "";
        }
    }

    public String getMeetTime() {
        if (meet_time_field.getText() != null) {
            return meet_time_field.getText();
        }
        else {
            return "";
        }
    }

    public List<String> getExceptionNames() {
        List<String> s = new ArrayList<>();
        if (!isBeginException()) {
            return s;
        } else {
            for (BeginExceptionEntry exception : beginExceptionList) {
                s.add(exception.getName());
            }
            return s;
        }
    }

    public int getNumBeginExceptionEntries() {
        if (beginExceptionList == null) {
            return 0;
        }
        else {
            return beginExceptionList.size();
        }
    }

    // this method returns the number of unique employees for a given day
    public int getNumberUniqueWorkers() {
        List<String> workers = getUniqueWorkersForDay();
        return workers.size();
    }

    // this method returns a list of the unique employees for a given day
    public List<String> getUniqueWorkersForDay() {

        List<String> workers = new ArrayList<>();

        // add workers from houses
        for (NW_HousePanel house_panel : house_panels) {
            List<String> workerList = house_panel.getSelectedWorkers();
            for (String worker : workerList) {
                if (!workers.contains(worker)) {
                    workers.add(worker);
                }
            }
        }

        // add workers from covenant panel
        List<String> covWorkers = cov_panel.getSelectedWorkers();
        for (String worker : covWorkers) {
            if (!workers.contains(worker)) {
                workers.add(worker);
            }
        }

        return workers;
    }

    public NoteData getNoteData() {
        return noteData;
    }

    public void setNoteData(NoteData noteData) {
        this.noteData = noteData;
        if (noteData.isBlank()) {
            cov_panel.setNoteButtonColor(Settings.DEFAULT_BUTTON_COLOR);
        } else {
            cov_panel.setNoteButtonColor(Settings.EDITED_BUTTON_COLOR);
        }
    }

    public List<BeginExceptionEntry> getBeginExceptionList() {
        return beginExceptionList;
    }

    public void setBeginExceptionList(List<BeginExceptionEntry> beginExceptions) {
        this.beginExceptionList = beginExceptions;
        if (isBeginException()) {
            exception_button.setBackground(Settings.EDITED_BUTTON_COLOR);
        } else {
            exception_button.setBackground(Settings.DEFAULT_BUTTON_COLOR);
        }
    }

    public void addFlexibleFocusListeners() {

        for (int i = 0; i < house_panels.size(); i++) {
            NW_HousePanel hp = house_panels.get(i);

            NW_HousePanel hp_up;
            NW_HousePanel hp_down;

            if (i > 0) {
                hp_up = house_panels.get(i - 1);
            }
            else {
                hp_up = new NW_HousePanel();  // all null fields
            }
            if (i < house_panels.size() - 1) {
                hp_down = house_panels.get(i + 1);
            }
            else {
                hp_down = new NW_HousePanel(); // all null fields
            }

            hp.house_name_text_field.addFocusListener(KeyboardFocusListener.from(
                hp.house_name_text_field,
                null,
                hp.worker_panel.getComponentToFocusFromLeft(),
                hp_up.house_name_text_field,
                hp_down.house_name_text_field,
                null
            ));
        }

        meet_location_box.getEditor().getEditorComponent().addFocusListener(KeyboardFocusListener.from(
            meet_location_box,
            null,
            meet_time_field,
            null,
            null,
            null
        ));

        meet_time_field.addFocusListener(KeyboardFocusListener.from(
            meet_time_field,
            meet_location_box,
            exception_button,
            null,
            null,
            null
        ));

        exception_button.addFocusListener(KeyboardFocusListener.from(
            exception_button,
            meet_time_field,
            null,
            null,
            null,
            null
        ));
    }

    public int getNumHousePanels() {
        return house_panels.size();
    }

    public List<NW_HousePanel> copyHousePanels() {
        List<NW_HousePanel> list = new ArrayList<>();
        for (NW_HousePanel house_panel : house_panels) {
            list.add(house_panel.copyPanel());
        }
        return list;
    }

    public WorkerList getHeaderPanelWorkerList() {
        return header_panel.getWorkerList();
    }

}


