package com.github.scottswolfe.kathyscleaning.completed.controller;

import java.util.Calendar;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.utility.TimeWindow;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionData;
import com.github.scottswolfe.kathyscleaning.completed.model.ExceptionEntry;
import com.github.scottswolfe.kathyscleaning.general.helper.FileNameHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.general.helper.ExcelMethods;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;

/**
 * Helps CompletedController write data to excel sheet
 */
public class CompletedExcelHelper implements ExcelHelper<CompletedModel> {

    private static final String COMPLETED_SHEET_NAME = "PAYROLL";

    private static final int HOUSE_ROW_OFFSET = 2;

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
        Sheet sheet = wb.getSheet(COMPLETED_SHEET_NAME);
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

        int name_row_num = getNameRowNumber(sheet, d);
        name_row = sheet.getRow(name_row_num);

        // if the house data is NOT empty
        final String houseName = completedModel.dayData[d].houseData[h].getHouseName();
        final String beginTime = completedModel.dayData[d].houseData[h].getTimeBegin();
        final String endTime = completedModel.dayData[d].houseData[h].getTimeEnd();
        if (!houseName.isEmpty()
            && beginTime != null && !beginTime.isEmpty()
            && endTime != null && !endTime.isEmpty()
        ) {

            // setting excel row number to write to
            row_num = name_row_num + HOUSE_ROW_OFFSET + h;
            row = sheet.getRow( row_num );

            Pair<String, String> times = TimeMethods.convertTo24HourFormat(beginTime, endTime, TimeWindow.HOUSES);
            row.getCell(0).setCellValue(DateUtil.convertTime(times.getLeft()));
            row.getCell(1).setCellValue(DateUtil.convertTime(times.getRight()));

            // setting # of hours worked
            double hours = TimeMethods.getNumberOfHours(
                completedModel.dayData[d].houseData[h].getTimeBegin(),
                completedModel.dayData[d].houseData[h].getTimeEnd(),
                TimeWindow.HOUSES
            );
            row.getCell(2).setCellValue(hours);

            // setting house name
            row.getCell(3).setCellValue(houseName);

            // setting money paid
            row.getCell(4).setCellValue(completedModel.dayData[d].houseData[h].getHousePay());

            // writing in zero for all employees who did not work
            boolean names_remaining = true;
            boolean name_match;
            int index = 5;
            List<String> worker = completedModel.dayData[d].getHouseData()[h].getSelectedWorkers();

            // while there are still names remaining in the excel sheet
            while ( names_remaining == true && index < 25 ) {
                // for the number of selected workers at the house
                name_match = false;
                // TODO needs refactoring here
                for ( int j=0; j<worker.size(); j++) {

                    // if selected worker matches name on excel sheet
                    if ( name_row.getCell(index).getStringCellValue().equals(worker.get(j)) ) {
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
                        s = ExcelMethods.insertHoursWorkedInPlaceOfCellReference(s,0.0);
                        row.getCell(index).setCellFormula(s);
                    }
                    else {
                        row.getCell(index).setCellValue(0);
                    }
                }
                index++;
            }

        }

        // if there is exception data, add data to excel sheet
        if (!houseName.isEmpty()) { // only if the house has been named

            ExceptionData exceptionData = completedModel.dayData[d].houseData[h].getExceptionData();
            if (exceptionData.isException()) {

                // iterate through the names in the exception data
                for (ExceptionEntry entry : exceptionData.getEntries()) {
                 // find name row; trace down name row looking for match with worker_name[m]
                    //      if no match (find kathy) send error report and move on
                    boolean name_found = false;
                    //Cell name_cell = row.getCell(5);
                    int cell_number = 5;

                    if (entry.getWorker_name() == null || entry.getWorker_name().length() == 0) {
                        continue;
                    }

                    while (name_found == false) {

                        // if cell name matches current worker name
                        Cell cell = name_row.getCell(cell_number);
                        String name = "";
                        if (cell != null &&
                            cell.getCellType() == Cell.CELL_TYPE_FORMULA &&
                            cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING)
                        {
                            name = cell.getRichStringCellValue().getString();
                        }
                        if (name.equals(entry.getWorker_name())) {

                            // calculate hours worked
                            double hours = TimeMethods.getNumberOfHours(
                                entry.getTime_begin(), entry.getTime_end(), TimeWindow.COVENANT
                            );

                            // insert data into excel doc
                            Row house_row = sheet.getRow( name_row_num + HOUSE_ROW_OFFSET + h );

                                String s = house_row.getCell(cell_number).getCellFormula(); // issue with numeric cells??
                                s = ExcelMethods.insertHoursWorkedInPlaceOfCellReference(s,hours);
                                house_row.getCell(cell_number).setCellFormula(s);

                            break;

                        }
                        // if cell name is Kathy
                        else if (name_row.getCell(cell_number).getStringCellValue().equals("Kathy") ) {
                            String message = "Error: Employee " + entry.getWorker_name() +
                                             " from the exception at " + completedModel.dayData[d].houseData[h].getHouseName() +
                                             " could not be found on the excel document.\n" +
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
                }

            }
        } // ending if house named
    }

    /**
     * TODO: the name_rows are now variably spaced, some 9 apart, some 10.
     * This is a temporary fix to allow the 10 row days to work.
     * This needs to be refactored to be more general and not have
     * hardcoded values.
     */
    private int getNameRowNumber(final Sheet sheet, int day_num) {
        int name_row_num = day_num * 9 + 1;
        int row_count = 0;
        while (row_count < 100) {
            Row name_row = sheet.getRow(name_row_num);
            Cell name_row_first_cell = name_row.getCell(0);
            if (name_row_first_cell != null && name_row_first_cell.getCellType() == Cell.CELL_TYPE_STRING) {
                String cellText = name_row_first_cell.getStringCellValue();
                if (cellText != null && cellText.equals("START")) {
                    return name_row_num;
                }
            }
            name_row_num++;
            row_count++;
        }
        throw new RuntimeException("Could not find name row.");
    }
}
