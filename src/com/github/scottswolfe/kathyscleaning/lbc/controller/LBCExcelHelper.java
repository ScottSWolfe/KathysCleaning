package com.github.scottswolfe.kathyscleaning.lbc.controller;

import com.github.scottswolfe.kathyscleaning.interfaces.ExcelHelper;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class LBCExcelHelper implements ExcelHelper<LBCModel> {

    @Override
    public void writeModelToExcel(LBCModel model, XSSFWorkbook wb) {
        System.out.println("This is not implemented yet.");
    }
}
