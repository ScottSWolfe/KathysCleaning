package com.github.scottswolfe.kathyscleaning.weekend.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import javax.annotation.Nonnull;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.general.model.GlobalData;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;

/**
 * Panel in which user can enter other cleaning jobs.
 */
public class WeekendPanel extends JPanel {

    public static final int MAX_JOB_COUNT = 2;

    JLabel heading_label;
    public JLabel dateLabel;
    JButton submit_button;
    public JobPanel[] jp;

    public WeekendPanel(@Nonnull final Calendar weekendStartDay) {
        setLayout(new MigLayout());
        setBackground(Settings.BACKGROUND_COLOR);

        add(createHeaderPanel(weekendStartDay), "grow, wrap 0");
        add(createJobsWorkedPanel(), "grow");
    }

    private JPanel createHeaderPanel(@Nonnull final Calendar weekendStartDay) {

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill"));
        panel.setBackground(Settings.BACKGROUND_COLOR);

        heading_label = new JLabel();
        heading_label.setText("Weekend Work");
        heading_label.setFont(heading_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE));
        heading_label.setBackground(Settings.BACKGROUND_COLOR);

        dateLabel = new JLabel();
        dateLabel.setText(createDateLabelString(weekendStartDay));
        dateLabel.setFont(dateLabel.getFont().deriveFont(Settings.FONT_SIZE));
        dateLabel.setBackground(Settings.BACKGROUND_COLOR);

        submit_button = new JButton();
        submit_button.setText("Next");
        submit_button.setFont(submit_button.getFont().deriveFont(Settings.FONT_SIZE));
        submit_button.setBackground(Settings.MAIN_COLOR);
        submit_button.setForeground(Settings.FOREGROUND_COLOR);
        submit_button.addActionListener((event) -> ApplicationCoordinator.getInstance().launchNextForm());

        panel.add(heading_label, "span, center, wrap");

        JPanel p = new JPanel();
        p.setLayout(new MigLayout("fill"));
        p.setBackground(Settings.HEADER_BACKGROUND);
        p.setBorder(new LineBorder(null,1));

        p.add(dateLabel, "center");
        p.add(new JSeparator(SwingConstants.VERTICAL), "growy");

        p.add(submit_button, "");

        panel.add(p, "grow");
        return panel;
    }

    private JPanel createJobsWorkedPanel() {

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout());
        panel.setBackground(Settings.BACKGROUND_COLOR);

        jp = new JobPanel[MAX_JOB_COUNT];
        for (int i = 0; i < MAX_JOB_COUNT; i++) {
            jp[i] = new JobPanel();
            panel.add(jp[i], "grow, wrap");
        }

        return panel;
    }

    public static class JobPanel extends JPanel {

        public JCheckBox worked_checkbox;
        public JComboBox<String> customer_combobox;
        public JTextField jobpaid_field;
        public JComboBox<String> employee_combobox;
        public JTextField workerpaid_field;

        private JobPanel() {

            setLayout( new MigLayout("fill", "[]20[]20[]20[]20[]") );
            setBackground( Settings.BACKGROUND_COLOR );
            setBorder(BorderFactory.createTitledBorder(""));

            worked_checkbox = new JCheckBox();
            worked_checkbox.setBackground(Settings.BACKGROUND_COLOR);
            worked_checkbox.setText("");

            customer_combobox = new JComboBox<>();
            customer_combobox.setFont(customer_combobox.getFont().deriveFont(Settings.FONT_SIZE));
            customer_combobox.setEditable(true);
            customer_combobox.addItem("");

            // TODO temporary, move this elsewhere
            List<String> weekendCustomers = new ArrayList<>();

            try {
                File file = new File((System.getProperty("user.dir") + "\\save\\WeekendWorkSaveFile"));
                Scanner input = new Scanner(file);
                while(input.hasNext()) {
                    weekendCustomers.add(input.nextLine());
                }
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String customer : weekendCustomers) {
                customer_combobox.addItem(customer);
            }

            jobpaid_field = new JTextField();
            jobpaid_field.setColumns(5);
            jobpaid_field.setFont(jobpaid_field.getFont().deriveFont(Settings.FONT_SIZE));

            employee_combobox = new JComboBox<>();
            employee_combobox.setFont(employee_combobox.getFont().deriveFont(Settings.FONT_SIZE));
            employee_combobox.setEditable(false);
            employee_combobox.addItem("");
            GlobalData.getInstance().getDefaultWorkerNames().forEach(employee_combobox::addItem);

            workerpaid_field = new JTextField();
            workerpaid_field.setColumns(5);
            workerpaid_field.setFont(workerpaid_field.getFont().deriveFont(Settings.FONT_SIZE));

            JLabel worked_label = new JLabel();
            worked_label.setText("Worked?");
            worked_label.setFont(worked_label.getFont().deriveFont(Settings.FONT_SIZE));
            worked_label.setBackground(Settings.BACKGROUND_COLOR);

            JLabel customer_label = new JLabel();
            customer_label.setText("Customer");
            customer_label.setFont(customer_label.getFont().deriveFont(Settings.FONT_SIZE));
            customer_label.setBackground(Settings.BACKGROUND_COLOR);

            JLabel job_paid_label = new JLabel();
            job_paid_label.setText("$ Job");
            job_paid_label.setFont(job_paid_label.getFont().deriveFont(Settings.FONT_SIZE));
            job_paid_label.setBackground(Settings.BACKGROUND_COLOR);

            JLabel worker_label = new JLabel();
            worker_label.setText("Employee");
            worker_label.setFont(worker_label.getFont().deriveFont(Settings.FONT_SIZE));
            worker_label.setBackground(Settings.BACKGROUND_COLOR);

            JLabel worker_paid_label = new JLabel();
            worker_paid_label.setText("$ Paid");
            worker_paid_label.setFont(worker_paid_label.getFont().deriveFont(Settings.FONT_SIZE));
            worker_paid_label.setBackground(Settings.BACKGROUND_COLOR);

            add(worked_label, "");
            add(customer_label, "");
            add(job_paid_label, "");
            add(worker_label, "");
            add(worker_paid_label, "wrap");

            add(worked_checkbox, "center");
            add(customer_combobox, "");
            add(jobpaid_field, "");
            add(employee_combobox, "");
            add(workerpaid_field, "");
        }
    }

    public void setDate(@Nonnull final Calendar date) {
        dateLabel.setText(createDateLabelString(date));
    }

    private String createDateLabelString(@Nonnull final Calendar date) {
        return "Week of "
            + (Integer.parseInt(String.valueOf(date.get(Calendar.MONTH))) + 1)
            + "/"
            + (date.get(Calendar.DATE))
            + "/"
            + date.get(Calendar.YEAR);
    }
}
