package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantEntry;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;

public class CovenantExcelHelper implements ExcelHelper<CovenantModel> {

    @Override
    public void writeModelToExcel(CovenantModel model, XSSFWorkbook wb) {

        String[] days = {"Monday", "Tuesday", "Wednesday",
                "Thursday", "Friday"};
        
        Sheet sheet = wb.getSheet("COVENANT");

        Row row;
        Cell cell;
        boolean day_complete;
        boolean missing_employee = false;
        String s1;
        String s2;
        
        List<CovenantEntry> entries = model.getEntries();
        CovenantEntry entry;
        String worker;
        List<WorkTime> workTimes;
        WorkTime workTime;
        String beginTime;
        String endTime;
        
        // iterate through each entry
        for (int i = 0; i < entries.size(); i++) {
                            
            entry = entries.get(i);
            workTimes = entry.getWorkTimes();
            row = sheet.getRow(0);
                
                // iterate through each day of the week
                for (int j = 0; j < 5; j++) {
                    
                    worker = entry.getWorker();
                    workTime = workTimes.get(j);
                    beginTime = workTime.getBeginTime();
                    endTime = workTime.getEndTime();
                    
                    boolean day_match = false;
                    while (day_match == false) {
                        
                        // making sure the row and columns are not null
                        if (row != null && row.getCell(0) != null) {
                                                            
                            cell = row.getCell(0);
                                                            
                            // checking for the cell that matches the current day of week
                            if (cell.getStringCellValue().equals(days[j])) {
                                
                                day_match = true;
                                                                    
                                // iterate through list of workers on excel sheet
                                // to find current worker from Covenant panel
                                day_complete = false;
                                while (day_complete == false) {
                                                                            
                                    // checking that the row and cell are not null
                                    if (row != null && row.getCell(0) != null) {
                                                                                    
                                        // checking for the cell that matches the current worker
                                        if (row.getCell(0).getStringCellValue().equals(entry.getWorker())) {
                                                                                        
                                            s1 = TimeMethods.convertFormat(beginTime, TimeMethods.COVENANT_TIME);
                                            s2 = TimeMethods.convertFormat(endTime, TimeMethods.COVENANT_TIME);
                                                
                                            row.getCell(1).setCellValue( DateUtil.convertTime(s1) );
                                            row.getCell(2).setCellValue( DateUtil.convertTime(s2) );
                                                                                                    
                                            day_complete = true;
                                            break;
                                        }
                                        // or if the next day comes first; the worker is missing from the excel sheet
                                        else if (j < 4 && row.getCell(0).getStringCellValue().equals(days[j + 1])) {
                                            
                                            String message = "Error: The selected employee " + worker +
                                                    " cannot be found on the Excel Document.\nYou will need " +
                                                    "to enter the Employee's payroll data manually on the Excel Sheet.\n" +
                                                    "Please correct the employee's name so that it matches the Excel Sheet.";
                                                    
                                            JOptionPane.showMessageDialog( new JFrame(), message,
                                                    null, JOptionPane.ERROR_MESSAGE);
                                            
                                            row = sheet.getRow(row.getRowNum() - 1);
                                            missing_employee = true;
                                        }
                                        
                                    if (missing_employee == true) {
                                        break;
                                    }
                                }
                                row = sheet.getRow(row.getRowNum() + 1);
                                    
                                    
                            }
                            row = sheet.getRow(row.getRowNum() + 1);
                                    
                        }
                        row = sheet.getRow(row.getRowNum() + 1);  
                    }
                    
                    
                }
                if (missing_employee == true) {
                    missing_employee = false;
                    break;
                }
                    
            }                
            
        }
        
        // adding in amount earned
        List<Double> amounts = model.getAmountsEarned();
        Double amount;
        
        sheet = wb.getSheet("PAYROLL");
        int counter = 0;
        int row_num = 0;
        
        // for each day
        for (int i = 0; i < days.length; i++) {
            
            while (counter < 150) {
                
                row = sheet.getRow(row_num);
                                    
                if (row != null &&
                         row.getCell(9) != null &&
                         row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING &&
                         row.getCell(9).getStringCellValue().equalsIgnoreCase(days[i])) {
                    
                    row_num++;
                    break;
                }
                row_num++;
                counter++;
            }
            
            
            counter = 0;
            while (counter < 11) {
                
                row = sheet.getRow(row_num);
                
                if (row != null &&
                     row.getCell(2) != null &&
                     row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING &&
                     row.getCell(2).getStringCellValue().equals("COV SCHL") ) {
                    
                    if (amounts.size() > 0) {
                        amount = amounts.get(i);
                        row.getCell(4).setCellValue(amount);
                    }
                    else {
                        row.getCell(4).setCellValue(0);
                    }
                    
                    row_num++;
                    counter = 0;
                    break;
                    
                }
                else {
                    counter++;
                    row_num++;
                }
                
            }
        
        }
        
        // putting in total covenant earned on Covenant tab
        sheet = wb.getSheet("COVENANT");
        counter = 0;
        row_num = 0;
        while ( counter < 150 ) {
            
            row = sheet.getRow( row_num );
            double total = 0;
            
            if ( row != null &&
                 row.getCell(1) != null &&
                 row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING &&
                 row.getCell(1).getStringCellValue().equals("KATHY INCOME") ) {
                
                for (Double earned : amounts) {   
                        total += earned;
                }
                
                row.getCell(4).setCellValue(total);
                
                row_num++;
                break;
                
            }
            else {
                counter++;
                row_num++;
            }
            
        }

        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
        
    }

}
