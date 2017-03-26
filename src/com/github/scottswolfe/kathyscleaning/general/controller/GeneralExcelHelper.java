package com.github.scottswolfe.kathyscleaning.general.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.completed.controller.CompletedExcelHelper;
import com.github.scottswolfe.kathyscleaning.completed.model.CompletedModel;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantExcelHelper;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.scheduled.controller.ScheduledExcelHelper;
import com.github.scottswolfe.kathyscleaning.scheduled.model.NW_Data;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendExcelHelper;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;

public class GeneralExcelHelper {

    public static void generateExcelDocument(File newExcelFile) {
        try {
            InputStream input = new FileInputStream(Settings.getExcelTemplateFile());
            XSSFWorkbook wb = new XSSFWorkbook(input);
            
            // Completed Form
            CompletedModel completedModel = (CompletedModel) JsonMethods.loadFromFileJSON(CompletedModel.class, GeneralController.TEMP_SAVE_FILE, Form.COMPLETED.getNum());
            CompletedExcelHelper comHelper = new CompletedExcelHelper();
            comHelper.writeModelToExcel(completedModel, wb);
            
            // Covenant Form
            CovenantModel covModel = (CovenantModel) JsonMethods.loadFromFileJSON(CovenantModel.class, GeneralController.TEMP_SAVE_FILE, Form.COVENANT.getNum()); 
            CovenantExcelHelper covHelper = new CovenantExcelHelper();
            covHelper.writeModelToExcel(covModel, wb);
            
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
                    "Error: Excel document was not created properly.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");

        } 
    }
    
    private static void writeToNewFile(File newExcelFile, XSSFWorkbook wb) {
        Settings.excelFile = newExcelFile; // TODO is this needed?
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(newExcelFile);
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(new JFrame(),
                    "Error: Excel document was not created properly.");
        }
    }
    
}
