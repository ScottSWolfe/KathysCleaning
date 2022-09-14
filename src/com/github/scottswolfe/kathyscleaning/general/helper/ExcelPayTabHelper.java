package com.github.scottswolfe.kathyscleaning.general.helper;

import com.github.scottswolfe.kathyscleaning.utility.ExcelUtil;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelPayTabHelper {

    private static final String PAY_TAB_NAME = "PAY";
    private static final String WORKER_NAME_TABLE_HEADER = "EMPLOYEE";

    private static final int WORKER_NAME_COLUMN = 1;
    private static final int MAX_WORKER_COUNT = 25;

    private final ExcelWorkbookFinder excelWorkbookFinder;

    public static ExcelPayTabHelper from() {
        return new ExcelPayTabHelper();
    }

    private ExcelPayTabHelper() {
        excelWorkbookFinder = ExcelWorkbookFinder.from();
    }

    public List<String> getExcelWorkerNames() throws IOException {
        final Sheet paySheet = excelWorkbookFinder.getExcelWorkbook().getSheet(PAY_TAB_NAME);

        final List<String> workerNames = new ArrayList<>();

        try {
            final int workerNameStartRow = ExcelUtil.findRow(paySheet, WORKER_NAME_TABLE_HEADER, WORKER_NAME_COLUMN) + 1;
            int currentRow = workerNameStartRow;
            while (currentRow < workerNameStartRow + MAX_WORKER_COUNT) {
                final String workerName = paySheet.getRow(currentRow).getCell(WORKER_NAME_COLUMN).getStringCellValue();
                if (!workerName.isEmpty()) {
                    workerNames.add(workerName);
                }
                currentRow++;
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new RuntimeException(
                "Unable to read worker names from PAY tab on excel template."
                    + "\n"
                    + "Perhaps the template was changed incorrectly.",
                e
            );
        }

        return workerNames;
    }
}
