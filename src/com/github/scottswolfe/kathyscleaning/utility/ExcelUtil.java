package com.github.scottswolfe.kathyscleaning.utility;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;
import java.util.Collections;

public class ExcelUtil {

    public static boolean doesCellValueEqualString(
        final Sheet sheet,
        int rowNumber,
        int columnNumber,
        final String string
    ) {
        return isCellValueInCollection(sheet, rowNumber, columnNumber, Collections.singletonList(string));
    }

    public static boolean isCellValueInCollection(
        final Sheet sheet,
        int rowNumber,
        int columnNumber,
        Collection<String> collection
    ) {
        final Row row = sheet.getRow(rowNumber);
        if (row == null) {
            return false;
        }

        final Cell cell = row.getCell(columnNumber);
        if (cell == null) {
            return false;
        }

        String cellValue;
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            cellValue = cell.getStringCellValue();
        } else if (
            cell.getCellType() == Cell.CELL_TYPE_FORMULA
                && cell.getCachedFormulaResultType() == Cell.CELL_TYPE_STRING
        ) {
            cellValue = cell.getRichStringCellValue().getString();
        } else {
            return false;
        }

        return collection.contains(cellValue);
    }
}
