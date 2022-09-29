package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.utility.TimeWindow;
import org.apache.commons.lang3.tuple.Pair;
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

    private static final String COVENANT_SHEET_NAME = "COVENANT";
    private static final String COVENANT_SHEET_INCOME_LABEL = "KATHY SCHOOL INCOME";
    private static final int COVENANT_SHEET_DAY_COLUMN = 0;
    private static final int COVENANT_SHEET_WORKER_COLUMN = 0;
    private static final int COVENANT_SHEET_BEGIN_TIME_COLUMN = 1;
    private static final int COVENANT_SHEET_END_TIME_COLUMN = 2;
    private static final int COVENANT_SHEET_INCOME_LABEL_COLUMN = 1;
    private static final int COVENANT_SHEET_INCOME_VALUE_COLUMN = 4;

    private static final String PAYROLL_SHEET_NAME = "PAYROLL";
    private static final String PAYROLL_SHEET_COVENANT_CUSTOMER_NAME = "COV SCHL";
    private static final int PAYROLL_SHEET_DAY_COLUMN = 9;
    private static final int PAYROLL_SHEET_CUSTOMER_COLUMN = 3;
    private static final int PAYROLL_SHEET_JOB_PAY_COLUMN = 4;

    @Override
    public void writeModelToExcel(CovenantModel model, XSSFWorkbook wb) {

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};

        Sheet sheet = wb.getSheet(COVENANT_SHEET_NAME);

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
            for (int j = 0; j < days.length; j++) {

                worker = entry.getWorker();
                workTime = workTimes.get(j);
                beginTime = workTime.getBeginTime();
                endTime = workTime.getEndTime();

                if (workTime.getBeginTime().isEmpty() && workTime.getEndTime().isEmpty()) {
                    continue;
                } else if (workTime.getBeginTime().isEmpty() || workTime.getEndTime().isEmpty()) {
                    final String message = String.format(
                        "Begin time or end time were incorrectly entered for worker %s\n"
                            + "for day %s on sheet %s. Begin time: '%s'; End time: '%s'.",
                        worker,
                        workTime.getDayOfWeek().getName(),
                        COVENANT_SHEET_NAME,
                        workTime.getBeginTime(),
                        workTime.getEndTime()
                    );
                    JOptionPane.showMessageDialog(
                        ApplicationCoordinator.getInstance().getWindow(),
                        message,
                        null,
                        JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

                boolean day_match = false;
                while (day_match == false) {

                    // making sure the row and columns are not null
                    if (row != null && row.getCell(COVENANT_SHEET_DAY_COLUMN) != null) {        // TODO infinite loop here!!!

                        cell = row.getCell(COVENANT_SHEET_DAY_COLUMN);

                        // checking for the cell that matches the current day of week
                        if (cell.getStringCellValue().equals(days[j])) {

                            day_match = true;

                            // iterate through list of workers on excel sheet
                            // to find current worker from Covenant panel
                            day_complete = false;
                            while (day_complete == false) {

                                // checking that the row and cell are not null
                                if (row != null && row.getCell(COVENANT_SHEET_WORKER_COLUMN) != null) {

                                    // checking for the cell that matches the current worker
                                    if (row.getCell(COVENANT_SHEET_WORKER_COLUMN).getStringCellValue().equals(entry.getWorker())) {

                                        Pair<String, String> times = TimeMethods.convertTo24HourFormat(
                                            workTime.getBeginTime(), workTime.getEndTime(), TimeWindow.COVENANT
                                        );
                                        row.getCell(COVENANT_SHEET_BEGIN_TIME_COLUMN).setCellValue(DateUtil.convertTime(times.getLeft()));
                                        row.getCell(COVENANT_SHEET_END_TIME_COLUMN).setCellValue(DateUtil.convertTime(times.getRight()));

                                        day_complete = true;
                                        break;
                                    }
                                    // or if the next day comes first; the worker is missing from the excel sheet
                                    else if (j < 4 && row.getCell(0).getStringCellValue().equals(days[j + 1])) {

                                        String message = "Error: The selected employee " + worker +
                                                " cannot be found on the Excel Document.\nYou will need " +
                                                "to enter the Employee's payroll data manually on the Excel Sheet.\n" +
                                                "Please correct the employee's name so that it matches the Excel Sheet.";

                                        JOptionPane.showMessageDialog(
                                            ApplicationCoordinator.getInstance().getWindow(),
                                            message,
                                            null,
                                            JOptionPane.ERROR_MESSAGE
                                        );

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
                    else {
                        String message = "Tell Scott to remove this infinite loop!";
                        JOptionPane.showMessageDialog(
                            ApplicationCoordinator.getInstance().getWindow(),
                            message,
                            null,
                            JOptionPane.ERROR_MESSAGE
                        );
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

        sheet = wb.getSheet(PAYROLL_SHEET_NAME);
        int counter = 0;
        int row_num = 0;

        // for each day
        for (int i = 0; i < days.length; i++) {

            while (counter < 150) {

                row = sheet.getRow(row_num);

                if (row != null &&
                         row.getCell(PAYROLL_SHEET_DAY_COLUMN) != null &&
                         row.getCell(PAYROLL_SHEET_DAY_COLUMN).getCellType() == Cell.CELL_TYPE_STRING &&
                         row.getCell(PAYROLL_SHEET_DAY_COLUMN).getStringCellValue().equalsIgnoreCase(days[i])) {

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
                     row.getCell(PAYROLL_SHEET_CUSTOMER_COLUMN) != null &&
                     row.getCell(PAYROLL_SHEET_CUSTOMER_COLUMN).getCellType() == Cell.CELL_TYPE_STRING &&
                     row.getCell(PAYROLL_SHEET_CUSTOMER_COLUMN)
                         .getStringCellValue()
                         .equals(PAYROLL_SHEET_COVENANT_CUSTOMER_NAME)
                ) {

                    if (amounts.size() > 0) {
                        amount = amounts.get(i);
                        row.getCell(PAYROLL_SHEET_JOB_PAY_COLUMN).setCellValue(amount);
                    }
                    else {
                        row.getCell(PAYROLL_SHEET_JOB_PAY_COLUMN).setCellValue(0);
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
        sheet = wb.getSheet(COVENANT_SHEET_NAME);
        counter = 0;
        row_num = 0;
        while ( counter < 150 ) {

            row = sheet.getRow( row_num );
            double total = 0;

            if ( row != null &&
                 row.getCell(COVENANT_SHEET_INCOME_LABEL_COLUMN) != null &&
                 row.getCell(COVENANT_SHEET_INCOME_LABEL_COLUMN).getCellType() == Cell.CELL_TYPE_STRING &&
                 row.getCell(COVENANT_SHEET_INCOME_LABEL_COLUMN)
                     .getStringCellValue()
                     .equals(COVENANT_SHEET_INCOME_LABEL)
            ) {

                for (Double earned : amounts) {
                        total += earned;
                }

                row.getCell(COVENANT_SHEET_INCOME_VALUE_COLUMN).setCellValue(total);

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
