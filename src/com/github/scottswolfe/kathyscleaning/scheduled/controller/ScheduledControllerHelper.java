package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.DefaultWorkerPanel;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HouseData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.WorkerSchedule;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class ScheduledControllerHelper 
                        implements ControllerHelper<TabbedPane, NW_Data>{

    @Override
    public NW_Data readViewIntoModel(TabbedPane view) {
        
        NW_Data data = new NW_Data();
        NW_DayData[] dayData = new NW_DayData[5];   // 5 days in week
        TabbedPane tp = view;
        NW_DayPanel dp;
        
        // for each day of the week
        for (int d = 0; d < 5; d++) {
            
            dp = tp.nw_day_panel[d];
            
            String[] dworkers = dp.getWorkers();
            String[] exceptnames = dp.getExceptionNames();
            WorkerSchedule[] ws;
            
            if (dworkers != null) {
            
            // gathering exception data
            
            // setting the worker names for the day
            ws = new WorkerSchedule[dworkers.length];       // one schedule for each worker that day
            for (int i=0; i<dworkers.length; i++) {
                ws[i] = new WorkerSchedule();
                ws[i].setName(dworkers[i]);
            }
            
            // for each worker for that day
            for (int w=0; w<ws.length; w++) {
                
                // a) Add houses they will work at
                
                // for each house of the day
                for(int j=0; j<dp.house_panel.length; j++) {
                    
                    NW_HousePanel house = dp.house_panel[j];
                    String[] hworkers = house.getSelectedWorkers();
                    
                    // for each worker at the house
                    for (int h=0; h<hworkers.length; h++) {
                                                
                        // if current worker is selected at the house, add the house to the worker's schedule
                        if (ws[w].getName() != null && hworkers[h] != null) {
                            if (ws[w].getName().equals(hworkers[h])) {
                                ws[w].addHouse( house.house_name_txt.getText() );
                                break;
                            }
                        }
                    }
                    
                }
                
                // b) If they have an exception, add exception location, time, note
                //    If no exception, add general location, time, note
                
                // for each exception, check if worker matches exception
                Boolean isException = false;
                int index = 0;
                if(exceptnames != null){
                    for (int i=0; i<exceptnames.length; i++) {
                        if ( ws[w].getName().equals(exceptnames[i]) ) {
                            isException = true;
                            index = i;
                            break;
                        }
                    }
                }
                
                // adding general notes and covenant notes
                
                // if current worker has a note, add the note to the worker's schedule
                if (ws[w].getName() != null && dp.getCovenant_note_data() != null &&
                        dp.getCovenant_note_data().name_box_data != null &&
                        dp.getCovenant_note_data().note_field_data != null) {
                    
                    // iterating through names in note data
                    for (int j=0; j<dp.getCovenant_note_data().name_box_data.length; j++) {
                        
                        
                        if ( ws[w].getName().equals(dp.getCovenant_note_data().name_box_data[j]) ) {
                            
                            // adding covenant note
                            ws[w].addNote( dp.getCovenant_note_data().note_field_data[j]);
                            
                        }
                    }
                }
                /*
                if (ws[w].getName() != null && dp.getDay_note_data() != null &&
                        dp.getDay_note_data().name_box_data != null &&
                        dp.getDay_note_data().note_field_data != null) {
                    
                    // iterating through each name in note data
                    for (int j=0; j<dp.getDay_note_data().name_box_data.length; j++) {
                        
                        if ( ws[w].getName().equals(dp.getDay_note_data().name_box_data[j]) ) {
                            
                            // adding day note
                            ws[w].addNote( dp.getDay_note_data().note_field_data[j]);
                            
                        }
                    }
                }
                */
                
                // if worker matches an exception
                if ( isException ) {
                    ws[w].setTime( dp.bed[index].getTime() );
                    ws[w].setMeetLocation( dp.bed[index].getMeetLocation() );
                    ws[w].ex_note = dp.bed[index].getNote();
                }
                // if worker does not have an exception
                else {
                    // if worker is at first house
                    if (ws[w].getHouse() != null && ws[w].getHouse()[0].equals(dp.house_panel[0].house_name_txt.getText())) {
                        ws[w].setTime( dp.getMeetTime() );
                        ws[w].setMeetLocation( dp.getMeetLocation() );
                    }
                    // if worker joins midday
                    else {
                        
                    }
                }
                
                // adding Covenant
                // if current worker is selected on Covenant panel
                for (int k=0; k<dp.cov_panel.dwp.rows; k++) {
                    for (int l=0; l<dp.cov_panel.dwp.columns; l++) {
                        if ( ws[w].getName().equals(dp.cov_panel.dwp.workerCheckBoxes[k][l].getText()) &&
                             dp.cov_panel.dwp.workerCheckBoxes[k][l].isSelected() ) {
                            
                            ws[w].working_covenant = true;
                        }
                    }
                }
                
                String s = new String();
                
                // add the time
                if( ws[w].time != null && ws[w].time.length() > 2 ) {
                    s = new String(s + ws[w].time );
                }
                
                // add the meeting location 
                // if has a meeting location
                if( ws[w].getMeetLocation() != null &&
                    ws[w].getMeetLocation().length() > 0 &&
                    ws[w].getHouse() != null &&
                    !ws[w].getMeetLocation().equals( ws[w].getHouse()[0] )) {
                    
                    // if has an exception note
                    if (ws[w].ex_note != null && ws[w].ex_note.length() > 0) {
                        s = new String( s + " " + ws[w].getMeetLocation() + " (" + ws[w].ex_note + ")..." );
                        ws[w].ex_note_written = true;
                    }
                    // if no exception note
                    else {
                        s = new String( s + " " + ws[w].getMeetLocation() + "..." );
                    }
                    
                }
                // if no meeting location
                else {
                    // if time has already been written
                    if (ws[w].time != null && ws[w].time.length() > 2) {
                        s = new String( s + "...");
                    }
                    // if time has not been written
                    else {
                        // do nothing
                    }
                }
                
                // add the houses
                if (ws[w].getHouse() != null) {
                    for (int i=0; i<ws[w].house.length; i++) {
                        // if last house, don't add a comma
                        if (i >= ws[w].house.length - 1) {
                            
                            s = new String( s + ws[w].getHouse()[i] );
                            
                            // ex_note exists that has not yet been written to the string
                            if ( ws[w].ex_note_written == false && ws[w].ex_note != null &&
                                 ws[w].ex_note.length() > 0 ){
                                
                                s = new String( s + " (" + ws[w].ex_note + ")");
                                ws[w].ex_note_written = true;
                            }
                        }
                        // if first house and ex_note exists and it has not already been written 
                        else if ( i==0 && ws[w].ex_note_written == false &&
                                ws[w].ex_note != null && ws[w].ex_note.length() > 0 ) {
                            
                            s = new String( s + ws[w].getHouse()[i] + " (" + ws[w].ex_note + "), ");
                            ws[w].ex_note_written = true;
                            
                        }
                        // if every other house, add a comma
                        else {
                            s = new String(s + ws[w].getHouse()[i] + ", ");
                        }
                    }
                }
                
                // add Covenant Info
                if (ws[w].working_covenant == true) {
                    // if employee worked at houses as well, add a comma
                    if(ws[w].getHouse() != null && ws[w].getHouse()[0].length() > 0) {
                        s = new String( s + ", Covenant" );
                    }
                    // if employee only working at Covenant, no comma
                    else {
                        s = new String( s + "Covenant" );
                    }
                }
                
                // add any notes
                if (ws[w].getNote() != null) {
                    for (int i=0; i<ws[w].note.length; i++) {
                        // if only note and either houses or covenant exist, add a preceding semicolon
                        if (ws[w].note.length <= 1 && ( ws[w].getHouse() != null || ws[w].working_covenant == true ) ) {
                            s = new String( s + "; " + ws[w].getNote()[i] );
                        }
                        // if only note and houses and covenant do not exist, no preceding semicolon
                        else if (ws[w].note.length <= 1 && ( ws[w].getHouse() == null && ws[w].working_covenant == false ) ) {
                            s = new String( s + ws[w].getNote()[i] );
                        }
                        // if last note, don't add a semicolon
                        else if (i >= ws[w].note.length - 1) {
                            s = new String( s + ws[w].getNote()[i] );
                        }
                        // if first note and either houses or covenant exist, add a preceding semicolon and succeeding semicolon
                        else if (i<=0 && ( ws[w].getHouse() != null || ws[w].working_covenant == true ) ) {
                            s = new String( s + "; " + ws[w].getNote()[i] + "; ");
                        }
                        // if first note and houses and covenant do not exist, no preceding semicolon and succeeding semicolon
                        else if (i<=0 && ( ws[w].getHouse() == null  && ws[w].working_covenant == false ) ) {
                            s = new String( s + ws[w].getNote()[i] + "; ");
                        }
                        // for every other note, add a semicolon
                        else {
                            // was this: s = new String(s + ws[w].getHouse()[i] + "; ");
                            s = new String(s + ws[w].getNote()[i] + "; ");
                        }
                    }
                }
                
                if (s.equals("...")){
                    s = "";
                }
                
                ws[w].setSchedule( s );
            }
            } // end if dworkers != null
            // else if dworkers == null
            else {
                ws = new WorkerSchedule[0];
            }
            
            dayData[d] = new NW_DayData();
            dayData[d].setWorkerSchedule(ws);
                            
        }  // end day iteration
        data.setDayData( dayData );
        return data;
    }

    @Override
    public void writeModelToView(NW_Data model, TabbedPane view) {
            
        TabbedPane tp = view;
        NW_DayData dayData;

        // iterate through each day
        for (int d = 0; d < 5; d++) {
            
            dayData = model.dayData[d];
            NW_DayPanel dp = tp.nw_day_panel[d];
            
            dp.meet_location_box.setSelectedItem(dayData.meet_location);                
            dp.meet_time_field.setText(dayData.meet_time);
            dp.setBeginExceptionData(dayData.bed);
            
            int rows = tp.nw_day_panel[d].cov_panel.dwp.rows;
            int columns = tp.nw_day_panel[d].cov_panel.dwp.columns;
            for(int l = 0; l < rows; l++){
                for(int m = 0; m < columns; m++){
                    tp.nw_day_panel[d].cov_panel.dwp.workerCheckBoxes[l][m].setSelected(false);
                }
            }
            for (int i = 0; i < dayData.cov_worker.length; i++) {
                String worker = dayData.cov_worker[i];
                
                for(int l = 0; l < rows; l++){
                    for(int m = 0; m < columns; m++){
                        if (worker.equals(tp.nw_day_panel[d].cov_panel.dwp.workerCheckBoxes[l][m].getText())){
                            tp.nw_day_panel[d].cov_panel.dwp.workerCheckBoxes[l][m].setSelected(true);
                            break;
                        }
                    }
                }   
            }
            tp.nw_day_panel[d].covenant_note_data = dayData.covNoteData;
            
            // iterate through houses
            NW_HouseData[] houses = dayData.houseData;
            for (int h = 0; h < houses.length; h++) {

                NW_HouseData houseData = houses[h];

                tp.nw_day_panel[d].house_panel[h].house_name_txt.setText(houseData.getHouseName());
                                    
                for(int l = 0; l < DefaultWorkerPanel.NORM_ROWS; l++){
                    for(int m = 0; m < DefaultWorkerPanel.NORM_COLUMNS; m++){
                        tp.nw_day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(false);
                    }
                }
                String[] workers = houseData.getSelectedWorkers();
                for (int i = 0; i < workers.length; i++) {
                    String worker = workers[i];
                    for(int l = 0; l < DefaultWorkerPanel.NORM_ROWS; l++){
                        for(int m = 0; m < DefaultWorkerPanel.NORM_COLUMNS; m++){
                            if (worker.equals(tp.nw_day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].getText())) {
                                tp.nw_day_panel[d].house_panel[h].worker_panel.workerCheckBoxes[l][m].setSelected(true);
                                break;
                            }
                        }
                    }
                }
                
                // 4. making sure there is a correct number of house panels available to fill
                
                // if there are more empty house panels and there are more houses to fill in
                if (h < (houses.length - 1) && (h + 1) < dp.house_panel.length ) {
                    // do nothing
                }
                // if there are no more empty house panels and there are more houses to fill in
                else if (h + 1 >= tp.nw_day_panel[d].house_panel.length && h < (houses.length - 1) ){
                    ActionEvent event = new ActionEvent(tp.nw_day_panel[d], 0, "test");
                    ActionListener[] al = tp.nw_day_panel[d].house_panel[h].add_house.getActionListeners();
                    al[0].actionPerformed(event);
                }
                // if there are more empty house panels and there are no more houses to fill in
                else if (( h+ 1) < tp.nw_day_panel[d].house_panel.length && h >= (houses.length - 1)) {
                    int numrepeat = tp.nw_day_panel[d].house_panel.length - h - 1;
                    for (int k = h; k < numrepeat + h; k++) {
                        ActionEvent event = new ActionEvent(tp.nw_day_panel[d], 0, "test");
                        ActionListener[] al = tp.nw_day_panel[d].house_panel[h + 1].delete_house.getActionListeners();
                        al[0].actionPerformed(event);
                    }
                }
                // no empty house panels and there are no more houses to fill in
                else if (h + 1 >= tp.nw_day_panel[d].house_panel.length && h >= (houses.length - 1)) {
                    // do nothing
                }    
            }
        }
        /*
        TabChangeListener.nw_resize(tp, frame, 4, 0); // resizing from final tab (friday) to first tab (monday)
        
        for (int j = 0; j < 5; j++) {
            tp.nw_day_panel[j].header_panel.weekSelected = wk;
        }
        
        if (wk == Settings.WEEK_A) {
            for (int j=0; j<5; j++) {
                tp.nw_day_panel[j].header_panel.week_A.setSelected(true);
            }
        }
        else {
            for (int j=0; j<5; j++) {
                tp.nw_day_panel[j].header_panel.week_B.setSelected(true);
            }
        }
        
        tp.nw_day_panel[tp.getSelectedIndex()].header_panel.weekSelected = wk;
        */
    }

    @Override
    public void saveToFile(NW_Data model, File file) {
        JsonMethods.saveToFileJSON(model, NW_Data.class, file, Form.SCHEDULED.getNum());
    }

    @Override
    public NW_Data loadFromFile(File file) {
        return (NW_Data) JsonMethods.loadFromFileJSON(NW_Data.class, file, Form.SCHEDULED.getNum());
    }

    @Override
    public void initializeForm(GeneralController<TabbedPane, NW_Data> controller) {
        
        WorkerList workers = new WorkerList();
        try {
            workers = new WorkerList(WorkerList.HOUSE_WORKERS);
        } catch (Exception e1) {
            System.out.println("failed to read house worker save file");
            e1.printStackTrace();
        }
        
        TabbedPane tp = new TabbedPane();
        tp.setFont(tp.getFont().deriveFont(Settings.TAB_FONT_SIZE));
                    
        // creating array of dates
        Calendar[] day = new Calendar[5];
        Calendar date = (Calendar) Settings.completedStartDay.clone();
        date.add(Calendar.DATE, 7);
        Calendar temp_date = (Calendar) date.clone();
        for(int i = 0; i < day.length; i++) {
            
            day[i] = Calendar.getInstance();
            day[i].set(temp_date.get(Calendar.YEAR), temp_date.get(Calendar.MONTH), temp_date.get(Calendar.DATE));
            temp_date.add(Calendar.DATE, 1);
            
        }
        
        controller.setView(tp);
                        
        MainFrame<TabbedPane, NW_Data> frame = new MainFrame<>(controller);
        
        NW_DayPanel[] day_panel = new NW_DayPanel[5];
        for(int i = 0; i < 5; i++){
            day_panel[i] = new NW_DayPanel(
                    controller, tp, workers,
                    day[i], frame, 0, 0); // TODO remove 0, 0
        }
        tp.nw_day_panel = day_panel;
        
        tp.addTab("Monday", day_panel[0]);
        tp.addTab("Tuesday", day_panel[1]);
        tp.addTab("Wednesday", day_panel[2]);
        tp.addTab("Thursday", day_panel[3]);
        tp.addTab("Friday", day_panel[4]);
        
        tp.changePreviousTab(0);
        tp.addChangeListener(new NW_TabChangeListener(tp, frame));
        
        frame.setBackground(Settings.BACKGROUND_COLOR);
        frame.add(tp);
        frame.pack();
        frame.setLocationRelativeTo(null);        
        frame.setVisible(true);
    }
    
    @Override
    public void updateDate(TabbedPane tp) {
        Settings.completedStartDay.add(Calendar.DATE, -7);
        Calendar[] days = new Calendar[5];
        Calendar temp_date = (Calendar) Settings.completedStartDay.clone();
        temp_date.add(Calendar.DATE, 7);
        for(int i = 0; i < days.length; i++) {
            days[i] = Calendar.getInstance();
            days[i].set(temp_date.get(Calendar.YEAR), temp_date.get(Calendar.MONTH), temp_date.get(Calendar.DATE));
            temp_date.add(Calendar.DATE, 1);
        }

        for (int i = 0; i < tp.nw_day_panel.length; i++) {
            tp.nw_day_panel[i].header_panel.date = days[i];
            
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            weekDay = dayFormat.format(days[i].getTime());
            
            tp.nw_day_panel[i].header_panel.day_label.setText(weekDay);
            tp.nw_day_panel[i].header_panel.date_label.setText((Integer.parseInt(String.valueOf(days[i].get(Calendar.MONTH)))+1) + "/" + days[i].get(Calendar.DATE) + "/" + days[i].get(Calendar.YEAR));
        }
    }

}
