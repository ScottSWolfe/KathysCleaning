package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Scanner;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HeaderPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class CompletedControllerHelper implements ControllerHelper<TabbedPane, Data> {
   
/* PUBLIC METHODS =========================================================== */
    
    @Override
    public Data readViewIntoModel(TabbedPane tp) {
        Data data = new Data();
        DayData[] dayData = new DayData[5];
        
        // for each day
        for (int d = 0; d < dayData.length; d++) {
            
            // Header for day
            HeaderData headerData = new HeaderData();
            headerData.setDate(tp.day_panel[d].header_panel.date);
            headerData.setWeekSelected(tp.day_panel[d].header_panel.getWeekSelected());
            headerData.setDWD(tp.day_panel[d].header_panel.getWorkers());
            
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
                houseData[h].setExceptionData(tp.day_panel[d].house_panel[h].exception_data.getExceptionData());
            }
            
            dayData[d] = new DayData();
            dayData[d].setHouseData(houseData);
            dayData[d].setHeader(headerData);
            
        }  // end day panels
        
        data.setDayData(dayData);
        data.setDate(tp.day_panel[0].header_panel.date);
        return data;
    }
    
    @Override
    public void writeModelToView(Data data, TabbedPane tp) {
                
        DayPanel day_panel;
        DayData day_data;
        HousePanel house_panel;
        HouseData house_data;
        HeaderPanel header_panel;
        HeaderData header_data;
        int num_house_panels;
        int num_house_datas;
        int weekSelected;
        
        // iterate through each day
        for (int d = 0; d < 5; d++) {
            
            day_panel = tp.day_panel[d];
            day_data = data.dayData[d];
            
            num_house_panels = day_panel.house_panel.length;
            num_house_datas = day_data.houseData.length;
            
            // iterate through each house
            for (int h = 0; h < day_panel.house_panel.length; h++) {
                
                house_panel = day_panel.house_panel[h];
                house_data = day_data.houseData[h];
                
                house_panel.house_name_txt.setText(house_data.getHouseName());
                house_panel.pay_txt.setText(
                        String.valueOf(
                        house_data.getHousePay()));
                house_panel.time_begin_txt.setText(house_data.getTimeBegin());
                house_panel.time_end_txt.setText(house_data.getTimeEnd());
                
                // TODO create method to set view blank before filling in ??
                // unselect any selected workers
                for(int l = 0; l < DefaultWorkerPanel.NORM_ROWS; l++) {
                    for(int m = 0; m < DefaultWorkerPanel.NORM_COLUMNS; m++) {
                        house_panel.worker_panel.workerCheckBoxes[l][m].setSelected(false);
                    }
                }
                
                // set selected workers
                // TODO this can be made more efficient by breaking out of
                // the double for loop when finding a match
                for (String worker : house_data.getSelectedWorkers()) {
                    for(int l = 0; l < DefaultWorkerPanel.NORM_ROWS; l++){
                        for(int m = 0; m < DefaultWorkerPanel.NORM_COLUMNS; m++){
                            if (worker.equals(house_panel.worker_panel.workerCheckBoxes[l][m].getText())) {
                                house_panel.worker_panel.workerCheckBoxes[l][m].setSelected(true);
                            }
                        }
                    }
                }
                        
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
            
            // setting header panel data
            // TODO more to do here (eg: dates, etc)
            header_panel = day_panel.header_panel;
            header_data = day_data.getHeaderData();
            
            weekSelected = header_data.getWeekSelected();
            if (weekSelected == Settings.WEEK_A) {
                header_panel.week_A.setSelected(true);
            }
            else if (weekSelected == Settings.WEEK_B){
                header_panel.week_B.setSelected(true);
            }
            else {
                header_panel.neither.setSelected(true);
            }
            
        }
    }

    @Override
    public void saveToFile(Data model, File file) {
        JsonMethods.saveToFileJSON(model, Data.class,
                                   file, Form.COMPLETED.getNum());
    }
    
    @Override
    public Data loadFromFile(File file) {
        return (Data) JsonMethods.loadFromFileJSON(Data.class, file,
                                                   Form.COMPLETED.getNum());
    }
    
    @Override
    public void initializeForm(Calendar date, int mode, int wk) {
        // TODO implement this (requires rewriting auto-fill based on last
        // week's save file
        throw new RuntimeException("not yet implemented");
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

}
