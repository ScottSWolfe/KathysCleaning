package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.general.helper.DateHelper;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;

/**
 * Helps CompletedController write data to excel sheet
 */
public class CompletedExcelHelper implements ExcelHelper<Data> {

/* INSTANCE VARIABLES ======================================================= */

    /**
     * The data to write to the excel file
     */
    Data data;
    
    /**
     * The excel file to which to write the data
     */
    File file;

    
    
/* CONSTRUCTOR ============================================================== */

    public CompletedExcelHelper(Data data, File file) {
        this.data = data;
        this.file = file;
    }
    
    public CompletedExcelHelper() {
        
    }
    
    
    
/* PUBLIC METHODS =+========================================================= */

    @Override
    public void writeModelToExcel(Data model, File file) {
        data = model;
        this.file = file;
        try {
            // Create workbook
            InputStream input = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(input);
            Sheet sheet = wb.getSheetAt(0);

            // set date
            writeDate(sheet);
                   
            // input house data
            writeDataForEachDay(sheet);
            XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
            
            // write to new file
            writeToNewFile(wb);
            
            // close workbook
            wb.close();      
            
        } catch(FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document could not be found.");
        } catch(IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");
        } catch(Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");
        }
        
    }

    
    
/* PRIVATE METHODS ========================================================== */
    
    private void writeToNewFile(XSSFWorkbook wb) {
        File save_location = Settings.getExcelSaveLocation();
        String save_name = DateHelper.generateSaveName(data);
        String pathname = new String(
                save_location.getAbsolutePath() + "\\" + save_name);
        pathname = checkIfPathnameExists(pathname, save_location, data.date);
        File new_save = new File(pathname);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new_save);
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    private void writeDate(Sheet sheet) {    
        Calendar c = data.date;
        String day = DateHelper.getDayString(c);
        String month = DateHelper.getMonthString(c);
        String year = DateHelper.getYearString(c);
        
        Row row = sheet.getRow(0);
        row.getCell(3).setCellValue(month);
        row.getCell(5).setCellValue(day);
        row.getCell(8).setCellValue(year);
    }
    
    
    private void writeDataForEachDay(Sheet sheet) {        
        for(int d = 0;  d < 5; d++){
            writeDataForDay(d, sheet);
        }
    }
    
    
    private void writeDataForDay(int d, Sheet sheet) {
        for (int h = 0; h < data.dayData[d].houseData.length; h++){
            writeDataForHouse(d, h, sheet);
        }
    }
    
    
    private void writeDataForHouse(int d, int h, Sheet sheet) {
        
        Row row;
        Row name_row;
        int row_num;

        name_row = sheet.getRow(d * 9 + 1);

        // if the house data is NOT empty
        if ( !data.dayData[d].houseData[h].getHouseName().isEmpty() &&
                data.dayData[d].houseData[h].getHours() != 0 ) {
            
            // setting excel row number to write to
            row_num = d*9 + 2 + h;  // this formula gives the correct line in the excel sheet template
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
            boolean names_remaining = true;
            boolean name_match;
            int index = 5;
            String[] worker = data.dayData[d].getHouseData()[h].getSelectedWorkers();

            // while there are still names remaining in the excel sheet
            while ( names_remaining == true && index < 25 ) {
                // for the number of selected workers at the house  
                name_match = false;
                for ( int j=0; j<worker.length; j++) {
                    
                    // if selected worker matches name on excel sheet
                    if ( name_row.getCell(index).getStringCellValue().equals(worker[j]) ) {
                        name_match = true;
                        break;
                    }
                    // if name on excel sheet is kathy
                    else if ( name_row.getCell(index).getStringCellValue().equals("Kathy")) {
                        names_remaining = false;
                        name_match = true;
                        break;
                    }
                    
                }
                // if none of the selected workers match the name on the excel sheet
                if ( name_match == false ) {
                    
                    if (row.getCell(index) == null) {
                        // do nothing
                    }
                    else if (row.getCell(index) != null && row.getCell(index).getCellType() == Cell.CELL_TYPE_FORMULA) {
                        String s = row.getCell(index).getCellFormula();
                        s = ExcelMethods.changeFormula(s,0.0);
                        row.getCell(index).setCellFormula(s);
                    }
                    else {
                        row.getCell(index).setCellValue(0);
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
                                s = ExcelMethods.changeFormula(s,hours);
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
    
    
    private String checkIfPathnameExists(String pathname, File save_location,
                                         Calendar c) {
        int count = 0;
        while(true) {
            if (new File(save_location, pathname).exists()) {
                count++;
                pathname = DateHelper.incrementPathnameCount(pathname, count, c);
            } else {
                return pathname;
            }
        }
    }
    
}
