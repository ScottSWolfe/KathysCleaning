package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;

/**
 * Helps CompletedController write data to excel sheet
 */
public class CompletedExcelHelper implements ExcelHelper<CompletedModel> {

/* INSTANCE VARIABLES ======================================================= */

    /**
     * The data to write to the excel file
     */
    CompletedModel completedModel;
   
    
    
/* CONSTRUCTOR ============================================================== */

    public CompletedExcelHelper(CompletedModel completedModel) {
        this.completedModel = completedModel;
    }
    
    public CompletedExcelHelper() {
        
    }
    
    
    
/* PUBLIC METHODS =+========================================================= */

    @Override
    public void writeModelToExcel(CompletedModel model, XSSFWorkbook wb) {
        completedModel = model;
        Sheet sheet = wb.getSheetAt(0);
        writeDate(sheet);
        writeDataForEachDay(sheet);
        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
    }

    
    
/* PRIVATE METHODS ========================================================== */
    
    private void writeDate(Sheet sheet) {    
        Calendar c = SessionModel.getCompletedStartDay();
        String day = FileNameHelper.getDayString(c);
        String month = FileNameHelper.getMonthString(c);
        String year = FileNameHelper.getYearString(c);
        
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
        for (int h = 0; h < completedModel.dayData[d].houseData.length; h++){
            writeDataForHouse(d, h, sheet);
        }
    }
    
    
    private void writeDataForHouse(int d, int h, Sheet sheet) {
        
        Row row;
        Row name_row;
        int row_num;

        name_row = sheet.getRow(d * 9 + 1);

        // if the house data is NOT empty
        if ( !completedModel.dayData[d].houseData[h].getHouseName().isEmpty() &&
                completedModel.dayData[d].houseData[h].getHours() != 0 ) {
            
            // setting excel row number to write to
            row_num = d*9 + 2 + h;  // this formula gives the correct line in the excel sheet template
            row = sheet.getRow( row_num );
            
            String s1 = TimeMethods.convertFormat( completedModel.dayData[d].houseData[h].getTimeBegin(), TimeMethods.HOUSE_TIME );
            String s2 = TimeMethods.convertFormat( completedModel.dayData[d].houseData[h].getTimeEnd(), TimeMethods.HOUSE_TIME );
            
            row.getCell(0).setCellValue( DateUtil.convertTime(s1) );
            row.getCell(1).setCellValue( DateUtil.convertTime(s2) );
            
            // setting # of hours worked
            row.getCell(2).setCellValue( completedModel.dayData[d].houseData[h].getHours() );
            
            // setting house name
            row.getCell(3).setCellValue( completedModel.dayData[d].houseData[h].getHouseName() );
            
            // setting money paid
            row.getCell(4).setCellValue( completedModel.dayData[d].houseData[h].getHousePay() );
                                
            
            // writing in zero for all employees who did not work
            boolean names_remaining = true;
            boolean name_match;
            int index = 5;
            String[] worker = completedModel.dayData[d].getHouseData()[h].getSelectedWorkers();

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
        else if (completedModel.dayData[d].houseData[h].getHouseName().isEmpty() &&
                completedModel.dayData[d].houseData[h].getHours() != 0
                 ||
                 !completedModel.dayData[d].houseData[h].getHouseName().isEmpty() &&
                 completedModel.dayData[d].houseData[h].getHours() == 0 ) {
        }
        else {
            // do nothing
        }
        
        // if there is exception data, add data to excel sheet
        if (!completedModel.dayData[d].houseData[h].getHouseName().isEmpty()) { // only if the house has been named
        
        ExceptionData exd = completedModel.dayData[d].houseData[h].getExceptionData();
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
                                             " from the exception at " + completedModel.dayData[d].houseData[h].getHouseName() +
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
