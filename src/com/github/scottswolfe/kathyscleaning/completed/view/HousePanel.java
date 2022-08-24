package com.github.scottswolfe.kathyscleaning.completed.view;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;

import com.github.scottswolfe.kathyscleaning.completed.controller.AddHouseListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.DeleteHouseListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.ExceptionListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.HouseNameDocListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.MoveDownListener;
import com.github.scottswolfe.kathyscleaning.completed.controller.MoveUpListener;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionEntry;
import com.github.scottswolfe.kathyscleaning.component.AmountEarnedPanel;
import com.github.scottswolfe.kathyscleaning.component.KcButton;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocFilter;
import com.github.scottswolfe.kathyscleaning.general.controller.TimeKeyListener;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

public class HousePanel extends JPanel {

/* INSTANCE VARIABLES ======================================================= */

    DayPanel day_panel;
    WorkerList dwd;
    JFrame frame;
    TabbedPane tp;

    private ExceptionData exception_data;



/* COMPONENTS =============================================================== */

    String title;
    Border border;

    public WorkerSelectPanel workerSelectPanel;
    private final AmountEarnedPanel amountEarnedPanel;

    public KcButton exceptions;

    JLabel house_name_label;
    JLabel time_label;

    public JTextField house_name_txt;
    public JTextField time_begin_txt;
    public JTextField time_end_txt;

    KcButton move_up;
    KcButton move_down;
    public KcButton add_house;
    public KcButton delete_house;



/* CONSTRUCTORS ============================================================= */

    public HousePanel(String title, WorkerList dwd, DayPanel day_panel, JFrame frame, TabbedPane tp) {

        this.day_panel = day_panel;
        this.frame = frame;
        this.tp = tp;

        // TODO temporary hack
        dwd = new WorkerList(WorkerList.HOUSE_WORKERS);

        exception_data = new ExceptionData();

        setLayout(new MigLayout("insets 0","[grow][grow][grow][grow][grow][grow]","[]"));
        setBackground(Settings.BACKGROUND_COLOR);
        setBorder(BorderFactory.createTitledBorder(new String()));

        JPanel house_name_panel = houseNamePanel();
        amountEarnedPanel = AmountEarnedPanel.from();
        JPanel time_panel = timePanel();
        workerSelectPanel = WorkerSelectPanel.from(dwd, Settings.BACKGROUND_COLOR, time_end_txt, exceptions);
        JPanel button_panel = buttonPanel();

        exceptions = new KcButton(
            "Exceptions",
            new ExceptionListener(
                dwd,
                frame,
                () -> this.exception_data,
                (newExceptionData) -> this.exception_data = newExceptionData)
        );

        add(house_name_panel, "growy");
        add(amountEarnedPanel, "growy");
        add(time_panel, "growy");
        add( new JSeparator(SwingConstants.VERTICAL), "growy" );

        add(workerSelectPanel, "pushy");
        add(exceptions, "hmin 50, pushy");
        add( new JSeparator(SwingConstants.VERTICAL), "growy" );

        add(button_panel, "growy");
    }


/* PRIVATE CONSTRUCTION METHODS ============================================= */

    //house name panel
    private JPanel houseNamePanel(){
        JPanel panel = new JPanel();

        panel.setLayout(new MigLayout("insets 0, ay 50%"));
        panel.setBackground( Settings.BACKGROUND_COLOR );

        house_name_label = new JLabel("House Name");
        house_name_label.setFont( house_name_label.getFont().deriveFont( Settings.FONT_SIZE ) );

        house_name_txt = new JTextField(10);
        house_name_txt.setFont( house_name_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
        house_name_txt.getDocument().addDocumentListener( new HouseNameDocListener( this ) );

        panel.add(house_name_label,"wrap, gap 3");
        panel.add(house_name_txt);

        return panel;
    }

    //time panel
    private JPanel timePanel(){
        JPanel panel = new JPanel();

        time_label = new JLabel("Time");
        time_label.setFont( time_label.getFont().deriveFont( Settings.FONT_SIZE ) );

        time_begin_txt = new JTextField( 5 );
        time_begin_txt.setFont( time_begin_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
        AbstractDocument time_begin_doc = (AbstractDocument)time_begin_txt.getDocument();
        TimeDocFilter tdf_begin = new TimeDocFilter(time_begin_txt );
        time_begin_doc.setDocumentFilter( tdf_begin );
        time_begin_txt.addKeyListener( new TimeKeyListener( tdf_begin ) );

        time_end_txt = new JTextField(5);
        time_end_txt.setFont( time_end_txt.getFont().deriveFont( Settings.FONT_SIZE ) );
        AbstractDocument time_end_doc = (AbstractDocument)time_end_txt.getDocument();
        TimeDocFilter tdf_end = new TimeDocFilter(time_end_txt );
        time_end_doc.setDocumentFilter( tdf_end );
        time_end_txt.addKeyListener( new TimeKeyListener( tdf_end ) );

        panel.setLayout( new MigLayout("insets 0, ay 50%") );
        panel.setBackground( Settings.BACKGROUND_COLOR );

        panel.add(time_label, "cell 0 0 1 1, wrap, ax 50%");
        panel.add(time_begin_txt, "cell 0 1");
        panel.add(time_end_txt, "cell 0 1, gap 0");

        return panel;
    }

    // button panel
    private JPanel buttonPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("insets 0"));
        panel.setBackground(Settings.BACKGROUND_COLOR);

        move_up = new KcButton("Up", new MoveUpListener(day_panel,this,dwd,frame,tp));
        move_down = new KcButton("Down", new MoveDownListener(day_panel,this,dwd,frame,tp));
        add_house = new KcButton("Add", new AddHouseListener(day_panel,this,dwd,frame,tp));
        delete_house = new KcButton("Delete", new DeleteHouseListener(day_panel,this,dwd,frame,tp));

        panel.add(move_up, "growx");
        panel.add(add_house,"wrap, growx");
        panel.add(move_down);
        panel.add(delete_house);

        return panel;
    }



/* PUBLIC METHODS =========================================================== */

    public HousePanel copyPanel( ) {

        HousePanel new_panel = new HousePanel(this.title, this.dwd, this.day_panel, this.frame, this.tp);

        new_panel.house_name_txt.setText(this.house_name_txt.getText());
        new_panel.amountEarnedPanel.setAmountEarnedText(this.amountEarnedPanel.getAmountEarnedText());
        new_panel.time_begin_txt.setText(this.time_begin_txt.getText());
        new_panel.time_end_txt.setText(this.time_end_txt.getText());
        new_panel.exception_data = this.exception_data;

        // temporary hack
        WorkerList workers = this.day_panel.header_panel.getWorkers();
        new_panel.workerSelectPanel.setWorkers(workers);

        return new_panel;
    }

    public HousePanel changeHouseWorkers(WorkerList dwd) {
        HousePanel new_panel = new HousePanel(this.title, dwd, this.day_panel, this.frame, this.tp);
        new_panel.title = this.title;
        new_panel.house_name_txt.setText(this.house_name_txt.getText());
        new_panel.amountEarnedPanel.setAmountEarnedText(this.amountEarnedPanel.getAmountEarnedText());
        new_panel.time_begin_txt.setText(this.time_begin_txt.getText());
        new_panel.time_end_txt.setText(this.time_end_txt.getText());
        return new_panel;
    }

    public void setTitle( String title ){
        this.title = title;
        this.setBorder(BorderFactory.createTitledBorder(border,title));
    }

    /**
     * Sets the exception button to a new color if an exception exists
     *
     * @param isException true if there is an exception, otherwise false
     */
    public void setExceptionButtonColor(boolean isException) {
        exceptions.setForeground(Settings.MAIN_COLOR);
    }

    public ExceptionData getExceptionData() {
        return exception_data;
    }

    public void setExceptionData(ExceptionData exceptionData) {
        exception_data = exceptionData;
        setExceptionButtonColor();
    }

    public void setExceptionDataEntries(List<ExceptionEntry> entries) {
        exception_data.setEntries(entries);
        setExceptionButtonColor();
    }

    public boolean hasExceptionData() {
        return exception_data.isException();
    }

    private void setExceptionButtonColor() {
        if (exception_data.isException()) {
            exceptions.setBackground(Settings.EDITED_BUTTON_COLOR);
        } else {
            exceptions.setBackground(Settings.DEFAULT_BUTTON_COLOR);
        }
    }

    public JComponent getAmountEarnedComponent() {
        return amountEarnedPanel.getComponent();
    }

    public String getAmountEarnedText() {
        return amountEarnedPanel.getAmountEarnedText();
    }

    public void setAmountEarnedText(final String newText) {
        amountEarnedPanel.setAmountEarnedText(newText);
    }
}
