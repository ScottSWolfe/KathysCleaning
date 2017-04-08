package com.github.scottswolfe.kathyscleaning.weekend.controller;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendEntry;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;

public class WeekendExcelHelper implements ExcelHelper<WeekendModel> {

    @Override
    public void writeModelToExcel(WeekendModel model, XSSFWorkbook wb) {
        
        Sheet sheet = wb.getSheet("PAYROLL");
        
        Row row;
        boolean found_row;
        boolean found_worker;
        int num_jobs_cap = WeekendPanel.NUM_JOBS_CAP;
        int job_num = 0;
        
        List<WeekendEntry> entries = model.getEntries();
        WeekendEntry entry;
        
        // for each job entry
        for (int i = 0; i < WeekendPanel.NUM_JOB_PANELS; i++) {
            
            entry = entries.get(i);

            if (entry.isWorkedIsChecked()) {
                
                if (job_num >= num_jobs_cap ) {
                    JOptionPane.showMessageDialog( new JFrame(), "Error: the number of weekend jobs you chose will not fit in the Excel Sheet.\nYou need to modify the Excel sheet if you want to include that many unique jobs.", null, JOptionPane.ERROR_MESSAGE);
                    break;
                }
                
                row = sheet.getRow(0);
                
                // find correct row
                found_row = false;
                while(found_row == false) {
                    
                    if (row != null && row.getCell(9) != null && String.valueOf(row.getCell(9)).equals("WEEKEND WORK")) {
                        found_row = true;
                        row = sheet.getRow(row.getRowNum() + 1 + job_num + 1);
                        break;
                    }
                    row = sheet.getRow(row.getRowNum() + 1);
                    if (row.getRowNum() > 1000) {
                        throw new RuntimeException("Could not find Weekend Row");
                    }
                }
                
                row.getCell(3).setCellValue(entry.getCustomer());
                row.getCell(4).setCellValue(entry.getAmountReceived());
                
                // if worker selected
                if (entry.getEmployee() != null && !entry.getEmployee().equals("")) {
                    
                    // find worker
                    row = sheet.getRow(row.getRowNum() - job_num - 1);
                    found_worker = false;
                    int index = 5; // this is where names begin on the excel sheet
                    while (found_worker == false) {
                        
                        // if cell matches worker's name
                        if (row.getCell(index) != null &&
                            String.valueOf(row.getCell(index)).equals(entry.getEmployee())) {
                            found_worker = true;
                            break;
                        }
                        else if (index > 100 || (row.getCell(index) != null && 
                                String.valueOf(row.getCell(index)).equals("Kathy"))) {
                            
                            String message = "Error: the selected employee " + entry.getEmployee() +
                                    " is not on the Excel Sheet. Please modify the Excel sheet as needed.";
                            JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    
                        index++;
                    }
                    
                    // do stuff once worker is found
                    if (found_worker == true) {
                        row = sheet.getRow(row.getRowNum() + job_num + 1);
                        row.getCell(index).setCellValue(entry.getAmountPaid());
                    }
                
                }
                job_num++;
            }
        }
        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
    }

}
