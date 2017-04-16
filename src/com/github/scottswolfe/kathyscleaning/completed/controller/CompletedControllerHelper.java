package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HeaderPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledControllerHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class CompletedControllerHelper implements ControllerHelper<TabbedPane, CompletedModel> {
   
/* PUBLIC METHODS =========================================================== */
    
    TabbedPane tp;
    
    @Override
    public CompletedModel readViewIntoModel(TabbedPane tp) {
        CompletedModel completedModel = new CompletedModel();
        DayData[] dayData = new DayData[5];
        
        // for each day
        for (int d = 0; d < dayData.length; d++) {
            
            // Header for day
            HeaderData headerData = new HeaderData();
            headerData.setDate(SessionModel.getCompletedStartDay());
            headerData.setWorkers(tp.day_panel[d].header_panel.getWorkers());
            
            // Houses in day
            HouseData[] houseData = new HouseData[tp.day_panel[d].house_panel.length];
            
            // for each house panel in the day
            for (int h = 0; h < houseData.length; h++) {
                houseData[h] = new HouseData();
                houseData[h].setHouseName(tp.day_panel[d].house_panel[h].house_name_txt.getText()); 
                houseData[h].setHousePay(tp.day_panel[d].house_panel[h].pay_txt.getText());
                houseData[h].setTimeBegin(tp.day_panel[d].house_panel[h].time_begin_txt.getText());
                houseData[h].setTimeEnd(tp.day_panel[d].house_panel[h].time_end_txt.getText());
                houseData[h].setSelectedWorkers(tp.day_panel[d].house_panel[h].worker_panel.getSelected());
                houseData[h].setWorkerList(tp.day_panel[d].house_panel[h].worker_panel.getWorkers());
                houseData[h].setExceptionData(tp.day_panel[d].house_panel[h].exception_data.getExceptionData());
            }
            
            dayData[d] = new DayData();
            dayData[d].setHouseData(houseData);
            dayData[d].setHeader(headerData);
            
        }  // end day panels
        
        completedModel.setDayData(dayData);
        return completedModel;
    }
    
    @Override
    public void writeModelToView(CompletedModel completedModel, TabbedPane tp) {
                
        DayPanel day_panel;
        DayData day_data;
        HousePanel house_panel;
        HouseData house_data;
        int num_house_panels;
        int num_house_datas;
        
        // iterate through each day
        for (int d = 0; d < 5; d++) {
            
            day_panel = tp.day_panel[d];
            day_data = completedModel.dayData[d];
            
            // set header panel
            HeaderPanel headerPanel = day_panel.header_panel;
            HeaderData headerData = day_data.getHeaderData();
            headerPanel.setDate(headerData.getDate());
            headerPanel.setWorkers(headerData.getWorkers());

            num_house_panels = day_panel.house_panel.length;
            num_house_datas = day_data.houseData.length;
            
            // iterate through each house
            for (int h = 0; h < num_house_datas; h++) {
                
                house_panel = day_panel.house_panel[h];
                house_data = day_data.houseData[h];
                
                house_panel.house_name_txt.setText(house_data.getHouseName());
                if (house_data.getHousePay() != 0) {
                    house_panel.pay_txt.setText(
                            String.valueOf(house_data.getHousePay()));
                }
                house_panel.time_begin_txt.setText(house_data.getTimeBegin());
                house_panel.time_end_txt.setText(house_data.getTimeEnd());                
                house_panel.worker_panel.setWorkers(house_data.getWorkerList());
                house_panel.exception_data = house_data.getExceptionData();
                
                // TODO add in this sort of functionality
                //if (house_data.getExceptionData().edited) {
                //    house_panel.exceptions.setBackground(Settings.MAIN_COLOR);
                //}
                
                // if there are more houses to fill in
                // and there are more empty house panels
                if (h < num_house_datas - 1 && h < num_house_panels - 1) {
                    // do nothing
                }
                // if there are more houses to fill in
                // but there are no more empty house panels 
                else if (h < num_house_datas - 1 && h >= num_house_panels - 1) {
                    ActionEvent event = new ActionEvent(day_panel, 0, "test");
                    ActionListener[] al =
                            house_panel.add_house.getActionListeners();
                    al[0].actionPerformed(event);
                }
                // if there are no more houses to fill in
                // and there are more empty house panels 
                else if (h >= num_house_datas - 1 && h < num_house_panels - 1) {
                    int numrepeat = num_house_panels - h - 1;
                    for (int k = h; k < numrepeat + h; k++) {
                        ActionEvent event =
                                new ActionEvent(day_panel, 0, "test");
                        ActionListener[] al = day_panel.house_panel[h + 1]
                                .delete_house.getActionListeners();
                        al[0].actionPerformed(event);
                    }
                }   
                            
            }     
        }
    }

    @Override
    public void saveToFile(CompletedModel model, File file) {
        JsonMethods.saveToFileJSON(model, CompletedModel.class,
                                   file, Form.COMPLETED.getNum());
    }
    
    @Override
    public CompletedModel loadFromFile(File file) {
        return (CompletedModel) JsonMethods.loadFromFileJSON(CompletedModel.class, file,
                                                   Form.COMPLETED.getNum());
    }
    
    @Override
    public void initializeForm(GeneralController<TabbedPane, CompletedModel> controller) {

        WorkerList workers = new WorkerList(WorkerList.HOUSE_WORKERS);
        
        TabbedPane tp = new TabbedPane();
        tp.setFont(tp.getFont().deriveFont(Settings.TAB_FONT_SIZE));

        // creating array of dates
        Calendar[] day = new Calendar[5];
        Calendar temp_date = SessionModel.getCompletedStartDay();
        for(int i = 0; i < day.length; i++) {            
            day[i] = Calendar.getInstance();
            day[i].set(temp_date.get(Calendar.YEAR), temp_date.get(Calendar.MONTH), temp_date.get(Calendar.DATE));
            temp_date.add(Calendar.DATE, 1);
        }
                
        controller.setView(tp);
        
        MainFrame<TabbedPane, CompletedModel> frame = new MainFrame<>(controller);
        
        DayPanel[] day_panel = new DayPanel[5];
        for(int i = 0; i < 5; i++){
            day_panel[i] = new DayPanel(controller,
                    tp, workers, day[i],
                    frame, Settings.TRUE_MODE, 2); // TODO remove wk = 2
        }
        tp.day_panel = day_panel;
        
        tp.addTab("Monday", day_panel[0]);
        tp.addTab("Tuesday", day_panel[1]);
        tp.addTab("Wednesday", day_panel[2]);
        tp.addTab("Thursday", day_panel[3]);
        tp.addTab("Friday", day_panel[4]);
        
        tp.changePreviousTab(0);
        tp.addChangeListener(new TabChangeListener(tp, frame));

        controller.readFileAndWriteToView(GeneralController.TEMP_SAVE_FILE);
        
        frame.setBackground(Settings.BACKGROUND_COLOR);
        frame.add(tp);
        frame.pack();
        frame.setLocationRelativeTo(null);        
        frame.setVisible(true);
    }
    
    @Override
    public void updateDate(TabbedPane tp) {
        this.tp = tp;
        ChooseWeekPanel.initializePanel(this, false);
    }
    
    @Override
    public void updateDateHelper() {
        Calendar[] days = new Calendar[5];
        Calendar temp_date = SessionModel.getCompletedStartDay();
        for(int i = 0; i < days.length; i++) {
            days[i] = Calendar.getInstance();
            days[i].set(temp_date.get(Calendar.YEAR), temp_date.get(Calendar.MONTH), temp_date.get(Calendar.DATE));
            temp_date.add(Calendar.DATE, 1);
        }

        for (int i = 0; i < tp.day_panel.length; i++) {
            tp.day_panel[i].header_panel.date = days[i];
            
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            weekDay = dayFormat.format(days[i].getTime());
            
            tp.day_panel[i].header_panel.day_label.setText(weekDay);
            tp.day_panel[i].header_panel.date_label.setText((Integer.parseInt(String.valueOf(days[i].get(Calendar.MONTH)))+1) + "/" + days[i].get(Calendar.DATE) + "/" + days[i].get(Calendar.YEAR));
        }
    }
    
    @Override
    public void eliminateWindow(TabbedPane tp) {
        @SuppressWarnings("rawtypes")
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(tp);
        frame.eliminate();
    }
    
    /**
     * Saves the amount of money earned at a house as the house's default
     * amount earned. // TODO rewrite so can be saved as key-value pairs in json
     */
    public static void saveHousePay(TabbedPane tp) {
        
        BufferedWriter bw = null;
        try {

            Scanner input = new Scanner( HouseNameDocListener.HOUSE_PAY_FILE );
            Scanner input2 = new Scanner( HouseNameDocListener.HOUSE_PAY_FILE );
                        
            int i=0;
            while (input2.hasNextLine()) {
                input2.nextLine();
                i++;
            }
            
            String[] s = new String[i];
            
            for( int j=0; j<i; j++) {
                s[j] = input.nextLine();
            }
            
            input.close();
            input2.close();
            
            // for each day
            for (int d=0; d<5; d++) {
                
                DayPanel dp = tp.day_panel[d];
                
                // for each house
                for (int h=0; h<dp.house_panel.length; h++) {
                    
                    boolean match = false;
                    
                    // for length of array
                    for (int k=0; k<s.length; k++) {
                        
                        if (s[k].equalsIgnoreCase( dp.house_panel[h].house_name_txt.getText() )) {
                            
                            s[k+1] = dp.house_panel[h].pay_txt.getText();
                            match = true;
                            break;
                        }
                        
                    }
                    if (match == false) {
                        
                        String[] r = new String[s.length+2];
                        
                        for (int l=0; l<s.length; l++) {
                            r[l] = s[l];
                        }
                        
                        r[r.length-2] = dp.house_panel[h].house_name_txt.getText();
                        r[r.length-1] = dp.house_panel[h].pay_txt.getText();
                        
                        s = r;
                    }
                }
            }
            
            FileWriter fw = new FileWriter( HouseNameDocListener.HOUSE_PAY_FILE );
            bw = new BufferedWriter( fw );
            for (int m=0; m<s.length; m++) {
                bw.write(s[m]);
                bw.newLine();
            }
            bw.close();         
        }
        catch(Exception e1){
            e1.printStackTrace();
        }
    }
    
    /**
     * Imports a scheduled form into completed form
     * 
     * @param file the file that has previously been completed
     * @param tp the view into which to import the schedule
     */
    public static void importSchedule(File file, TabbedPane tp) {
        ScheduledControllerHelper helper = new ScheduledControllerHelper();
        NW_Data scheduledModel = helper.loadFromFile(file);
        CompletedModel completedModel = scheduledToCompletedModel(scheduledModel);
        CompletedControllerHelper completedHelper = new CompletedControllerHelper();
        completedHelper.writeModelToView(completedModel, tp);
    }

    private static CompletedModel scheduledToCompletedModel(NW_Data scheduledModel) {
        CompletedModel completedModel = new CompletedModel();
        completedModel.setDayData(scheduledModel.completedDayData);
        return completedModel;
    }
    
}
