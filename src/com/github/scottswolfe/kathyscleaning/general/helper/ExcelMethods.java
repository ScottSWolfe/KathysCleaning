package com.github.scottswolfe.kathyscleaning.general.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralExcelHelper;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener.Action;
import com.github.scottswolfe.kathyscleaning.general.model.SessionModel;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;

public class ExcelMethods {

    /**
     * Changes the given excel String to explicitly contain the worked hours
     * instead of having a reference to the hours worked cell
     *
     * Formula input format:  =+VLOOKUP(O$2,PAY!$B$3:$C$16,2,FALSE)*$C12
     * Formula return format: =+VLOOKUP(O$2,PAY!$B$3:$C$16,2,FALSE)*1.362
     *
     * @param s the formula
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void chooseFileAndGenerateExcelDoc(Controller controller) {

        controller.readInputAndWriteToFile(null);
        boolean response = MainWindowListener.askUserIfSaveBeforeAction(controller, Action.CLOSE, false);
        if (response == false) {
            return;
        }

        JOptionPane.showMessageDialog(null, "Now save the new Excel document.");

        File file = FileChooserHelper.saveAs(
                    SettingsModel.getExcelSaveLocation(),
                    getDefaultSaveFileName(),
                    FileChooserHelper.XLSX);

        if (file != null) {
            GeneralExcelHelper.generateExcelDocument(file);
            try {
                Desktop dt = Desktop.getDesktop();
                dt.open(file);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(new JFrame(), "The Excel document could not be opened automatically.");
            }
            System.exit(0);
        }

        controller.initializeForm(
                new GeneralController(controller.getFormType()));
    }

    private static String getDefaultSaveFileName() {
        if (SessionModel.isSaveFileChosen()) {
            String fileName = SessionModel.getSaveFile().getName();
            return FilenameUtils.removeExtension(fileName);
        } else {
            return FileNameHelper.createDatedFileName(
                    SettingsModel.getExcelSaveLocation().getPath(),
                    FileChooserHelper.XLSX);
        }
    }

}
