package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_ExceptionListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_SubmitWeekListener;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionEntry;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;

import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCException;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class NW_DayPanel extends JPanel {

    GeneralController<ScheduledTabbedPane, NW_Data> controller;

    NoteData noteData;
    List<BeginExceptionEntry> beginExceptionList;

    TabbedPane tp;
    Calendar date;
    JFrame frame;

    private final ScheduledHeaderPanel scheduledHeaderPanel;
    private final ScheduledLBCPanel scheduledLBCPanel;
    public ScheduledHousesCopyWorkersPanel header_panel;
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

    public NW_DayPanel(GeneralController<ScheduledTabbedPane, NW_Data> controller, ScheduledTabbedPane tp,
            WorkerList workers, Calendar date, JFrame frame) {

        this.controller = controller;
        this.date = date;
        this.frame = frame;
        this.tp = tp;

        noteData = new NoteData();

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);

        scheduledHeaderPanel = ScheduledHeaderPanel.from(
            date,
            new PreviousDayListener(tp),
            new NextDayListener(tp),
            new NW_SubmitWeekListener(controller)
        );
        scheduledLBCPanel = ScheduledLBCPanel.from(
            BorderFactory.createMatteBorder(0, 1, 2, 1, Color.BLACK),
            new FrameCloseListener(frame)
        );
        header_panel = new ScheduledHousesCopyWorkersPanel(tp, workers, this, frame);
        begin_panel = createBeginPanel(workers);
        house_panels = new ArrayList<NW_HousePanel>();
        for(int i = 0; i < DayPanel.DEFAULT_HOUSE_PANEL_COUNT; i++) {
            house_panels.add(new NW_HousePanel(workers, this, frame));
        }
        cov_panel = new NW_CovenantPanel(this, frame);

        // creating scroll pane and adding house panels
        jsp_panel = new JPanel();
        jsp_panel.setLayout(new MigLayout("fill"));
        jsp_panel.setBackground(Settings.BACKGROUND_COLOR);

        for(NW_HousePanel house_panel : house_panels) {
            jsp_panel.add(house_panel, new String("wrap " + DayPanel.PANEL_PADDING + ", grow") );
        }

        jsp = new JScrollPane(jsp_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        jsp.setBackground( Settings.BACKGROUND_COLOR );

        addFlexibleFocusListeners();

        final JPanel housesPanel = new JPanel();
        housesPanel.setLayout(new MigLayout("fill, insets 0"));
        housesPanel.setBackground(Settings.BACKGROUND_COLOR);
        housesPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, Color.BLACK));
        housesPanel.add(begin_panel, "grow, wrap 0");
        housesPanel.add(header_panel, "grow, wrap 0");
        housesPanel.add(jsp, "grow");

        // Adding Elements onto Panel
        add(scheduledHeaderPanel, "grow, wrap 0");
        add(scheduledLBCPanel, "grow, wrap 0 ");
        add(housesPanel, "grow, wrap 0");
        add(cov_panel, "grow");
    }

    protected JPanel createBeginPanel(WorkerList workers) {

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("insets 0, fill"));
        panel.setBackground( Settings.BACKGROUND_COLOR );

        MatteBorder mborder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);
        panel.setBorder(mborder);

        final RowLabelPanel housesBeginLabel = RowLabelPanel.from("Houses");

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

        panel.add(housesBeginLabel);
        panel.add(meet_location_label,"gapx 0, align right");
        panel.add(meet_location_box,"gapx 0, align left");
        panel.add(meet_time_label,"gapx 0, align right");
        panel.add(meet_time_field,"gapx 0, align left");
        panel.add(exception_button,"gapx 0, align right");

        return panel;
    }

    public void setDate(final Calendar calendar) {
        scheduledHeaderPanel.setDate(calendar);
    }

    public void setWorkers(final List<List<String>> workerNames) {
        scheduledLBCPanel.setWorkers(workerNames);
        header_panel.setWorkers(WorkerList.from(workerNames));
        house_panels.forEach(house_panel -> house_panel.setWorkers(workerNames));
        cov_panel.setWorkers(WorkerList.from(workerNames));
    }

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

    public ScheduledLBCData getLBCData() {
        return ScheduledLBCData.from(
            scheduledLBCPanel.getMeetTime(),
            scheduledLBCPanel.getWorkerSelectionGrid(),
            scheduledLBCPanel.getScheduledLBCExceptions()
        );
    }

    public void setLBCData(final ScheduledLBCData lbcData) {
        scheduledLBCPanel.setLBCData(lbcData);
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

        // add workers from LBC panel
        scheduledLBCPanel.getWorkerSelectionGrid().stream()
            .flatMap(Collection::stream)
            .filter(Pair::getRight)
            .map(Pair::getLeft)
            .forEach(workers::add);
        scheduledLBCPanel.getScheduledLBCExceptions().stream()
            .map(ScheduledLBCException::getWorkerName)
            .filter(workerName -> !workerName.isEmpty())
            .forEach(workers::add);

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
                hp.worker_panel.getComponentOnLeft(),
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


