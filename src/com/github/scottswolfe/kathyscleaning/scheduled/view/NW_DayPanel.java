package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.MatteBorder;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.component.Button;
import com.github.scottswolfe.kathyscleaning.component.RowLabelPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.NextDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.PreviousDayListener;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_ExceptionListener;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionEntry;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NoteData;

import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.ScheduledLBCException;
import com.google.common.collect.ImmutableList;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.Pair;

public class NW_DayPanel extends JPanel implements FocusableCollection {

    NoteData noteData;
    List<BeginExceptionEntry> beginExceptionList;

    JTabbedPane tp;
    Calendar date;

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
    private Button exception_button;

    public NW_DayPanel(ScheduledTabbedPane tp, WorkerList workers, Calendar date) {
        this.date = date;
        this.tp = tp;

        beginExceptionList = new ArrayList<>();
        noteData = new NoteData();

        setLayout(new MigLayout("fill, insets 0"));
        setBackground(Settings.BACKGROUND_COLOR);

        scheduledHeaderPanel = ScheduledHeaderPanel.from(
            date,
            new PreviousDayListener(tp),
            new NextDayListener(tp),
            (event) -> ExcelMethods.chooseFileAndGenerateExcelDoc()
        );
        scheduledLBCPanel = ScheduledLBCPanel.from(
            BorderFactory.createMatteBorder(0, 1, 2, 1, Color.BLACK)
        );
        header_panel = new ScheduledHousesCopyWorkersPanel(tp, workers, this);
        begin_panel = createBeginPanel(workers);
        house_panels = new ArrayList<NW_HousePanel>();
        for(int i = 0; i < DayPanel.DEFAULT_HOUSE_PANEL_COUNT; i++) {
            house_panels.add(new NW_HousePanel(workers, this));
        }
        cov_panel = new NW_CovenantPanel(this);

        // creating scroll pane and adding house panels
        jsp_panel = new JPanel();
        jsp_panel.setLayout(new MigLayout("fillx, insets 4 0 0 0"));
        jsp_panel.setBackground(Settings.BACKGROUND_COLOR);

        for (int index = 0; index < house_panels.size(); index++) {
            jsp_panel.add(house_panels.get(index), getHousePanelConstraints(house_panels.size(), index));
        }

        jsp = new JScrollPane(jsp_panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                   ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED );
        jsp.setBackground( Settings.BACKGROUND_COLOR );
        jsp.setBorder(BorderFactory.createEmptyBorder());

        final JPanel housesPanel = new JPanel();
        housesPanel.setLayout(new MigLayout("fill, insets 0"));
        housesPanel.setBackground(Settings.BACKGROUND_COLOR);
        housesPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, Color.BLACK));
        housesPanel.add(begin_panel, "growx, wrap 0");
        housesPanel.add(header_panel, "growx, wrap 0");
        housesPanel.add(jsp, "grow, pushy");

        // Adding Elements onto Panel
        add(scheduledHeaderPanel, "growx, wrap 0");
        add(scheduledLBCPanel, "growx, wrap 0 ");
        add(housesPanel, "grow, pushy, wrap 0");
        add(cov_panel, "growx");
    }

    private String getHousePanelConstraints(final int housePanelCount, final int index) {
        final int padding = index < housePanelCount - 1 ? DayPanel.PANEL_PADDING : 0;
        return "wrap " + padding + ", growx";
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
        meet_location_box.setSelectedItem("");

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

        // todo: rewrite code from NW_ExceptionListener so we are not triggering an action on a listener
        exception_button = Button.from(
            "Exceptions",
            Settings.QUIET_BUTTON_COLORS,
            () -> new NW_ExceptionListener(this, workers).actionPerformed(null)
        );

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
        scheduledLBCPanel.updateWorkerNames(workerNames);
        header_panel.setWorkers(WorkerList.from(workerNames));
        house_panels.forEach(house_panel -> house_panel.setWorkers(workerNames));
        cov_panel.setWorkers(WorkerList.from(workerNames));
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
        final Object selectedItem = meet_location_box.getEditor().getItem();
        return selectedItem != null ? String.valueOf(selectedItem) : "";
    }

    public String getMeetTime() {
        if (meet_time_field.getText() != null) {
            return meet_time_field.getText();
        }
        else {
            return "";
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
            cov_panel.setNoteButtonColor(Settings.QUIET_BUTTON_COLORS);
        } else {
            cov_panel.setNoteButtonColor(Settings.LOUD_BUTTON_COLORS);
        }
    }

    public List<BeginExceptionEntry> getBeginExceptionList() {
        return beginExceptionList;
    }

    public void setBeginExceptionList(List<BeginExceptionEntry> beginExceptions) {
        this.beginExceptionList = beginExceptions;
        if (isBeginException()) {
            exception_button.setColors(Settings.LOUD_BUTTON_COLORS);
        } else {
            exception_button.setColors(Settings.QUIET_BUTTON_COLORS);
        }
    }

    public int getNumHousePanels() {
        return house_panels.size();
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {

        // todo: make all of the following components implement FocusableCollection

        return ImmutableList.of(
            ImmutableList.of(scheduledHeaderPanel),
            ImmutableList.of(scheduledLBCPanel),
            ImmutableList.of(begin_panel),
            ImmutableList.of(header_panel),
            ImmutableList.of(jsp_panel),
            ImmutableList.of(cov_panel)
        );
    }
}
