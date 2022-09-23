package com.github.scottswolfe.kathyscleaning.general.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.controller.ApplicationCoordinator;
import com.github.scottswolfe.kathyscleaning.utility.SaveFileManager;
import org.apache.commons.io.FilenameUtils;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralExcelHelper;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;

public class ExcelMethods {

    private static final SaveFileManager saveFileManager = SaveFileManager.from();

    /**
     * Changes the given excel String to explicitly contain the worked hours
     * instead of having a reference to the hours worked cell
     *
     * Formula input format:  =+VLOOKUP(O$2,PAY!$B$3:$C$16,2,FALSE)*$C12
     * Formula return format: =+VLOOKUP(O$2,PAY!$B$3:$C$16,2,FALSE)*1.362
     *
     * @param formula the formula
     * @param hours the number of hours worked
     * @return the adjusted formula as a String
     */
    public static String insertHoursWorkedInPlaceOfCellReference(String formula, Double hours) {
        String string_to_add = doubleToStringWith3DigitsAfterDecimal(hours);
        String[] formula_pieces = formula.split("[*]");
        return formula_pieces[0] + "*" + string_to_add;
    }

    private static String doubleToStringWith3DigitsAfterDecimal(Double d) {
        DecimalFormat numberFormat = new DecimalFormat("#.000");
        return numberFormat.format(d);
    }

    public static void chooseFileAndGenerateExcelDoc() {

        final boolean shouldCompleteAction;
        if (saveFileManager.isCurrentFileDefaultTemplate()) {
            JOptionPane.showMessageDialog(null, "First, save as a new file.");
            shouldCompleteAction = ApplicationCoordinator.getInstance().saveAsNew();
        } else {
            shouldCompleteAction = ApplicationCoordinator.getInstance().askIfSaveBeforeClose();
        }
        if (!shouldCompleteAction) {
            return;
        }

        JOptionPane.showMessageDialog(null, "Choose the Excel document to create.");

        File file = FileChooserHelper.saveAs(
            SettingsModel.getExcelSaveLocation(),
            getDefaultSaveFileName(),
            FileChooserHelper.XLSX
        );

        if (file != null) {

            JOptionPane.showMessageDialog(null, "Now generating the Excel document...");
            ApplicationCoordinator.getInstance().hideCurrentWindow();

            try {
                GeneralExcelHelper.generateExcelDocument(file);
            } catch (Exception e) {
                ApplicationCoordinator.getInstance().endApplicationDueToException(
                    "Error when generating the Excel document:", e
                );
            }

            try {
                Desktop dt = Desktop.getDesktop();
                dt.open(file);
            } catch (IOException e) {
                ApplicationCoordinator.getInstance().endApplicationDueToException(
                    "The Excel document could not be opened automatically.", e
                );
            }

            ApplicationCoordinator.getInstance().endApplication();
        }
    }

    private static String getDefaultSaveFileName() {
        if (SessionModel.isSaveFileChosen()) {
            final String fileName = SessionModel.getSaveFile().getName();
            return FileNameHelper.addCopyNumberIfNeeded(
                SettingsModel.getExcelSaveLocation().getPath(),
                FilenameUtils.removeExtension(fileName),
                FileChooserHelper.XLSX
            );
        } else {
            return FileNameHelper.createDatedFileName(
                SettingsModel.getExcelSaveLocation().getPath(),
                FileChooserHelper.XLSX
            );
        }
    }
}
