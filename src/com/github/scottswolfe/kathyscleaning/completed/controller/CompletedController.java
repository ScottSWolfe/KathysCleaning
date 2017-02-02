package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.completed.model.DayData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.HeaderData;
import com.github.scottswolfe.kathyscleaning.completed.model.HouseData;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;

public class CompletedController implements Controller {

    TabbedPane tp;
    Data data;
    
    
    public CompletedController() {
        
    }
    
    
    
    
    /**
     * Reads the user's input data into the given Data object
     * 
     * @param data
     */
    public Data readUserInput() {
        
        data = new Data();
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
    
    
    public void writeDataToExcel() {
        File file = Settings.getExcelTemplateFile();
        writeDayData(data, file);
    }
    
    
    private void writeDayData(Data data, File file){
        
        /*
         *  1) Create workbook
         *  2) Set date
         *  3) Input house data
         *  4) Write to file 
         */
        
        try{
            
            // 1) Create workbook
            InputStream input = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);

            // 2) set date
            setDate(data, sheet);
                   
            // 3) input house data
            
            // for each day
            for(int d=0; d<5; d++){
                
                Row name_row = sheet.getRow(d*9 + 1);
                
                // for each house
                for (int h=0; h<data.dayData[d].houseData.length; h++){
                                        
                    // if the house data is NOT empty
                    if ( !data.dayData[d].houseData[h].getHouseName().isEmpty() &&
                            data.dayData[d].houseData[h].getHours() != 0 ) {
                        
                        // setting excel row number to write to
                        int row_num = d*9 + 2 + h;  // this formula gives the correct line in the excel sheet template
                        row = sheet.getRow( row_num );
                        
                        String s1 = TimeMethods.convertFormat( data.dayData[d].houseData[h].getTimeBegin(), TimeMethods.HOUSE_TIME );
                        String s2 = TimeMethods.convertFormat( data.dayData[d].houseData[h].getTimeEnd(), TimeMethods.HOUSE_TIME );
                        
                        row.getCell(0).setCellValue( DateUtil.convertTime(s1) );
                        row.getCell(1).setCellValue( DateUtil.convertTime(s2) );
                        
                        // setting # of hours worked
                        row.getCell(2).setCellValue( data.dayData[d].houseData[h].getHours() );
                        
                        // setting house name
                        row.getCell(3).setCellValue( data.dayData[d].houseData[h].getHouseName() );
                        
                        // setting money paid
                        row.getCell(4).setCellValue( data.dayData[d].houseData[h].getHousePay() );
                                            
                        
                        // writing in zero for all employees who did not work
                        //System.out.println("House " + (h+1));
                        boolean names_remaining = true;
                        boolean name_match;
                        int index = 5;
                        String[] worker = data.dayData[d].getHouseData()[h].getSelectedWorkers();
                        /*
                        for (int i=0; i<worker.length; i++) {
                            System.out.println(worker[i]);
                        }
                        */
                        // while there are still names remaining in the excel sheet
                        while ( names_remaining == true && index < 25 ) {
                            // for the number of selected workers at the house  
                            name_match = false;
                            for ( int j=0; j<worker.length; j++) {
                                
                                // if selected worker matches name on excel sheet
                                //System.out.println(name_row.getCell(index).getStringCellValue());
                                //System.out.println(worker[j]);
                                //System.out.println();
                                if ( name_row.getCell(index).getStringCellValue().equals(worker[j]) ) {
                                    name_match = true;
                                    //System.out.println("Name Match: " + "True");
                                    break;
                                }
                                // if name on excel sheet is kathy
                                else if ( name_row.getCell(index).getStringCellValue().equals("Kathy")) {
                                    names_remaining = false;
                                    name_match = true;
                                    //System.out.println("Name Match: Kathy");
                                    break;
                                }
                                
                            }
                            // if none of the selected workers match the name on the excel sheet
                            if ( name_match == false ) {
                                //System.out.println("Name Match: False");
                                
                                if (row.getCell(index) == null) {
                                    // do nothing
                                }
                                else if (row.getCell(index) != null && row.getCell(index).getCellType() == Cell.CELL_TYPE_FORMULA) {
                                    String s = row.getCell(index).getCellFormula();
                                    s = changeFormula(s,(double)0);
                                    row.getCell(index).setCellFormula( s );
                                }
                                else {
                                    row.getCell(index).setCellValue( 0 );
                                }                               
                            }
                            index++;
                        }
                    
                    }
                    else if (data.dayData[d].houseData[h].getHouseName().isEmpty() &&
                            data.dayData[d].houseData[h].getHours() != 0
                             ||
                             !data.dayData[d].houseData[h].getHouseName().isEmpty() &&
                             data.dayData[d].houseData[h].getHours() == 0 ) {
                    }
                    else {
                        // do nothing
                    }
                    
                    // if there is exception data, add data to excel sheet
                    if (!data.dayData[d].houseData[h].getHouseName().isEmpty()) { // only if the house has been named
                    
                    ExceptionData exd = data.dayData[d].houseData[h].getExceptionData();
                    if ( exd.edited == true ) {
                        
                        // iterate through the names in the exception data
                        for (int m=0; m<exd.worker_name.length; m++) {
                            
                            // checking that worker name and times are not null or empty
                            if (exd.worker_name[m] != null && !exd.worker_name[m].isEmpty() &&
                                exd.time_begin[m] != null && !exd.time_begin[m].isEmpty() &&
                                exd.time_end[m] != null && !exd.time_end[m].isEmpty()) {
                                
                                
                                // find name row; trace down name row looking for match with worker_name[m]
                                //      if no match (find kathy) send error report and move on
                                boolean name_found = false;
                                //Cell name_cell = row.getCell(5);
                                int cell_number = 5;
                                while (name_found == false) {
                                    
                                    // if cell name matches current worker name
                                    if ( name_row.getCell(cell_number).getStringCellValue().equals(exd.worker_name[m]) ) {
                                        
                                        // calculate hours worked
                                        double hours = TimeMethods.getHours(exd.time_begin[m], exd.time_end[m]);
                                        
                                        // insert data into excel doc
                                        Row house_row = sheet.getRow( d*9 + 2 + h );

                                            String s = house_row.getCell(cell_number).getCellFormula(); // issue with numeric cells??
                                            s = changeFormula(s,hours);
                                            house_row.getCell(cell_number).setCellFormula(s);

                                        break;
                                        
                                    }
                                    // if cell name is Kathy
                                    else if ( name_row.getCell(cell_number).getStringCellValue().equals("Kathy") ) {
                                        String message = "Error: Employee " + exd.worker_name[m] + 
                                                         " from the exception at " + data.dayData[d].houseData[h].getHouseName() +
                                                         "could not be found on the excel document.\n" +
                                                         "You will need to enter the data manually.";
                                        JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
                                        break;
                                    }
                                    // else move on to the next cell
                                    else {
                                        // move on to next cell
                                        cell_number++;
                                    }
                                }
                                
                                
                                 /* 
                                 * find house row
                                 * 
                                 * calculate length of time
                                 * find worker's pay
                                 * calculate amount earned for worker
                                 * 
                                 * insert amount earned into correct cell
                                 * 
                                 * done!
                                 * 
                                 */
                                
                            }
                            
                        }
                        
                    }} // ending if house named
                    
                }
                
            }
            XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
            
            
            
            
            
            // 4) write to new file
            File save_location = Settings.getExcelSaveLocation();
            
            // generate save name
            String save_name = new String();
            Calendar copy2 = (Calendar) c.clone();
            firstday = copy2.get( Calendar.DAY_OF_MONTH );
            copy2.add( Calendar.DAY_OF_MONTH, 4);
            lastday = copy2.get( Calendar.DAY_OF_MONTH );
            
            if ( lastday < firstday ) {
                save_name = new String(     
                        months[c.get(Calendar.MONTH)] +
                        firstday +
                        "-" +
                        months[copy2.get( Calendar.MONTH )] +
                        lastday + "," +
                        c.get(Calendar.YEAR) +
                        ".xlsx");
            }
            else {
                save_name = new String(     
                        months[c.get(Calendar.MONTH)] +
                        firstday +
                        "-" +
                        lastday + "," +
                        c.get(Calendar.YEAR) +
                        ".xlsx");
            }
            // end generate save name
            
            String pathname = new String( save_location.getAbsolutePath() + "\\" + save_name );
            
            // check if this exact path already exists.
            try {
                boolean newfile = false;
                int count = 0;
                while( newfile == false ){
                newfile = true;
                File folder = new File( save_location.getAbsolutePath() );
                File[] listOfFiles = folder.listFiles();

                    for (int i=0; i<listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            
                            if ( pathname.equals( listOfFiles[i].getAbsolutePath() ) ) {

                                newfile = false;
                                count++;
                                if ( lastday < firstday ) {
                                    save_name = new String(     
                                            months[c.get(Calendar.MONTH)] +
                                            firstday +
                                            "-" +
                                            months[copy2.get( Calendar.MONTH )] +
                                            lastday + "," +
                                            c.get(Calendar.YEAR) +
                                            "("+count+")" +                 
                                            ".xlsx");
                                }
                                else {
                                    save_name = new String(     
                                            months[c.get(Calendar.MONTH)] +
                                            firstday +
                                            "-" +
                                            lastday + "," +
                                            c.get(Calendar.YEAR) +
                                            "("+count+")" +                 
                                            ".xlsx");
                                }
                                
                                pathname = new String( save_location.getAbsolutePath() + "\\" + save_name );

                            }
                            
                        } 
                        
                    }
                }   
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            
            new_save = new File( pathname );
            
            FileOutputStream out = new FileOutputStream( new_save );
            wb.write( out );
            
            out.close();
            wb.close();         
            
        }catch(Exception exception){
            exception.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(), "Error: Excel document was not created properly.");
        }
        
        
        
    }
    
    private void setDate(Data data, Sheet sheet) {
        String day;
        String month;
        String year;
        
        String[] months = { "January", "February", "March",
                "April", "May", "June", "July",
                "August", "September", "October",
                "November", "December" };
        
        Calendar c = data.date;
        
        day = getDayString(c, months);
        month = new String(months[c.get(Calendar.MONTH)]);
        year = new String(String.valueOf(c.get(Calendar.YEAR)));
        
        Row row = sheet.getRow(0);
        row.getCell(3).setCellValue(month);
        row.getCell(5).setCellValue(day);
        row.getCell(8).setCellValue(year);
    }
    
    private String getDayString(Calendar c, String[] months) {
        String day;
        Calendar copy = (Calendar) c.clone();
        int firstday = copy.get(Calendar.DAY_OF_MONTH);
        copy.add(Calendar.DAY_OF_MONTH, 4);
        int lastday = copy.get(Calendar.DAY_OF_MONTH);
        
        if (lastday < firstday) {
            day = new String(firstday +      // beginning of week
                    " - " +
                    months[copy.get(Calendar.MONTH )] + " " +
                    lastday); // end of week
        } else {
            day = new String(firstday +      // beginning of week
                    " - " +
                    lastday); // end of week
        }
        return day;
    }
    
}
