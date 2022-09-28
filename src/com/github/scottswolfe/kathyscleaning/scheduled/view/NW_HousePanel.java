package com.github.scottswolfe.kathyscleaning.scheduled.view;

import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.component.WorkerSelectPanel;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_AddHouseListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_DeleteHouseListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_MoveDownListener;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.NW_MoveUpListener;

import net.miginfocom.swing.MigLayout;

public class NW_HousePanel extends JPanel {

    NW_DayPanel parent_day_panel;

    public WorkerSelectPanel worker_panel;
    JLabel house_name_label;
    public JTextField house_name_text_field;
    JButton move_up;
    JButton move_down;
    public JButton add_house;
    public JButton delete_house;

    public NW_HousePanel(String house_name, WorkerList workers, NW_DayPanel day_panel) {

        this.parent_day_panel = day_panel;

        setBorder(BorderFactory.createTitledBorder( new String() ));
        setBackground(Settings.BACKGROUND_COLOR);
        setLayout(new MigLayout("fillx, insets 0"));

        JPanel house_name_panel = houseNamePanel(house_name);
        worker_panel = WorkerSelectPanel.from(workers, Settings.BACKGROUND_COLOR);
        JPanel button_panel = buttonPanel(workers);

        add(house_name_panel, "growy");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(worker_panel, "push y");
        add(new JSeparator(SwingConstants.VERTICAL), "growy");
        add(button_panel, "growy");
    }

    public NW_HousePanel(WorkerList workers, NW_DayPanel day_panel) {
        this("", workers, day_panel);
    }

    public NW_HousePanel() {}

    private JPanel houseNamePanel(String house_name){
        JPanel panel = new JPanel();

        house_name_label = new JLabel(" House Name");
        house_name_label.setFont( house_name_label.getFont().deriveFont( Settings.FONT_SIZE ) );
        house_name_label.setForeground(Settings.MAIN_COLOR);

        house_name_text_field = new JTextField(10);
        house_name_text_field.setText(house_name);
        house_name_text_field.setFont( house_name_text_field.getFont().deriveFont( Settings.FONT_SIZE ) );

        panel.setLayout(new MigLayout("insets 0 1 0 1, ay 50%"));
        panel.setBackground(Settings.BACKGROUND_COLOR);

        panel.add(house_name_label,"wrap");
        panel.add(house_name_text_field);

        return panel;
    }

    private JPanel buttonPanel(WorkerList workers){
        JPanel panel = new JPanel();

        move_up = new JButton("Up");
        move_up.setFont( move_up.getFont().deriveFont( Settings.FONT_SIZE ) );


        move_down = new JButton("Down");
        move_down.setFont( move_down.getFont().deriveFont( Settings.FONT_SIZE ) );

        add_house = new JButton("Add");
        add_house.setFont( add_house.getFont().deriveFont( Settings.FONT_SIZE ) );

        delete_house = new JButton("Delete");
        delete_house.setFont( delete_house.getFont().deriveFont( Settings.FONT_SIZE ) );

        panel.setLayout( new MigLayout("insets 0") );
        panel.setBackground(Settings.BACKGROUND_COLOR);

        panel.add(move_up, "growx");
        panel.add(add_house,"wrap, growx");
        panel.add(move_down);
        panel.add(delete_house);

        move_up.addActionListener(new NW_MoveUpListener(parent_day_panel, this, workers));
        add_house.addActionListener(new NW_AddHouseListener(parent_day_panel,this, workers));
        move_down.addActionListener(new NW_MoveDownListener(parent_day_panel,this, workers));
        delete_house.addActionListener(new NW_DeleteHouseListener(parent_day_panel,this, workers));

        return panel;
    }

    public List<String> getSelectedWorkers() {
        return worker_panel.getSelectedWorkerNames();
    }

    public void setWorkers(final List<List<String>> workerNames) {
        worker_panel.updateWorkerNames(workerNames);
    }

    public String getHouseName() {
        return house_name_text_field.getText();
    }

    public void setHouseName(String house_name) {
        house_name_text_field.setText(house_name);
    }
}
