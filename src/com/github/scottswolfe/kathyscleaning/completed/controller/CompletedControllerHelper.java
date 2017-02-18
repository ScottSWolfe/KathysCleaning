package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.completed.view.DayPanel;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_ExceptionPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_NotePanel;

import com.google.gson.Gson;

public class CompletedControllerHelper {
    
    /**
     * Current Completed Houses Save File
     */
    public static final File CURRENT_COMPLETED_DATA =
            new File(System.getProperty("user.dir") +
                    "\\save\\current\\CurrentCompletedHouseData.txt");


    
    /**
     * Reads User's input from the Completed GUI into a Data object 
     * 
     * @param data the Data object to read the data into
     * @param tp the TabbedPane containing the user input
     * @return the updated Data object
     */
    public static Data readUserInput(TabbedPane tp) {
        Data data = new Data();
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

    public static void saveToFileJSON(Data data) {
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
                
                // write date
                Calendar date = day.getHeaderData().getDate();
                String dateString = (Integer.parseInt(String.valueOf(date.get(Calendar.MONTH))) + 1) + "/" + date.get(Calendar.DATE) + "/" + date.get(Calendar.YEAR);
                bw.write(dateString);
                bw.newLine();
                
                // write day
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

    
    // TODO break into two different methods (we already have one above)
    // pretty much turn this into setting UI from Data object
    public static void loadFromFileAndSetUI(TabbedPane tp) {

        Scanner input;
        Scanner counter;
        
        try {
                        
            // iterate through each day
            for (int d=0; d<5; d++) {
                
                DayPanel dp = tp.day_panel[d];
                
                input = new Scanner(CURRENT_COMPLETED_DATA);
                counter = new Scanner(CURRENT_COMPLETED_DATA);
                
                String s = input.nextLine();
                counter.nextLine();
                
                // find current day
                while ( !s.equals("Day " + d) && input.hasNextLine() ) {
                    s = input.nextLine();
                    counter.nextLine();
                }
                              
                // find out how many houses for current day
                String t = counter.nextLine();
                int num_houses = 0;
                while ( !t.equals("Day " + (d+1) ) && counter.hasNextLine() ) {
                    if ( t.equals("House " + num_houses) ) {
                        num_houses++;
                    }
                    t = counter.nextLine();
                }
                counter.close();
    
                int wk = readWeek();
                // reading begin data and covenant data
                
                // skip meet_location_box and meet_time_field save items
                input.nextLine();
                input.nextLine();

                //BeginExceptionData[] bed = new BeginExceptionData[NW_ExceptionPanel.NUM_EXCEPTIONS];
                for (int i=0; i<NW_ExceptionPanel.NUM_EXCEPTIONS; i++) {
                    
                    //bed[i] = new BeginExceptionData();
                    
                    input.nextLine();  //bed[i].setName( input.nextLine() );
                    input.nextLine();  //bed[i].setMeetLocation( input.nextLine() );
                    input.nextLine();  //bed[i].setTime( input.nextLine() );
                    input.nextLine();  //bed[i].setNote( input.nextLine() );
                    
                }
                
                // late edit trying to make 1.1.4 work
                // possibly erroneous
                input.nextLine(); //consuming ??covenant workers?? line
                
                
                //dp.setBeginExceptionData(bed);
                
                // reading covenant workers
                //String line = input.nextLine();
                //Scanner parser1 = new Scanner(line);
                //parser1.useDelimiter(" ");
                    
                /*      
                // unselecting any selected workers
                int rows = tp.day_panel[d].cov_panel.dwp.rows;
                int columns = tp.nw_day_panel[d].cov_panel.dwp.columns;
                for(int l=0; l<rows; l++){
                    for(int m=0; m<columns; m++){
                        tp.nw_day_panel[d].cov_panel.dwp.worker[l][m].setSelected(false);
                    }
                }
                
                // selecting saved workers
                while (parser1.hasNext() ) {
                    String worker = parser1.next();
                    
                    for(int l=0; l<rows; l++){
                        for(int m=0; m<columns; m++){
                            if (worker.equals( tp.nw_day_panel[d].cov_panel.dwp.worker[l][m].getText() ) ){
                                tp.nw_day_panel[d].cov_panel.dwp.worker[l][m].setSelected(true);
                                break;
                            }
                        }
                    }   
                }
                parser1.close();
                */
                
                // notes
                
                //tp.nw_day_panel[d].covenant_note_data = new NoteData( NW_ExceptionPanel.NUM_EXCEPTIONS );
                //tp.nw_day_panel[d].covenant_note_data.name_box_data = new String[NW_NotePanel.ROWS];
                //tp.nw_day_panel[d].covenant_note_data.note_field_data = new String[NW_NotePanel.ROWS];

                for (int i=0; i<NW_NotePanel.ROWS; i++) {
                    
                    input.nextLine(); //tp.nw_day_panel[d].covenant_note_data.name_box_data[i] = input.nextLine();
                    input.nextLine(); //tp.nw_day_panel[d].covenant_note_data.note_field_data[i] = input.nextLine();
                    
                }
                
                            
                
                // iterate through houses
                for ( int h=0; h<num_houses; h++ ) {
                    
                    input.nextLine(); // burn "House i"
                    
                    tp.day_panel[d].house_panel[h].house_name_txt.setText( input.nextLine() );
                                        
                    String line = input.nextLine();
                    Scanner parser = new Scanner(line);
                    parser.useDelimiter(" ");
                                
                        // unselecting any selected workers
                        for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
                            for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
                                tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(false);
                            }
                        }
                        
                        // selecting saved workers
                        while (parser.hasNext() ) {
                            String worker = parser.next();
                            
                            for(int l=0; l<DefaultWorkerPanel.NORM_ROWS; l++){
                                for(int m=0; m<DefaultWorkerPanel.NORM_COLUMNS; m++){
                                    //tp.day_panel[0].house_panel[i].worker_panel.worker[l][m].setSelected(false);
                                    if (worker.equals( tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].getText() ) ){
                                        tp.day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(true);
                                        break;
                                    }
                                }
                            }
                            
                        }
                        parser.close();
                        
                        // 4. making sure there is a correct number of house panels available to fill
                        
                        // if there are more empty house panels and there are more houses to fill in
                        if ( h < (num_houses - 1) && (h+1) < dp.house_panel.length ) {
                            // do nothing
                        }
                        // if there are no more empty house panels and there are more houses to fill in
                        else if ( h+1 >= tp.day_panel[d].house_panel.length && h < (num_houses - 1) ){
                            ActionEvent event = new ActionEvent(tp.day_panel[d],0,"test");
                            ActionListener[] al = tp.day_panel[d].house_panel[h].add_house.getActionListeners();
                            al[0].actionPerformed( event );
                        }
                        // if there are more empty house panels and there are no more houses to fill in
                        else if ( (h+1) < tp.day_panel[d].house_panel.length && h >= (num_houses - 1 ) ) {
                            int numrepeat = tp.day_panel[d].house_panel.length-h-1;
                            for (int k=h; k<numrepeat+h; k++) {
                                ActionEvent event = new ActionEvent(tp.day_panel[d],0,"test");
                                ActionListener[] al = tp.day_panel[d].house_panel[h+1].delete_house.getActionListeners();
                                al[0].actionPerformed( event );
                            }
                        }
                        // no empty house panels and there are no more houses to fill in
                        else if (h+1 >= tp.day_panel[d].house_panel.length && h >= (num_houses - 1 )) {
                            // do nothing
                        }      
                                
                }
                
                if (wk == Settings.WEEK_A ) {
                    tp.day_panel[d].header_panel.week_A.setSelected(true);

                }
                else if ( wk == Settings.WEEK_B ){
                    tp.day_panel[d].header_panel.week_B.setSelected(true);
                }
                else {
                    tp.day_panel[d].header_panel.neither.setSelected(true);
                }
                
                input.close();
            }         
            
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Could not read saved schedule file correctly",
                    null, JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }

    }
    
    /**
     * Reads the week from the input file
     * 
     * @return an int represent the week seleection
     */
    private static int readWeek() {
        // TODO implement
        return -1;
    }

}
