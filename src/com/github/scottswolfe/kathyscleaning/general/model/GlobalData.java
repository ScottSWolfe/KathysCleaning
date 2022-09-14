package com.github.scottswolfe.kathyscleaning.general.model;

import com.github.scottswolfe.kathyscleaning.general.helper.ExcelPayTabHelper;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.List;

public class GlobalData {

    private static GlobalData globalDataInstance;

    private ImmutableList<String> excelWorkerNames;

    public static GlobalData getInstance() {
        if (globalDataInstance == null) {
            globalDataInstance = new GlobalData();
        }
        return globalDataInstance;
    }

    private GlobalData() {}

    public List<String> getExcelWorkerNames() throws IOException {
        if (excelWorkerNames == null) {
            excelWorkerNames = ImmutableList.copyOf(ExcelPayTabHelper.from().getExcelWorkerNames());
        }
        return excelWorkerNames;
    }
}
