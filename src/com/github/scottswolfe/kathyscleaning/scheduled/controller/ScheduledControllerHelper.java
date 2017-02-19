package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HeaderPanel;
import com.github.scottswolfe.kathyscleaning.completed.view.HousePanel;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HeaderData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HouseData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.WorkerSchedule;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;
import com.google.gson.Gson;

public class ScheduledControllerHelper {

/* CLASS VARIABLES ========================================================== */
   
    /**
     * Current Completed Houses Save File
     */
    public static final File CURRENT_COMPLETED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\current\\CurrentScheduledHouseData.txt");

    

/* PUBLIC METHODS =========================================================== */
    
    /**
     * Reads User's input from the Completed GUI into a NW_Data object 
     * 
     * @param data the NW_Data object to read the data into
     * @param tp the TabbedPane containing the user input
     * @return the new NW_Data object
     */
    public static NW_Data readUserInput(TabbedPane tp) {
        // TODO implement method
        return null;
    }

    /**
     * Saves the given data object to current file in JSON format
     * 
     * @param data the data to be saved
     */
    public static void saveToFileJSON(NW_Data data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        try {
            FileWriter fw = new FileWriter(CURRENT_COMPLETED_DATA);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Returns a data object retried from the current JSON file
     * 
     * @param data the data to be saved
     */
    public static Data loadFromFileJSON() {
        Gson gson = new Gson();
        Data data = null;
        try {
            Scanner input = new Scanner(CURRENT_COMPLETED_DATA);
            String json = input.nextLine();
            data = gson.fromJson(json, Data.class);
            input.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return data;
    }
    
    /**
     * Writes the given data into the given view
     * 
     * @param data the data to be written into the view
     * @param tp the view in which to write the data
     */
    public static void writeDataToView(Data data, TabbedPane tp) {

        // TODO an issue: if different week is selected than current,
        // the week selection listener will auto fill stuff...
        
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
                house_panel.time_begin_txt.setText(house_data.getTimeEnd());
                
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
            
            
            // TODO: temporary for now until listener situation is corrected
            weekSelected = header_data.getWeekSelected();
            /*
            if (weekSelected == Settings.WEEK_A ) {
                header_panel.week_A.setSelected(true);
            }
            else if (weekSelected == Settings.WEEK_B ){
                header_panel.week_B.setSelected(true);
            }
            else {
            */
                header_panel.neither.setSelected(true);
            //}
            
        }
    }



}
