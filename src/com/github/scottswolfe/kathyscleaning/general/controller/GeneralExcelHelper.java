package com.github.scottswolfe.kathyscleaning.general.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.scottswolfe.kathyscleaning.lbc.controller.LBCExcelHelper;
import com.github.scottswolfe.kathyscleaning.lbc.model.LBCModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedExcelHelper;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantExcelHelper;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.menu.model.SettingsModel;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledExcelHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendExcelHelper;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;

public class GeneralExcelHelper {

    public static void generateExcelDocument(File newExcelFile) {
        try {
            InputStream input = new FileInputStream(SettingsModel.getExcelTemplateFile());
            XSSFWorkbook wb = new XSSFWorkbook(input);

            // Completed Form
            CompletedModel completedModel = (CompletedModel) JsonMethods.loadFromFileJSON(CompletedModel.class, GeneralController.TEMP_SAVE_FILE, Form.COMPLETED.getNum());
            CompletedExcelHelper comHelper = new CompletedExcelHelper();
            comHelper.writeModelToExcel(completedModel, wb);

            // Covenant Form
            CovenantModel covModel = (CovenantModel) JsonMethods.loadFromFileJSON(CovenantModel.class, GeneralController.TEMP_SAVE_FILE, Form.COVENANT.getNum());
            CovenantExcelHelper covHelper = new CovenantExcelHelper();
            covHelper.writeModelToExcel(covModel, wb);

            // LBC Form
            LBCModel lbcModel = (LBCModel) JsonMethods.loadFromFileJSON(LBCModel.class, GeneralController.TEMP_SAVE_FILE, Form.LBC.getNum());
            LBCExcelHelper lbcHelper = new LBCExcelHelper();
            lbcHelper.writeModelToExcel(lbcModel, wb);

            // Weekend Form
            WeekendModel weekendModel = (WeekendModel) JsonMethods.loadFromFileJSON(WeekendModel.class, GeneralController.TEMP_SAVE_FILE, Form.WEEKEND.getNum());
            WeekendExcelHelper weekendHelper = new WeekendExcelHelper();
            weekendHelper.writeModelToExcel(weekendModel, wb);

            // Scheduled Form
            NW_Data nwData = (NW_Data) JsonMethods.loadFromFileJSON(NW_Data.class, GeneralController.TEMP_SAVE_FILE, Form.SCHEDULED.getNum());
            ScheduledExcelHelper scheduledHelper = new ScheduledExcelHelper();
            scheduledHelper.writeModelToExcel(nwData, wb);

            writeToNewFile(newExcelFile, wb);
            wb.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel template could not be found.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    new JFrame(),
                    "Unexpected Error :( You can congratulate Scott on how much"
                    + " better of a programer he is now than when he wrote this"
                    + " code. Also you can tell him that the error is "
                    + e.getClass().getName()
                    + " and that the error message is '"
                    + e.getMessage()
                    + "'"
            );
            throw e;
        }
    }

    private static void writeToNewFile(File newExcelFile, XSSFWorkbook wb) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(newExcelFile);
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");
            if (e.getMessage().contains("it is being used by another process")) {
                JOptionPane.showMessageDialog(new JFrame(),
                        "The Excel document must be closed in order to be saved");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");
        }
    }

}
