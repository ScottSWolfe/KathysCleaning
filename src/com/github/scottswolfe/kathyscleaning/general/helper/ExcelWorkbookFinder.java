package com.github.scottswolfe.kathyscleaning.general.helper;

import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ExcelWorkbookFinder {

    public static ExcelWorkbookFinder from() {
        return new ExcelWorkbookFinder();
    }

    private ExcelWorkbookFinder() {}

    public Workbook getExcelWorkbook() throws IOException {
        final InputStream input = Files.newInputStream(SettingsModel.getExcelTemplateFile().toPath());
        return new XSSFWorkbook(input);
    }
}
