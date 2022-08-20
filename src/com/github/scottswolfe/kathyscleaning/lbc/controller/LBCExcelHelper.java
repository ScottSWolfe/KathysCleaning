package com.github.scottswolfe.kathyscleaning.lbc.controller;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCEntry;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.utility.ExcelUtil;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;
import com.github.scottswolfe.kathyscleaning.utility.TimeWindow;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LBCExcelHelper implements ExcelHelper<LBCModel> {

    private static final String LBC_SHEET_NAME = "LBC";
    private static final String LBC_SHEET_INCOME_LABEL = "KATHY CHURCH INCOME";
    private static final int LBC_SHEET_DAY_COLUMN = 0;
    private static final int LBC_SHEET_WORKER_COLUMN = 0;
    private static final int LBC_SHEET_BEGIN_TIME_COLUMN = 1;
    private static final int LBC_SHEET_END_TIME_COLUMN = 2;
    private static final int LBC_SHEET_INCOME_LABEL_COLUMN = 1;
    private static final int LBC_SHEET_INCOME_VALUE_COLUMN = 4;

    private static final String PAYROLL_SHEET_NAME = "PAYROLL";
    private static final String PAYROLL_SHEET_LBC_CUSTOMER_NAME = "LBC";
    private static final int PAYROLL_SHEET_DAY_COLUMN = 9;
    private static final int PAYROLL_SHEET_CUSTOMER_COLUMN = 3;
    private static final int PAYROLL_SHEET_JOB_PAY_COLUMN = 4;

    private static final Map<DayOfWeek, String> PAYROLL_SHEET_DAY_NAME_MAP =
        LBCModel.DAYS.stream().collect(Collectors.toMap(
            day -> day,
            day -> day.equals(DayOfWeek.SATURDAY) ? "WEEKEND WORK" : day.getName().toUpperCase()
        ));

    private static final String MISSING_WORKER_MESSAGE =
        "The selected employee %s for day %s cannot be found on on the %s sheet\n"
            + "of the the Excel template. You will need to enter the employee's\n"
            + "payroll data manually. You may need to correct the employee's name\n"
            + "so that it matches the name on the Excel sheet.";

    @Override
    public void writeModelToExcel(LBCModel model, XSSFWorkbook wb) {

        // Setting time worked
        final Sheet lbcSheet = wb.getSheet(LBC_SHEET_NAME);

        for (LBCEntry lbcWorkerEntry : model.getEntries()) {

            // Skip if the worker's name is empty
            final String workerName = lbcWorkerEntry.getWorker();
            if (workerName.isEmpty()) {
                continue;
            }

            for (WorkTime workTime : lbcWorkerEntry.getWorkTimes()) {

                if (workTime.getBeginTime().isEmpty() && workTime.getEndTime().isEmpty()) {
                    continue;
                } else if (workTime.getBeginTime().isEmpty() || workTime.getEndTime().isEmpty()) {
                    final String message = String.format(
                        "Begin time or end time were incorrectly entered for worker %s\n"
                        + "for day %s on sheet %s. Begin time: '%s'; End time: '%s'.",
                        workerName,
                        workTime.getDayOfWeek().getName(),
                        lbcSheet.getSheetName(),
                        workTime.getBeginTime(),
                        workTime.getEndTime()
                    );
                    JOptionPane.showMessageDialog(
                        new JFrame(), message, null, JOptionPane.ERROR_MESSAGE
                    );
                    continue;
                }

                if (workTime.getDayOfWeek() == null) {
                    throw new RuntimeException("Day of week cannot be null for LBC WorkTime.");
                }

                Row row;
                try {
                    row = findLBCRow(lbcSheet, workerName, workTime.getDayOfWeek().getName());
                } catch (IllegalArgumentException e) {
                    final String message = String.format(
                        MISSING_WORKER_MESSAGE, workerName, workTime.getDayOfWeek().getName(), lbcSheet.getSheetName()
                    );
                    JOptionPane.showMessageDialog(new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                /*
                String beginTimeToSet = "00:00";
                String endTimeToSet = "00:00";
                if (workTime.getBeginTime() != null && !workTime.getBeginTime().isEmpty()
                    && workTime.getEndTime() != null && !workTime.getEndTime().isEmpty()
                ) {
                    Pair<String, String> times = TimeMethods.convertTo24HourFormat(
                        workTime.getBeginTime(), workTime.getEndTime(), TimeWindow.LBC
                    );
                    beginTimeToSet = times.getLeft();
                    endTimeToSet = times.getRight();
                } else {
                    System.out.println("LBCExcelHelper: begin time and/or end time is null or empty");
                }
                row.getCell(LBC_SHEET_BEGIN_TIME_COLUMN).setCellValue(DateUtil.convertTime(beginTimeToSet));
                row.getCell(LBC_SHEET_END_TIME_COLUMN).setCellValue(DateUtil.convertTime(endTimeToSet));

                 */

                Pair<String, String> times = TimeMethods.convertTo24HourFormat(
                    workTime.getBeginTime(), workTime.getEndTime(), TimeWindow.LBC
                );
                row.getCell(LBC_SHEET_BEGIN_TIME_COLUMN).setCellValue(DateUtil.convertTime(times.getLeft()));
                row.getCell(LBC_SHEET_END_TIME_COLUMN).setCellValue(DateUtil.convertTime(times.getRight()));
            }
        }

        // Adding amount earned
        final Sheet payrollSheet = wb.getSheet(PAYROLL_SHEET_NAME);

        if (model.getAmountsEarned().size() != LBCModel.DAYS.size()) {
            throw new RuntimeException("Incorrect number of amounts");
        }
        final Map<String, Double> dayToAmount = new HashMap<>();
        for (int i = 0; i < LBCModel.DAYS.size(); i++) {
            dayToAmount.put(PAYROLL_SHEET_DAY_NAME_MAP.get(LBCModel.DAYS.get(i)), model.getAmountsEarned().get(i));
        }

        for (Map.Entry<String, Double> entry : dayToAmount.entrySet()) {
            final Row row = findPayrollLBCRow(payrollSheet, entry.getKey());
            row.getCell(PAYROLL_SHEET_JOB_PAY_COLUMN).setCellValue(entry.getValue());
        }

        // Adding total LBC earned on LBC tab
        final double totalEarned = model.getAmountsEarned().stream().mapToDouble(d -> d).sum();
        int rowNumber = 0;
        for (; rowNumber < 1000; rowNumber++) {
            if (ExcelUtil.doesCellValueEqualString(
                lbcSheet, rowNumber, LBC_SHEET_INCOME_LABEL_COLUMN, LBC_SHEET_INCOME_LABEL
            )) {
                final Row row = lbcSheet.getRow(rowNumber);
                row.getCell(LBC_SHEET_INCOME_VALUE_COLUMN).setCellValue(totalEarned);
                break;
            }
        }

        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
    }

    private Row findLBCRow(final Sheet sheet, final String workerName, final String dayOfWeek) {

        final int dayOfWeekRowNumber = findDayOfWeekRow(sheet, dayOfWeek, LBC_SHEET_DAY_COLUMN);

        int workerRowNumber = dayOfWeekRowNumber + 1;
        for (; workerRowNumber < 1000; workerRowNumber++) {

            if (ExcelUtil.isCellValueInCollection(
                sheet, workerRowNumber, LBC_SHEET_DAY_COLUMN, DayOfWeek.getSetOfNames()
            )) {
                throw createWorkerNotFoundException(sheet, workerName, dayOfWeek);
            }

            if (ExcelUtil.doesCellValueEqualString(sheet, workerRowNumber, LBC_SHEET_WORKER_COLUMN, workerName)) {
                return sheet.getRow(workerRowNumber);
            }
        }

        throw createWorkerNotFoundException(sheet, workerName, dayOfWeek);
    }

    private IllegalArgumentException createWorkerNotFoundException(
        final Sheet sheet,
        final String workerName,
        final String dayOfWeek
    ) {
        return new IllegalArgumentException(String.format(
            "Unable to find row for worker %s on day %s on sheet %s.",
            workerName,
            dayOfWeek,
            sheet.getSheetName()
        ));
    }

    private Row findPayrollLBCRow(final Sheet sheet, final String dayOfWeek) {

        final int dayOfWeekRowNumber = findDayOfWeekRow(sheet, dayOfWeek, PAYROLL_SHEET_DAY_COLUMN);

        int workerRowNumber = dayOfWeekRowNumber + 1;
        for (; workerRowNumber < 1000; workerRowNumber++) {

            if (ExcelUtil.isCellValueInCollection(
                sheet, workerRowNumber, PAYROLL_SHEET_CUSTOMER_COLUMN, PAYROLL_SHEET_DAY_NAME_MAP.values()
            )) {
                throw createLBCNotFoundException(sheet, dayOfWeek);
            }

            if (ExcelUtil.doesCellValueEqualString(
                sheet, workerRowNumber, PAYROLL_SHEET_CUSTOMER_COLUMN, PAYROLL_SHEET_LBC_CUSTOMER_NAME
            )) {
                return sheet.getRow(workerRowNumber);
            }
        }

        throw createLBCNotFoundException(sheet, dayOfWeek);
    }

    private IllegalArgumentException createLBCNotFoundException(
        final Sheet sheet,
        final String dayOfWeek
    ) {
        return new IllegalArgumentException(String.format(
            "Unable to find LBC on day %s on sheet %s.",
            dayOfWeek,
            sheet.getSheetName()
        ));
    }

    private int findDayOfWeekRow(
        final Sheet sheet,
        final String dayOfWeek,
        int columnNumber
    ) {
        int rowNumber = 0;
        for (; rowNumber < 1000; rowNumber++) {
            if (ExcelUtil.doesCellValueEqualString(sheet, rowNumber, columnNumber, dayOfWeek)) {
                return rowNumber;
            }
        }

        throw new IllegalArgumentException(String.format(
            "Unable to find day %s on sheet %s.",
            dayOfWeek,
            sheet.getSheetName()
        ));
    }
}
