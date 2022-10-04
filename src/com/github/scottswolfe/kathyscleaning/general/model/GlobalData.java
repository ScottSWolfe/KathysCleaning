package com.github.scottswolfe.kathyscleaning.general.model;

import com.github.scottswolfe.kathyscleaning.general.helper.ExcelPayTabHelper;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public void initializeData() {
        try {
            excelWorkerNames = ImmutableList.copyOf(ExcelPayTabHelper.from().getExcelWorkerNames());
        } catch (IOException e) {
            StaticMethods.shareErrorMessage(
                "Could not get worker names from the Excel sheet. Make sure the Excel"
                    + "\n"
                    + "template is selected in the Settings menu and that the Excel sheet is"
                    + "\n"
                    + "correctly formatted. Then restart the program."
            );
            e.printStackTrace();
            excelWorkerNames = ImmutableList.of();
        }
    }

    public List<String> getDefaultWorkerNames() {
        return getWorkerNamesFromExcelTemplate().stream()
            .distinct()
            .sorted()
            .collect(Collectors.toList());
    }

    public List<String> getWorkerNamesFromExcelTemplate() {
        if (excelWorkerNames == null) {
            throw createIllegalStateException();
        }
        return excelWorkerNames;
    }

    private IllegalStateException createIllegalStateException() {
        return new IllegalStateException("GlobalData must be initialized before it can be used.");
    }
}
