package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;

public class CompletedPersistanceManager {
    
    /**
     * Current Completed Houses Save File
     */
    public static final File CURRENT_COMPLETED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\current\\CurrentCompletedHouseData.txt");


    
    public static boolean saveToFile(Data data) {

        try {
            
            FileWriter fw = new FileWriter(CURRENT_COMPLETED_DATA);
            BufferedWriter bw = new BufferedWriter(fw);
            
            // input house data
            DayData day;
            HouseData house;
            
            for (int d=0; d<data.dayData.length; d++) {
                
                day = data.dayData[d];
                
                // write day number starting at 0
                bw.write("Day " + d);
                bw.newLine();
                
                // write date and day
                Calendar date = day.getHeaderData().getDate();
                String dateString = (Integer.parseInt(String.valueOf(date.get(Calendar.MONTH))) + 1) + "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR);
                bw.write(dateString);
                bw.newLine();
                String weekDay;
                SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
                weekDay = dayFormat.format(date.getTime());
                bw.write(weekDay);
                bw.newLine();
                
                // write selected week
                bw.write(String.valueOf(day.getHeaderData().getWeekSelected()));
                bw.newLine();
                
                // for each house
                for (int h = 0; h < day.houseData.length; h++) {
                    
                    house = day.houseData[h];
                    
                    // write house number starting at 0
                    bw.write("House " + h);
                    bw.newLine();
                    
                    // write name
                    bw.write(house.getHouseName());
                    bw.newLine();
                    
                    // write house pay
                    bw.write(String.valueOf(house.getHousePay()));
                    bw.newLine();
                    
                    // write begin time
                    bw.write(house.getTimeBegin());
                    bw.newLine();
                    
                    // write end time
                    bw.write(house.getTimeEnd());
                    bw.newLine();
                    
                    // write selected workers
                    String s = "";
                    for (int i=0; i<house.getSelectedWorkers().length; i++) {
                        s = new String(s + house.getSelectedWorkers()[i] + " ");
                    }
                    bw.write(s);
                    bw.newLine();
                    
                    // write exceptions
                    ExceptionData ex = house.getExceptionData();
                    for (int i = 0; i < ex.worker_name.length; i++) {
                        
                        // write worker name
                        bw.write(ex.worker_name[i]);
                        bw.newLine();
                        
                        // write begin time
                        bw.write(ex.time_begin[i]);
                        bw.newLine();
                        
                        // write end time
                        bw.write(ex.time_end[i]);
                        bw.newLine();
                    }
                
                } // end houses
                
            }  // end days
            
            bw.close();
            return true;
            
        }catch(Exception exception){
            exception.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Error: Data failed to save properly.");
            return false;
        }

    }

    
    public boolean loadFromFile() {
        // TODO Auto-generated method stub
        return false;
    }

}
