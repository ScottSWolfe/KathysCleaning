package com.github.scottswolfe.kathyscleaning.completed.controller;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;

public class CompletedController implements Controller {

    TabbedPane tp;
    DayData[] dayData;
    
    
    public CompletedController() {
        
    }
    
    
    
    
    /**
     * Reads the user's input data into the given Data object
     * 
     * @param data
     */
    public Data readUserInput() {
        
        Data data = new Data(); // TODO <-- don't really need this
        DayData[] dayData = new DayData[5]; // 5 days in week
        
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
                houseData[h].setHouseName(tp.day_panel[d].house_panel[h].house_name_txt.getText());                       //read house name
                houseData[h].setHousePay( tp.day_panel[d].house_panel[h].pay_txt.getText() );         //read house pay
                houseData[h].setTimeBegin( tp.day_panel[d].house_panel[h].time_begin_txt.getText() ); //read begin time
                houseData[h].setTimeEnd( tp.day_panel[d].house_panel[h].time_end_txt.getText() ); //read end time
                houseData[h].setSelectedWorkers( tp.day_panel[d].house_panel[h].worker_panel.getSelected() );                                                     //get selected workers
                houseData[h].setExceptionData( tp.day_panel[d].house_panel[h].exception_data.getExceptionData() );                                                    //get exception info
            } // end house panels
            
            dayData[d] = new DayData();
            dayData[d].setHouseData(houseData);
            dayData[d].setHeader(headerData);
            
        }  // end day panels
        
        data.setDayData(dayData);
        data.setDate(tp.day_panel[0].header_panel.date);
        return data;
    }
    
}
