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
import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.covenant.controller.CovenantExcelHelper;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;
import com.github.scottswolfe.kathyscleaning.weekend.controller.WeekendExcelHelper;
import com.github.scottswolfe.kathyscleaning.weekend.model.WeekendModel;

public class GeneralExcelHelper {

    public static void generateExcelDocument(File newExcelFile) {
        try {
            InputStream input = new FileInputStream(Settings.getExcelTemplateFile());
            XSSFWorkbook wb = new XSSFWorkbook(input);
            
            // Completed Form
            Data data = (Data) JsonMethods.loadFromFileJSON(Data.class, Settings.saveFile, Form.COMPLETED.getNum());
            CompletedExcelHelper comHelper = new CompletedExcelHelper();
            comHelper.writeModelToExcel(data, wb);
            
            // Covenant Form
            CovenantModel covModel = (CovenantModel) JsonMethods.loadFromFileJSON(CovenantModel.class, Settings.saveFile, Form.COVENANT.getNum()); 
            CovenantExcelHelper covHelper = new CovenantExcelHelper();
            covHelper.writeModelToExcel(covModel, wb);
            
            // Weekend Form
            WeekendModel weekendModel = (WeekendModel) JsonMethods.loadFromFileJSON(WeekendModel.class, Settings.saveFile, Form.WEEKEND.getNum()); 
            WeekendExcelHelper weekendHelper = new WeekendExcelHelper();
            weekendHelper.writeModelToExcel(weekendModel, wb);
            
            /* 
            TODO scheduled
            CovenantExcelHelper covHelper = new CovenantExcelHelper();
            covHelper.writeModelToExcel(covModel, newFileName);
            */
            
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
