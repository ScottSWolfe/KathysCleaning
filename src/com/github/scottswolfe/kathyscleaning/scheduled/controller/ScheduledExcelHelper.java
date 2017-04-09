package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_DayData;
import com.github.scottswolfe.kathyscleaning.scheduled.model.WorkerSchedule;

public class ScheduledExcelHelper implements ExcelHelper<NW_Data> {

    @Override
    public void writeModelToExcel(NW_Data model, XSSFWorkbook wb) {

        Sheet sheet = wb.getSheetAt(2);
        
        // 2) input schedules for each employee on each day
        int n_column = 3;       // column with worker names is column 3
        int d_column = 1;       // column with days is column 1
        
        Row nrow;               // current row in looking for worker names
        Row drow;               // current row in looking for days
        
        int nnum;
        int dnum;
        
        String s1;
        String s2;
        
        WorkerSchedule ws;
        String[] weekday = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        
        NW_DayData[] days = model.dayData;
        
        // for each day
        for(int d = 0; d < 5; d++){
            
            NW_DayData dayData = days[d];
            
            // for each employee
            WorkerSchedule[] workSchedules = dayData.getWorkerSchedule();
            for (int w = 0; w < workSchedules.length; w++) {
                
                ws = workSchedules[w];
                
                nnum = 0;
                nrow = sheet.getRow(nnum);

                boolean d1 = false;
                boolean d2 = false;
                                
                // while worker name has not been found
                while (!d1) {
                    
                    if (nrow != null) {
                        
                        if (nrow.getCell(n_column) != null && nrow.getCell(n_column).getCellType() == Cell.CELL_TYPE_STRING) {
                            s1 = nrow.getCell(n_column).getStringCellValue();
                        }
                        else {
                            s1 = null;
                        }
                    }
                    else {
                        s1 = null;
                    }
                    
                    // compare strings to worker name; if name is found
                    if (ws.isEquivalent(s1)) {
                        
                        dnum = nnum + 1;                    //begin row after name was found
                        drow = sheet.getRow(dnum);

                        // while correct day of week has not been found
                        while ( !d2 ) {
                            
                            if (drow != null) {
                                if (drow.getCell(d_column) != null && drow.getCell(d_column).getCellType() == Cell.CELL_TYPE_STRING) {
                                    s2 = drow.getCell(d_column).getStringCellValue();
                                }
                                else {
                                    s2 = "";
                                }
                            }
                            else {
                                s2 = "";
                            }
                                                
                            // compare strings in succeeding rows and column 1? to String[] s = [Monday, Tuesday, Wednesday, Thursday, Friday]
                            // if day is found
                            if ( s2.equals( weekday[d] ) ) {
                            
                                // move over one cell and input schedule
                                drow.getCell(d_column + 1).setCellValue( ws.getSchedule() );
                                d1 = true;
                                d2 = true;
                                
                            }
                            dnum++;
                            drow = sheet.getRow( dnum );

                            if (nnum > 5000) {
                                d2 = true;
                            }
                        }
                        
                    }
                    nnum++;
                    nrow = sheet.getRow(nnum);
                    if (nnum > 5000) {
                        JOptionPane.showMessageDialog(new JFrame(), "Employee " + ws.getName() + " does not match any name in the Excel file");
                        d1 = true;
                    }
                }
                
            }
        }        
        makeUnusedCellsBlank(wb, sheet);        
    }
    
    private void makeUnusedCellsBlank(XSSFWorkbook wb, Sheet sheet) {

        FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
        Cell cell;
        CellValue value;
        
        int house_column = 3;
        int hours_column = 4;
        int total_column = 5;
        int row = 0;
        
        int counter = 0;
        while (counter < 50) {
            cell = getCellSafely(sheet, row, hours_column, evaluator);
            if (cell != null) {
                value = evaluator.evaluate(cell);
                if (value.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                    if (value.getNumberValue() == 0) {
                        sheet.getRow(row).getCell(house_column).setCellValue("");
                        sheet.getRow(row).getCell(hours_column).setCellValue("");
                        sheet.getRow(row).getCell(total_column).setCellValue("");
                        counter = 0;
                    }
                }
            }
            row++;
            counter++;
        }
    }
    
    private Cell getCellSafely(Sheet sheet, int r, int c, FormulaEvaluator evaluator) {
        if (sheet == null) {
            return null;
        }
        Row row = sheet.getRow(r);
        if (row == null) {
            return null;
        }
        Cell cell = row.getCell(c);
        if (cell == null) {
            return null;
        }
        CellValue value = evaluator.evaluate(cell);
        if (value == null) {
            return null;
        }
        return cell;
    }
        
}
