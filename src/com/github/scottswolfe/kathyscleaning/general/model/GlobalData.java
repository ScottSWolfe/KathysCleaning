package com.github.scottswolfe.kathyscleaning.general.model;

import com.github.scottswolfe.kathyscleaning.general.helper.ExcelPayTabHelper;
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

    public void initializeData() throws IOException {
        excelWorkerNames = ImmutableList.copyOf(ExcelPayTabHelper.from().getExcelWorkerNames());
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
