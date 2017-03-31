package com.github.scottswolfe.kathyscleaning.weekend.controller;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendEntry;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel.JobPanel;

public class WeekendControllerHelper
        implements ControllerHelper<WeekendPanel, WeekendModel> {

    WeekendPanel panel;
    
    @Override
    public WeekendModel readViewIntoModel(WeekendPanel view) {

        WeekendModel model = new WeekendModel();
        WeekendEntry entry;
        JobPanel jp;
        
        for (int i = 0; i < WeekendPanel.NUM_JOB_PANELS; i++) {
            entry = new WeekendEntry();
            jp = view.jp[i];
            
            entry.setWorkedIsChecked(jp.worked_checkbox.isSelected());
            entry.setCustomer(String.valueOf(jp.customer_combobox.getSelectedItem()));
            if (!jp.jobpaid_field.getText().isEmpty()) {
                entry.setAmountReceived(Double.parseDouble(jp.jobpaid_field.getText()));
            } else {
                entry.setAmountReceived(0.0);
            }
            entry.setEmployee(String.valueOf(jp.employee_combobox.getSelectedItem()));
            if (!jp.workerpaid_field.getText().isEmpty()) {
                entry.setAmountPaid(Double.parseDouble(jp.workerpaid_field.getText()));
            } else {
                entry.setAmountPaid(0.0);
            }
            
            model.addEntry(entry);
        }
        return model;
    }

    @Override
    public void writeModelToView(WeekendModel model, WeekendPanel view) {
        
        view.date = model.getDate();
        
        List<WeekendEntry> entries = model.getEntries();
        JobPanel jp;
        int i = 0;
        for (WeekendEntry entry : entries) {
            jp = view.jp[i];
            jp.worked_checkbox.setSelected(entry.isWorkedIsChecked());
            jp.customer_combobox.setSelectedItem(entry.getCustomer());
            jp.jobpaid_field.setText(String.valueOf(entry.getAmountReceived()));
            jp.employee_combobox.setSelectedItem(entry.getEmployee());
            jp.workerpaid_field.setText(String.valueOf(entry.getAmountPaid()));
            i++;
        }
    }

    @Override
    public void saveToFile(WeekendModel model, File file) {
        JsonMethods.saveToFileJSON(model, WeekendModel.class, file, Form.WEEKEND.getNum());
    }

    @Override
    public WeekendModel loadFromFile(File file) {
        return (WeekendModel) JsonMethods.loadFromFileJSON(WeekendModel.class, file, Form.WEEKEND.getNum());
    }

    @Override
    public void initializeForm(GeneralController<WeekendPanel, WeekendModel> controller) {
        
        WeekendPanel wp = new WeekendPanel(controller, 0, 0); // TODO remove 0, 0
        MainFrame<WeekendPanel, WeekendModel> weekendFrame = new MainFrame<>(controller);
        
        controller.setView(wp);
        wp.setFrame(weekendFrame);          
        
        controller.readFileAndWriteToView(GeneralController.TEMP_SAVE_FILE);

        weekendFrame.add(wp);
        weekendFrame.pack();
        weekendFrame.setLocationRelativeTo(null);
        weekendFrame.setVisible(true);
    }
    
    @Override
    public void updateDate(WeekendPanel wp) {
        this.panel = wp;
        ChooseWeekPanel.initializePanel(this, false);
    }
    
    @Override
    public void updateDateHelper() {
        Calendar date = SessionModel.getCompletedStartDay();
        String s = new String( "Week of " +
                ( Integer.parseInt(String.valueOf(date.get(Calendar.MONTH)))+1 ) +
                "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR) );
        panel.date_label.setText(s);
    }
    
    @Override
    public void eliminateWindow(WeekendPanel view) {
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(view);
        frame.eliminate();
    }
    
    /*
    stuff () {
        if (!StaticMethods.confirmSubmitWeek()) {
            return;
        }
        
        if (mode == Settings.TRUE_MODE) {
        // reading data and writing to Excel Document
        try {
        File f = Settings.excelFile;
        InputStream inp = new FileInputStream(f);
        XSSFWorkbook wb = new XSSFWorkbook(inp);
        Sheet sheet = wb.getSheet("PAYROLL");
        
        Row row;
        boolean found_row;
        boolean found_worker;
        int num_jobs_cap = NUM_JOBS_CAP;
        int unchecked = 0;
        int job_num;
        int repeat = 0;
        
        
        // for each job panel
        for (int i=0; i<NUM_JOB_PANELS; i++) {
            
            // if checkbox is checked
            if (jp[i].worked_checkbox.isSelected()) {
                
                // counting the unique job number, so the program knows 
                // whether to write to a new line on the excel doc or not
                job_num = i + 1 - unchecked - repeat;
                if (i>0) {
                    
                    for (int j=0; j<i; j++) {
                        if ( String.valueOf(jp[i].customer_combobox.getSelectedItem()).equals(String.valueOf(jp[j].customer_combobox.getSelectedItem())) ) {
                            job_num = j + 1;
                            repeat++;
                            break;
                        }
                        
                    }
                    
                }
                if ( job_num > num_jobs_cap ) {
                    
                    JOptionPane.showMessageDialog( new JFrame(), "Error: the number of weekend jobs you chose will not fit in the Excel Sheet.\nYou need to modify the Excel sheet if you want to include that many unique jobs.", null, JOptionPane.ERROR_MESSAGE);
                    break;
                }
                
                
                row = sheet.getRow(0);
                
                // find correct row
                found_row = false;
                while(found_row == false) {
                    
                    if (row != null && row.getCell(9) != null && String.valueOf(row.getCell(9)).equals("WEEKEND WORK")) {
                        found_row = true;
                        row = sheet.getRow(row.getRowNum() + 1 + job_num);
                        break;
                    }
                    row = sheet.getRow(row.getRowNum() + 1);
                }
                
                if ( !String.valueOf( jp[i].customer_combobox.getSelectedItem()).isEmpty() ) {
                    row.getCell(3).setCellValue( String.valueOf( jp[i].customer_combobox.getSelectedItem() ));
                }
                if ( !jp[i].jobpaid_field.getText().isEmpty() ) {
                    row.getCell(4).setCellValue( Integer.parseInt( jp[i].jobpaid_field.getText() ));
                }
                
                // if worker selected
                if ( String.valueOf(jp[i].employee_combobox.getSelectedItem()) != null &&
                        !String.valueOf(jp[i].employee_combobox.getSelectedItem()).isEmpty() ) {
                    
                    // find worker
                    row = sheet.getRow(row.getRowNum() - job_num);
                    found_worker = false;
                    int index = 5; // this is where names begin on the excel sheet
                    while (found_worker == false) {
                        
                        // if cell matches worker's name
                        if (row.getCell(index) != null &&
                            String.valueOf(row.getCell(index)).equals(String.valueOf(jp[i].employee_combobox.getSelectedItem()))) {
                            found_worker = true;
                            break;
                        }
                        else if (row.getCell(index) != null &&
                                String.valueOf(row.getCell(index)).equals("Kathy")) {
                            
                            String message = "Error: the selected employee " + String.valueOf(jp[i].employee_combobox.getSelectedItem()) +
                                    " is not on the Excel Sheet. Please modify the Excel sheet as needed.";
                            JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    
                        index++;
                    }
                    
                    // do stuff once worker is found
                    if (found_worker == true) {
                        row = sheet.getRow(row.getRowNum() + job_num);
                        row.getCell(index).setCellValue( Integer.parseInt( jp[i].workerpaid_field.getText() ));
                    }
                    
                }
                
            }
            else {
                unchecked++;
            }
            
        }
        
        
        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
        
        OutputStream out = new FileOutputStream(f);
        wb.write(out);
        wb.close();
        inp.close();
        
        }
        catch(Exception e1){
            JOptionPane.showMessageDialog(new JFrame(), "Error", null, JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
        
        
        
        // creating new frame for next week panel and disposing of Weekend panel
        JFrame nwframe = new JFrame();
        nwframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        nwframe.setResizable(false);
        nwframe.addWindowListener( new MainWindowListener() );
            
        ChooseWeekPanel cwp = new ChooseWeekPanel(nwframe, ChooseWeekPanel.NEXT_WEEK);
                
        nwframe.add(cwp);
        nwframe.pack();
        
        Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        
        int difference = (int) effectiveScreenSize.getWidth() - nwframe.getWidth();
        int new_x = (int) difference/2;
        
        nwframe.setLocation( new Point(new_x , 20) );
            
        frame.setVisible( false );
        frame.dispose();
                    
        // set 
        if ( wk == Settings.WEEK_A ) {
            cwp.week_B_rbutton.setSelected(true);
        }
        else if ( wk == Settings.WEEK_B ) {
            cwp.week_A_rbutton.setSelected(true);
        }
        else {
            // do nothing
        }
        
        
        
        
        nwframe.setVisible(true);
        
        } // end if true mode
        
        
        
        // else if settings mode
        else {
            
            
            
            File f;
            if (wk == Settings.WEEK_A) {
                f = Settings.WEEKEND_WEEK_A;
            }
            else {
                f = Settings.WEEKEND_WEEK_B;
            }
            
            BufferedWriter bw = null;
            try {
                
                FileWriter fw = new FileWriter( f );
                bw = new BufferedWriter( fw );
                
                for (int i=0; i<NUM_JOB_PANELS; i++) {
                    
                    if (jp[i].worked_checkbox.isSelected()) {
                        bw.write("true");
                    }
                    else {
                        bw.write("false");
                    }
                    bw.newLine();
                    
                    if (jp[i].customer_combobox != null) {
                        bw.write(String.valueOf(jp[i].customer_combobox.getSelectedItem()));
                    }
                    else {
                        bw.write("");
                    }
                    bw.newLine();
                    
                    bw.write(jp[i].jobpaid_field.getText());
                    bw.newLine();
                    
                    if (jp[i].employee_combobox != null) {
                        bw.write(String.valueOf(jp[i].employee_combobox.getSelectedItem()));
                    }
                    else {
                        bw.write("");
                    }
                    bw.newLine();
                    
                    bw.write(jp[i].workerpaid_field.getText());
                    bw.newLine();
                    
                }
                
                bw.close();
                
            }
            catch(Exception e1) {
                e1.printStackTrace();
            }
            
            
            JFrame nwframe = new JFrame();
            nwframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            nwframe.setResizable(false);
            nwframe.addWindowListener( new MainWindowListener() );
            
            
            //Reading Default Worker Data from save file
            WorkerList dwd_house = null;
            try {
                dwd_house = new WorkerList( WorkerList.HOUSE_WORKERS);
            } catch (Exception e1) {
                e1.printStackTrace();
            }               
            
            GeneralController<TabbedPane, NW_Data> controller = 
                    new GeneralController<>(Form.SCHEDULED);
            
            TabbedPane tp = new TabbedPane(controller);
            tp.setFont( tp.getFont().deriveFont(Settings.TAB_FONT_SIZE) );
            tp.setBackground( Settings.BACKGROUND_COLOR );
            
            Calendar date = Calendar.getInstance();
            while (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                date.add(Calendar.DAY_OF_MONTH, 1);
            }
            Calendar[] day = new Calendar[5];
            for(int i=0; i<day.length; i++) {   
                day[i] = (Calendar) date.clone();
                date.add(Calendar.DATE, 1);
            }
            
            NW_DayPanel[] day_panel = new NW_DayPanel[5];
            for(int i=0; i<5; i++){
                day_panel[i] = new NW_DayPanel(controller, tp, dwd_house,
                        day[i], nwframe, Settings.EDIT_MODE, wk);
            }
            tp.nw_day_panel = day_panel;
            
            tp.addTab("Monday", day_panel[0]);
            tp.addTab("Tuesday", day_panel[1]);
            tp.addTab("Wednesday", day_panel[2]);
            tp.addTab("Thursday", day_panel[3]);
            tp.addTab("Friday", day_panel[4]);
            
            tp.changePreviousTab(0);
            tp.addChangeListener( new NW_TabChangeListener(tp, nwframe) );
            
            nwframe.setBackground(Settings.BACKGROUND_COLOR);
            
            nwframe.add(tp);
            nwframe.pack();
            
            Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
            
            int difference = (int) effectiveScreenSize.getWidth() - nwframe.getWidth();
            int new_x = (int) difference/2;
            
            nwframe.setLocation( new Point(new_x , 20) );
            
            frame.setVisible( false );
            frame.dispose();
            
            try {
                if ( wk == Settings.WEEK_A ) {
                    NW_DayPanel.fillWeek(tp, nwframe, Settings.WEEK_B);
                    for (int j=0; j<5; j++) {
                        tp.nw_day_panel[j].header_panel.week_B.setSelected(true);
                    }
                }
                else if ( wk == Settings.WEEK_B ) {
                    NW_DayPanel.fillWeek(tp, nwframe, Settings.WEEK_A);
                    for (int j=0; j<5; j++) {
                        tp.nw_day_panel[j].header_panel.week_A.setSelected(true);
                    }
                }
                else {
                    for (int j=0; j<5; j++) {
                        tp.nw_day_panel[j].header_panel.neither.setSelected(true);
                    }
                }
            }
            catch (Exception e2) {
                // do nothing
            }
            
            
            for (int i=0; i<5; i++) {
                day_panel[i].header_panel.week_A.setEnabled(false);
                day_panel[i].header_panel.week_B.setEnabled(false);
                day_panel[i].header_panel.neither.setEnabled(false);
            }
            
            nwframe.setVisible(true);
    }
    */
}
