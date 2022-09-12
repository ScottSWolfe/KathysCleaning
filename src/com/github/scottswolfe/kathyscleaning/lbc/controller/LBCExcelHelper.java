package com.github.scottswolfe.kathyscleaning.lbc.controller;

import com.github.scottswolfe.kathyscleaning.enums.DayOfWeek;
import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCDay;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import com.github.scottswolfe.kathyscleaning.utility.ExcelUtil;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;
import com.github.scottswolfe.kathyscleaning.utility.TimeWindow;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.Collection;
import java.util.List;
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
    private static final int PAYROLL_SHEET_START_TIME_COLUMN = 0;
    private static final int PAYROLL_SHEET_END_TIME_COLUMN = 1;
    private static final int PAYROLL_SHEET_HOURS_COLUMN = 2;
    private static final int PAYROLL_SHEET_CUSTOMER_COLUMN = 3;
    private static final int PAYROLL_SHEET_JOB_PAY_COLUMN = 4;

    private static final Map<DayOfWeek, String> PAYROLL_SHEET_DAY_NAME_MAP =
        LBCModel.DAYS.stream().collect(Collectors.toMap(
            day -> day,
            day -> day.equals(DayOfWeek.SATURDAY) ? "WEEKEND WORK" : day.getName().toUpperCase()
        ));

    @Override
    public void writeModelToExcel(LBCModel model, XSSFWorkbook wb) {
        writeWorkerHoursToLBCTab(model, wb);
        writeAmountEarnedToLBCTab(model, wb);
        writeAmountEarnedAndTimeRangeToPayrollTab(model, wb);
        XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
    }

    private void writeWorkerHoursToLBCTab(final LBCModel lbcModel, final XSSFWorkbook workbook) {

        final Sheet lbcSheet = workbook.getSheet(LBC_SHEET_NAME);

        for (LBCDay lbcDay : lbcModel.getLbcDays().values()) {

            final DayOfWeek dayOfWeek = lbcDay.getDayOfWeek();

            final List<String> selectedWorkers = lbcDay.getWorkerSelectionGrid().stream()
                .flatMap(Collection::stream)
                .filter(Pair::getRight)
                .map(Pair::getLeft)
                .collect(Collectors.toList());

            if (!selectedWorkers.isEmpty() && !lbcDay.getBeginTime().isEmpty() && !lbcDay.getEndTime().isEmpty()) {
                selectedWorkers.forEach(selectedWorker -> writeBeginAndEndTimeToWorkerRow(
                    lbcSheet,
                    dayOfWeek.getName(),
                    selectedWorker,
                    lbcDay.getBeginTime(),
                    lbcDay.getEndTime())
                );
            }

            lbcDay.getExceptionData().getEntries().forEach(exceptionEntry -> {

                if (exceptionEntry.getWorker_name().isEmpty()) {
                    return;
                }

                if (exceptionEntry.getTime_begin().isEmpty() || exceptionEntry.getTime_end().isEmpty()) {
                    throw new RuntimeException(String.format(
                        "LBC Exception for worker %s on day %s is missing start or end time",
                        exceptionEntry.getWorker_name(),
                        dayOfWeek.getName()
                    ));
                }

                writeBeginAndEndTimeToWorkerRow(
                    lbcSheet,
                    dayOfWeek.getName(),
                    exceptionEntry.getWorker_name(),
                    exceptionEntry.getTime_begin(),
                    exceptionEntry.getTime_end()
                );
            });
        }
    }

    private void writeBeginAndEndTimeToWorkerRow(
        final Sheet lbcSheet,
        final String dayOfWeek,
        final String workerName,
        final String beginTime,
        final String endTime
    ) {
        final Row workerRow = findLBCWorkerRow(lbcSheet, dayOfWeek, workerName);
        final Pair<String, String> timeRange = TimeMethods.convertTo24HourFormat(beginTime, endTime, TimeWindow.LBC);
        workerRow.getCell(LBC_SHEET_BEGIN_TIME_COLUMN).setCellValue(DateUtil.convertTime(timeRange.getLeft()));
        workerRow.getCell(LBC_SHEET_END_TIME_COLUMN).setCellValue(DateUtil.convertTime(timeRange.getRight()));
    }

    private void writeAmountEarnedToLBCTab(final LBCModel lbcModel, final XSSFWorkbook workbook) {
        final Sheet lbcSheet = workbook.getSheet(LBC_SHEET_NAME);

        final double totalEarned = lbcModel.getLbcDays().values().stream()
            .map(LBCDay::getAmountEarned)
            .filter(StringUtils::isNumeric)
            .mapToDouble(Double::valueOf)
            .sum();

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
    }

    private void writeAmountEarnedAndTimeRangeToPayrollTab(final LBCModel lbcModel, final XSSFWorkbook workbook) {
        final Sheet payrollSheet = workbook.getSheet(PAYROLL_SHEET_NAME);

        lbcModel.getLbcDays().values().forEach(lbcDay -> {
            final Row row = findPayrollLBCRow(payrollSheet, PAYROLL_SHEET_DAY_NAME_MAP.get(lbcDay.getDayOfWeek()));

            final boolean isBeginTimeEmpty = lbcDay.getBeginTime() == null || lbcDay.getBeginTime().isEmpty();
            final boolean isEndTimeEmpty = lbcDay.getEndTime() == null || lbcDay.getEndTime().isEmpty();

            if (isBeginTimeEmpty && !isEndTimeEmpty) {
                throw new RuntimeException(String.format("LBC begin time is empty for day %s", lbcDay.getDayOfWeek().getName()));
            } else if (!isBeginTimeEmpty && isEndTimeEmpty) {
                throw new RuntimeException(String.format("LBC end time is empty for day %s", lbcDay.getDayOfWeek().getName()));
            }

            if (!isBeginTimeEmpty && !isEndTimeEmpty) {
                final Pair<String, String> timeRange = TimeMethods.convertTo24HourFormat(
                    lbcDay.getBeginTime(), lbcDay.getEndTime(), TimeWindow.LBC
                );
                final double hours = TimeMethods.getNumberOfHours(lbcDay.getBeginTime(), lbcDay.getEndTime(), TimeWindow.LBC);
                row.getCell(PAYROLL_SHEET_START_TIME_COLUMN).setCellValue(DateUtil.convertTime(timeRange.getLeft()));
                row.getCell(PAYROLL_SHEET_END_TIME_COLUMN).setCellValue(DateUtil.convertTime(timeRange.getRight()));
                row.getCell(PAYROLL_SHEET_HOURS_COLUMN).setCellValue(hours);
            }

            if (lbcDay.getAmountEarned() != null && !lbcDay.getAmountEarned().isEmpty()) {
                row.getCell(PAYROLL_SHEET_JOB_PAY_COLUMN).setCellValue(Double.parseDouble(lbcDay.getAmountEarned()));
            }
        });
    }

    private Row findLBCWorkerRow(final Sheet sheet, final String dayOfWeek, final String workerName) {

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

        int currentRowNumber = dayOfWeekRowNumber + 1;
        for (; currentRowNumber < 1000; currentRowNumber++) {

            if (ExcelUtil.isCellValueInCollection(
                sheet, currentRowNumber, PAYROLL_SHEET_CUSTOMER_COLUMN, PAYROLL_SHEET_DAY_NAME_MAP.values()
            )) {
                throw createLBCNotFoundException(sheet, dayOfWeek);
            }

            if (ExcelUtil.doesCellValueEqualString(
                sheet, currentRowNumber, PAYROLL_SHEET_CUSTOMER_COLUMN, PAYROLL_SHEET_LBC_CUSTOMER_NAME
            )) {
                return sheet.getRow(currentRowNumber);
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
