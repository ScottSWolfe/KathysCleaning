package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingUtilities;

import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.model.BeginExceptionEntry;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HeaderData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_HouseData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.WorkerSchedule;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_DayPanel;
import com.github.scottswolfe.kathyscleaning.scheduled.view.NW_HousePanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class ScheduledControllerHelper 
                        implements ControllerHelper<TabbedPane, NW_Data>{

    TabbedPane tp;
    
    @Override
    public NW_Data readViewIntoModel(TabbedPane view) {
        
        NW_Data data = new NW_Data();
        TabbedPane tp = view;
        
        NW_DayData[] dayData = new NW_DayData[5];
        DayData[] completedDayData = new DayData[5];
        
        // for each day
        for (int d = 0; d < dayData.length; d++) {
            NW_DayPanel dp = tp.nw_day_panel[d];
            
            dayData[d] = new NW_DayData();
            completedDayData[d] = new DayData();

            NW_HeaderData header = new NW_HeaderData();
            header.setDWD(dp.header_panel.dwp.getWorkers());
            dayData[d].setHeader(header);
            
            dayData[d].beginExceptionList = dp.getBeginExceptionList();
            dayData[d].cov_worker = dp.cov_panel.dwp.getWorkers();
            dayData[d].meet_location = dp.getMeetLocation();
            dayData[d].meet_time = dp.getMeetTime();
            dayData[d].covNoteData = dp.getNoteData();
            
            NW_HouseData[] houseData = new NW_HouseData[tp.nw_day_panel[d].house_panel.length];
            HouseData[] completedHouseData = new HouseData[tp.nw_day_panel[d].house_panel.length];
            
            // for each house panel in the day
            for (int h = 0; h < completedHouseData.length; h++) {
                houseData[h] = new NW_HouseData();
                houseData[h].setHouseName(tp.nw_day_panel[d].house_panel[h].getHouseName());
                houseData[h].setSelectedWorkers(tp.nw_day_panel[d].house_panel[h].getSelectedWorkers());
                houseData[h].setWorkerList(tp.nw_day_panel[d].house_panel[h].worker_panel.getWorkers());
                
                completedHouseData[h] = new HouseData();
                completedHouseData[h].setHouseName(tp.nw_day_panel[d].house_panel[h].getHouseName()); 
                completedHouseData[h].setSelectedWorkers(tp.nw_day_panel[d].house_panel[h].worker_panel.getSelected());
                completedHouseData[h].setWorkerList(tp.nw_day_panel[d].house_panel[h].worker_panel.getWorkers());
            }
            dayData[d].setHouseData(houseData);
            completedDayData[d].setHouseData(completedHouseData);
            
            List<WorkerSchedule> ws = getWorkerSchedules(tp.nw_day_panel[d]);
            dayData[d].setWorkerSchedule(ws);
        }
        
        data.setDayData(dayData);
        data.completedDayData = completedDayData;
        
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
            DayData completedDayData = model.completedDayData[d];
            
            dp.header_panel.dwp.setWorkers(dayData.getHeaderData().getDWD());
            dp.meet_location_box.setSelectedItem(dayData.meet_location);
            dp.meet_time_field.setText(dayData.meet_time);
            dp.setBeginExceptionList(dayData.beginExceptionList);
            tp.nw_day_panel[d].cov_panel.dwp.setWorkers(dayData.cov_worker);            
            tp.nw_day_panel[d].setNoteData(dayData.covNoteData);
            
            // iterate through houses
            NW_HouseData[] houses = dayData.houseData;
            HouseData[] completedHouses = completedDayData.houseData;
            for (int h = 0; h < houses.length; h++) {

                HouseData houseData = completedHouses[h];

                tp.nw_day_panel[d].house_panel[h].setHouseName(houseData.getHouseName());                
                tp.nw_day_panel[d].house_panel[h].worker_panel.setWorkers(houseData.getWorkerList());
                
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
        
        WorkerList workers = new WorkerList(WorkerList.HOUSE_WORKERS);
        
        TabbedPane tp = new TabbedPane();
        tp.setFont(tp.getFont().deriveFont(Settings.TAB_FONT_SIZE));
                    
        // creating array of dates
        Calendar[] day = new Calendar[5];
        Calendar date;
        if (SessionModel.getScheduledStartDay() == null) {
            date = SessionModel.getCompletedStartDay();
            date.add(Calendar.DATE, 7);
            SessionModel.setScheduledStartDay(date);
        } else {
            date = SessionModel.getScheduledStartDay();
        }
        for(int i = 0; i < day.length; i++) {
            day[i] = Calendar.getInstance();
            day[i].set(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DATE));
            date.add(Calendar.DATE, 1);
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
        ChooseWeekPanel.initializePanel(this, true);
    }
    
    @Override
    public void updateDateHelper() {
        Calendar[] days = new Calendar[5];
        Calendar temp_date = (Calendar) SessionModel.getScheduledStartDay();
        for(int i = 0; i < days.length; i++) {
            days[i] = Calendar.getInstance();
            days[i].set(temp_date.get(Calendar.YEAR), temp_date.get(Calendar.MONTH), temp_date.get(Calendar.DATE));
            
            tp.nw_day_panel[i].header_panel.date = days[i];
            String weekDay;
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
            weekDay = dayFormat.format(days[i].getTime());
            tp.nw_day_panel[i].header_panel.day_label.setText(weekDay);
            tp.nw_day_panel[i].header_panel.date_label.setText((Integer.parseInt(String.valueOf(days[i].get(Calendar.MONTH)))+1) + "/" + days[i].get(Calendar.DATE) + "/" + days[i].get(Calendar.YEAR));

            temp_date.add(Calendar.DATE, 1);
        }
    }
    
    @Override
    public void eliminateWindow(TabbedPane view) {
        @SuppressWarnings("rawtypes")
        MainFrame frame = (MainFrame) SwingUtilities.getWindowAncestor(view);
        frame.eliminate();
    }

    private List<WorkerSchedule> getWorkerSchedules(NW_DayPanel dp) {
        
        List<WorkerSchedule> scheduleList = new ArrayList<>();
        
        List<String> workers = dp.getUniqueWorkersForDay();        

        // for each worker for the day
        for (String worker : workers) {
            WorkerSchedule schedule = new WorkerSchedule();
            schedule.setName(worker);
            
            // for each house of the day
            for (int j = 0; j < dp.house_panel.length; j++) {
                
                NW_HousePanel house = dp.house_panel[j];
                List<String> hworkers = house.getSelectedWorkers();
                
                if (hworkers.contains(worker)) {
                    schedule.addHouse(house.getHouseName());
                }
            }
                
            // if current worker has a note, add the note to the worker's schedule
            if (dp.getNoteData() != null &&
                dp.getNoteData().name_box_data != null &&
                dp.getNoteData().note_field_data != null) {
                
                // iterating through names in note data
                for (int j = 0; j < dp.getNoteData().name_box_data.length; j++) {
                    
                    if (worker.equals(dp.getNoteData().name_box_data[j])
                            && !dp.getNoteData().equals("")) {
                        schedule.addNote(dp.getNoteData().note_field_data[j]);                        
                    }
                }
            }
            
            // adding Covenant
            // if current worker is selected on Covenant panel
            if (dp.cov_panel.dwp.isSelected(schedule.getName())) {
                schedule.working_covenant = true;
            }

            // adding meeting information
            Boolean hasException = false;
            for (BeginExceptionEntry entry : dp.getBeginExceptionList()) {
                // if worker matches an exception
                if (entry.getName().equals(worker)) {
                    schedule.setTime(entry.getTime());
                    schedule.setMeetLocation(entry.getMeetLocation());
                    schedule.ex_note = entry.getNote();
                    hasException = true;
                    break;
                }
            }
            // if worker does not have an exception
            if (!hasException) {
                // if worker is at first house
                if (schedule.getHouseList().size() > 0 && schedule.getHouseList().get(0).equals(dp.house_panel[0].getHouseName())) {
                    schedule.setTime(dp.getMeetTime());
                    schedule.setMeetLocation(dp.getMeetLocation());
                }
                // if worker joins midday
                else {
                    // do nothing
                }
            }
                
            String s = createScheduleString(schedule);
            schedule.setSchedule(s);
            scheduleList.add(schedule);
        }
        return scheduleList;
    }
                        
    private String createScheduleString(WorkerSchedule schedule) {
        String s = new String();
        
        // add the time
        if( schedule.time != null && schedule.time.length() > 2 ) {
            s += schedule.time;
        }
        
        // add the meeting location 
        // if has a meeting location
        if( schedule.getMeetLocation() != null &&
            schedule.getMeetLocation().length() > 0 &&
            schedule.getHouseList().size() > 0 &&
            !schedule.getMeetLocation().equals( schedule.getHouseList().get(0) )) {
            
            // if has an exception note
            if (schedule.ex_note != null && schedule.ex_note.length() > 0) {
                s += " " + schedule.getMeetLocation() + " (" + schedule.ex_note + ")...";
                schedule.ex_note_written = true;
            }
            // if no exception note
            else {
                s += " " + schedule.getMeetLocation() + "...";
            }
            
        }
        // if no meeting location
        else {
            // if time has already been written
            if (schedule.time != null && schedule.time.length() > 2) {
                s += "...";
            }
            // if time has not been written
            else {
                // do nothing
            }
        }
        
        // add the houses
        int numHouses = schedule.houseList.size();
        for (int i = 0; i < numHouses; i++) {
            
            String house = schedule.houseList.get(i);
            if (house.length() > 0) {
                
                // if last house, don't add a comma
                if (i >= numHouses - 1) {
                    
                    s += house;
                    
                    // ex_note exists that has not yet been written to the string
                    if (schedule.ex_note_written == false && schedule.ex_note != null &&
                         schedule.ex_note.length() > 0) {
                        
                        s += " (" + schedule.ex_note + ")";
                        schedule.ex_note_written = true;
                    }
                }
                // if first house and ex_note exists and it has not already been written 
                else if (i == 0 && schedule.ex_note_written == false &&
                        schedule.ex_note != null && schedule.ex_note.length() > 0) {
                    
                    s += house + " (" + schedule.ex_note + "), ";
                    schedule.ex_note_written = true;
                }
                // if every other house, add a comma
                else {
                    s += house + ", ";
                }
            }
        }
        
        // add Covenant Info
        if (schedule.working_covenant == true) {
            // if employee worked at houses as well, add a comma
            if(schedule.getHouseList().size() > 0) {
                s += ", Covenant" ;
            }
            // if employee only working at Covenant, no comma
            else {
                s += "Covenant";
            }
        }
        
        // add any notes
        int numNotes = schedule.noteList.size();
        for (String note : schedule.noteList) {
            if (note.length() > 0) {
                // if only note and either houses or covenant exist, add a preceding semicolon
                if (numNotes <= 1 && (numHouses > 0 || schedule.working_covenant == true)) {
                    s += "; " + note;
                }
                // if only note and houses and covenant do not exist, no preceding semicolon
                else if (numNotes <= 1 && (numHouses == 0 && schedule.working_covenant == false)) {
                    s += note;
                }
                // if last note, don't add a semicolon
                else if (schedule.noteList.indexOf(note) >= numNotes - 1) {
                    s += note;
                }
                // if first note and either houses or covenant exist, add a preceding semicolon and succeeding semicolon
                else if (schedule.noteList.indexOf(note) <=0 && (numHouses > 0 || schedule.working_covenant == true)) {
                    s += "; " + note + "; ";
                }
                // if first note and houses and covenant do not exist, no preceding semicolon and succeeding semicolon
                else if (schedule.noteList.indexOf(note) <=0 && (numHouses == 0 && schedule.working_covenant == false)) {
                    s += note + "; ";
                }
                // for every other note, add a semicolon
                else {
                    // was this: s = new String(s + ws[w].getHouse()[i] + "; ");
                    s += note + "; ";
                }
            }
        }    
        
        if (s.equals("...")) {
            s = "";
        }
        return s;
    }
    
}
