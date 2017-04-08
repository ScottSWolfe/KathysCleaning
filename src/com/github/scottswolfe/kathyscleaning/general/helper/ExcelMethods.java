package com.github.scottswolfe.kathyscleaning.general.helper;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralExcelHelper;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;

public class ExcelMethods {

    /**
     * Changes the given excel String to explicitly contain the worked hours
     * instead of having a reference to the cell TODO: not sure why...?
     * 
     * @param s
     * @param hours
     * @return
     */
    public static String changeFormula(String s, Double hours) {
        
        // change double to 3 digits after decimal
        DecimalFormat numberFormat = new DecimalFormat("#.000");
        String insert = numberFormat.format(hours);
        int skip = 0;       
        
        // parse the string and change (capital letter,number) (eg A5) to double
        char[] c = s.toCharArray();
        
        //int count = 0;
        for (int i=1; i<c.length; i++) {
            if (c[i] == '*') {
                break;
            }
            else {
                skip++;
            }
        }
        
        char[] k = new char[ c.length - skip + insert.length() ];
        
        // formuals come in the form: +C6*PAY!$C$7
        // I want to send them out as: +1.362*PAY!$C$7
        
        int shift = 0;
        k[0] = c[0];
        for (int i=1; i<insert.length()+1; i++) {
            k[i] = insert.toCharArray()[i-1];
            shift++;
        }
        for (int i=1+shift; i<k.length; i++) {
            k[i] = c[i-shift + skip];
        }
        return String.valueOf(k);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static void chooseFileAndGenerateExcelDoc(Controller controller) {

        controller.readInputAndWriteToFile(null);
        boolean response = MainWindowListener.askUserIfSaveBeforeClose(controller, false);
        if (response == false) {
            return;
        }

        JOptionPane.showMessageDialog(null, "Now save the new Excel document.");
        
        File file = FileChooserHelper.saveAs(
                    SettingsModel.getExcelSaveLocation(),
                    FileNameHelper.createDatedFileName(
                    SettingsModel.getExcelSaveLocation().getPath(),
                    FileChooserHelper.XLSX),
                    FileChooserHelper.XLSX);
        
        if (file != null) {
            GeneralExcelHelper.generateExcelDocument(file);
            try {
                Desktop dt = Desktop.getDesktop();
                dt.open(file);
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(new JFrame(), "The Excel document could not be opened automatically.");
            }
            return;
        }
        
        controller.initializeForm(
                new GeneralController(controller.getFormType()));
    }
    
}
