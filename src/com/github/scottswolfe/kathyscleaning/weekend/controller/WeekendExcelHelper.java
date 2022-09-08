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

    private static final String PAYROLL_SHEET_NAME = "PAYROLL";
    private static final String PAYROLL_SHEET_WEEKEND_WORK_DAY_NAME = "WEEKEND WORK";
    private static final String PAYROLL_SHEET_WORKERS_STOP_LABEL = "Kathy";
    private static final int PAYROLL_SHEET_DAY_COLUMN = 9;
    private static final int PAYROLL_SHEET_JOB_ROW_OFFSET_FROM_WORKER_NAMES = 2;
    private static final int PAYROLL_SHEET_JOB_ROW_OFFSET_FROM_DAY_LABEL = PAYROLL_SHEET_JOB_ROW_OFFSET_FROM_WORKER_NAMES + 1;
    private static final int PAYROLL_SHEET_CUSTOMER_COLUMN = 3;
    private static final int PAYROLL_SHEET_JOB_PAY_COLUMN = 4;
    private static final int PAYROLL_SHEET_WORKER_NAMES_START_COLUMN = 5;

    @Override
    public void writeModelToExcel(WeekendModel model, XSSFWorkbook wb) {

        Sheet sheet = wb.getSheet(PAYROLL_SHEET_NAME);

        Row row;
        boolean found_row;
        boolean found_worker;
        int num_jobs_cap = WeekendPanel.NUM_JOBS_CAP;
        int job_num = 0;

        List<WeekendEntry> entries = model.getEntries();
        WeekendEntry entry;

        // for each job entry
        for (int i = 0; i < entries.size(); i++) {

            entry = entries.get(i);

            if (entry.isWorkedIsChecked()) {

                if (job_num >= num_jobs_cap ) {
                    JOptionPane.showMessageDialog( new JFrame(), "Error: the number of weekend jobs you chose will not fit in the Excel Sheet.\nYou need to modify the Excel sheet if you want to include that many unique jobs.", null, JOptionPane.ERROR_MESSAGE);
                    break;
                }

                int row_num = 0;
                row = sheet.getRow(row_num);

                // find correct row
                found_row = false;
                while(found_row == false) {

                    if (row != null
                        && row.getCell(PAYROLL_SHEET_DAY_COLUMN) != null
                        && String.valueOf(row.getCell(PAYROLL_SHEET_DAY_COLUMN)).equals(PAYROLL_SHEET_WEEKEND_WORK_DAY_NAME)
                    ) {
                        found_row = true;
                        row_num = row.getRowNum() + PAYROLL_SHEET_JOB_ROW_OFFSET_FROM_DAY_LABEL + job_num;
                        row = sheet.getRow(row_num);
                        break;
                    }
                    row_num++;
                    row = sheet.getRow(row_num);
                    if (row.getRowNum() > 1000) {
                        throw new RuntimeException("Could not find Weekend Row");
                    }
                }

                row.getCell(PAYROLL_SHEET_CUSTOMER_COLUMN).setCellValue(entry.getCustomer());
                row.getCell(PAYROLL_SHEET_JOB_PAY_COLUMN).setCellValue(entry.getAmountReceived());

                // if worker selected
                if (entry.getEmployee() != null && !entry.getEmployee().equals("")) {

                    // find worker
                    row_num = row.getRowNum() - job_num - PAYROLL_SHEET_JOB_ROW_OFFSET_FROM_WORKER_NAMES;
                    row = sheet.getRow(row_num);
                    found_worker = false;
                    int index = PAYROLL_SHEET_WORKER_NAMES_START_COLUMN;
                    while (found_worker == false) {

                        // if cell matches worker's name
                        if (row.getCell(index) != null &&
                            row.getCell(index).getStringCellValue().equals(entry.getEmployee())) {
                            found_worker = true;
                            break;
                        }
                        else if (index > 100 || (row.getCell(index) != null &&
                                String.valueOf(row.getCell(index)).equals(PAYROLL_SHEET_WORKERS_STOP_LABEL))) {

                            String message = "Error: the selected employee " + entry.getEmployee() +
                                    " is not on the Excel Sheet. Please modify the Excel sheet as needed.";
                            JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
                            break;
                        }

                        index++;
                    }

                    // do stuff once worker is found
                    if (found_worker == true) {
                        row = sheet.getRow(row.getRowNum() + job_num + PAYROLL_SHEET_JOB_ROW_OFFSET_FROM_WORKER_NAMES);
                        row.getCell(index).setCellValue(entry.getAmountPaid());
                    }

                }
                job_num++;
            }
        }
        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
    }

}
