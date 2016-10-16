package com.github.scottswolfe.kathyscleaning.covenant.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.scottswolfe.kathyscleaning.completed.controller.SubmitWeekListener;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.covenant.view.CovenantPanel;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.StaticMethods;
import com.github.scottswolfe.kathyscleaning.utility.TimeMethods;
import com.github.scottswolfe.kathyscleaning.weekend.WeekendPanel;

/**
 * Controller that links CovenantModel and CovenantPanel. 
 */
public class CovenantController {

    
    
/* FIELDS =================================================================== */
    
    /**
     * Model that this controller controls.
     */
    CovenantModel covModel;
    
    /**
     * Panel that this controller controls.
     */
    CovenantPanel covPanel;
    
    
    

/* FIELDS =================================================================== */
    
    public CovenantController(CovenantPanel panel) {
        covPanel = panel;
    }

    
    
    
/* LISTENERS ================================================================ */
    
    /**
     * Listener for the Edit button.
     */
    public class EditListener implements ActionListener {
        
        public void actionPerformed ( ActionEvent e ) {
           
            covPanel.getCovFrame().setEnabled(false);
            EditWorkersController.initializePanelFrame(covPanel);
        }
    }
    
    
    /**
     * Listener for the Submit button.
     */
    public class SubmitListener implements ActionListener {
        
        public void actionPerformed (ActionEvent e) {
            
            int a = StaticMethods.confirmSubmitWeek();
            if (a==0) {
                return;
            }
            
            // initializing variables
            
            // in case any names are blank
            String[] names = new String[CovenantPanel.ROWS];
            int count = 0;
            for (int i=0; i<CovenantPanel.ROWS; i++) {
                if (covPanel.getNameLabels()[i] != null &&
                        !covPanel.getNameLabels()[i].getText().equals("")) {
                    names[count] = covPanel.getNameLabels()[i].getText();
                    count++;
                }
            }
            
            String[] worker = new String[count];
            for( int i=0; i<count; i++ ) {
                worker[i] = names[i];
            }
            String[] days = {"Monday", "Tuesday", "Wednesday",
                    "Thursday", "Friday"};
            
            
            // writing data to excel document
            try{
            File f = SubmitWeekListener.new_save;
            InputStream inp = new FileInputStream(f);
            XSSFWorkbook wb = new XSSFWorkbook(inp);
            Sheet sheet = wb.getSheet("COVENANT");

            Row row;
            Cell cell;
            boolean day_complete;
            boolean missing_employee = false;
            String s1;
            String s2;
            
            // iterate through each worker on the Covenant panel
            for (int i=0; i<worker.length; i++) {
                                
                row = sheet.getRow(0);
                    
                    // iterate through each day of the week
                    for (int j=0; j<5; j++) {
                                                
                        boolean day_match = false;
                        while (day_match == false) {
                            
                            // making sure the row and columns are not null
                            if ( row != null && row.getCell(0) != null ) {
                                                                
                                cell = row.getCell(0);
                                                                
                                // checking for the cell that matches the current day of week
                                if ( cell.getStringCellValue().equals(days[j]) ) {
                                    
                                    day_match = true;
                                                                        
                                    // iterate through list of workers on excel sheet
                                    // to find current worker from Covenant panel
                                    day_complete = false;
                                    while ( day_complete == false ) {
                                                                                
                                        // checking that the row and cell are not null
                                        if ( row != null && row.getCell(0) != null ) {
                                                                                        
                                            // checking for the cell that matches the current worker
                                            if ( row.getCell(0).getStringCellValue().equals(worker[i]) ) {
                                                                                            
                                                s1 = TimeMethods.convertFormat(covPanel.getBeginTimeTextfield()[i][j], TimeMethods.COVENANT_TIME);
                                                s2 = TimeMethods.convertFormat(covPanel.getEndTimeTextfield()[i][j], TimeMethods.COVENANT_TIME);
                                                    
                                                row.getCell(1).setCellValue( DateUtil.convertTime(s1) );
                                                row.getCell(2).setCellValue( DateUtil.convertTime(s2) );
                                                                                                        
                                                day_complete = true;
                                                break;
                                            }
                                            // or if the next day comes first; the worker is missing from the excel sheet
                                            else if (j<4 && row.getCell(0).getStringCellValue().equals(days[j+1]) ) {
                                                
                                                String message = "Error: The selected employee " + worker[i] +
                                                        " cannot be found on the Excel Document.\nYou will need " +
                                                        "to enter the Employee's payroll data manually on the Excel Sheet.\n" +
                                                        "Please correct the employee's name so that it matches the Excel Sheet.";
                                                        
                                                JOptionPane.showMessageDialog( new JFrame(), message,
                                                        null, JOptionPane.ERROR_MESSAGE);
                                                
                                                row = sheet.getRow( row.getRowNum() - 1 );
                                                missing_employee = true;
                                            }
                                            
                                        if (missing_employee == true) {
                                            break;
                                        }
                                    }
                                    row = sheet.getRow( row.getRowNum() + 1 );
                                        
                                        
                                }
                                row = sheet.getRow( row.getRowNum() + 1 );
                                        
                            }
                            row = sheet.getRow( row.getRowNum() + 1 );  
                        }
                        
                        
                    }
                    if (missing_employee == true) {
                        missing_employee = false;
                        break;
                    }
                        
                }                
                
            }
            
            // adding in amount earned
            sheet = wb.getSheet("PAYROLL");
            int counter = 0;
            int row_num = 0;
            
            // for each day
            for (int i=0; i<days.length; i++) {              
                
                while ( counter < 150 ) {
                    
                    row = sheet.getRow( row_num );
                                        
                    if ( row != null &&
                             row.getCell(9) != null &&
                             row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING &&
                             row.getCell(9).getStringCellValue().equalsIgnoreCase(days[i]) ) {
                        
                        row_num++;
                        break;
                        
                    }
                    
                    row_num++;
                    counter++;
                }
                
                
                counter = 0;
                while ( counter < 11 ) {
                    
                    row = sheet.getRow( row_num );
                    
                    if ( row != null &&
                         row.getCell(2) != null &&
                         row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING &&
                         row.getCell(2).getStringCellValue().equals("COV SCHL") ) {
                        
                        if (covPanel.getEarnedTextfields()[i].getText().length() > 0 ) {
                            row.getCell(4).setCellValue( Double.parseDouble(covPanel.getEarnedTextfields()[i].getText()) );
                        }
                        else {
                            row.getCell(4).setCellValue( 0 );
                        }
                        
                        row_num++;
                        counter = 0;
                        break;
                        
                    }
                    else {
                        counter++;
                        row_num++;
                    }
                    
                }
            
            }
            
            // putting in total covenant earned on Covenant tab
            sheet = wb.getSheet("COVENANT");
            counter = 0;
            row_num = 0;
            while ( counter < 150 ) {
                
                row = sheet.getRow( row_num );
                double money = 0;
                
                if ( row != null &&
                     row.getCell(1) != null &&
                     row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING &&
                     row.getCell(1).getStringCellValue().equals("KATHY INCOME") ) {
                    
                    for (int i=0; i<days.length; i++) {
                        
                        if (covPanel.getEarnedTextfields()[i].getText().length() > 0 ) {
                            money = money + Double.parseDouble(covPanel.getEarnedTextfields()[i].getText() );
                        }
                        else {
                            
                        }
                        
                    }
                    
                    row.getCell(4).setCellValue( money );
                    
                    row_num++;
                    break;
                    
                }
                else {
                    counter++;
                    row_num++;
                }
                
            }
            
            
            
            XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
            
            
            OutputStream out = new FileOutputStream(f);
            wb.write( out );
            
            wb.close();
            inp.close();
            out.close();
            }
            catch (Exception e1) {
                String message = "There was an error in copying the Covenant Data onto the Excel Document.\n" +
                                    "You will need to enter the data manually into the Excel Sheet.";
                JOptionPane.showMessageDialog( new JFrame(), message, null, JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
            
            
            
            // saving chosen workers into CovenantWorkerSaveFile
            try {

                FileWriter fw = new FileWriter(Settings.COV_WORKER_SAVE.getPath() );
                BufferedWriter bw = new BufferedWriter( fw );
                
                for (int i=0; i<CovenantPanel.ROWS; i++) {
                    bw.write(covPanel.getNameLabels()[i].getText());
                    bw.newLine();
                }
                
                boolean match;
                if (covModel.getDwd().getWorkers() != null) {
                for (int i=0; i<covModel.getDwd().size(); i++) {
                    
                    match = false;
                    
                    for (int j=0; j<covPanel.getNameLabels().length; j++) {
                    
                        if (covModel.getDwd().get(i).equals(covPanel.getNameLabels()[j].getText())) {
                            match = true;
                            break;
                        }
                        
                    }
                    
                    if (match == false) {
                        bw.write(covModel.getDwd().get(i));
                        bw.newLine();
                    }
                    
                }}
                
                bw.close();
                
            }
            catch(Exception e2) {
                e2.printStackTrace();
            }
            
            
            // Saving amount earned into save file
            try {

                FileWriter fw = new FileWriter(
                        Settings.COVENANT_EARNED_SAVE_FILE);
                BufferedWriter bw = new BufferedWriter( fw );
                
                for (int i = 0; i < days.length; i++) {
                    
                    if (covPanel.getEarnedTextfields()[i].getText().length() > 0) {
                        bw.write(covPanel.getEarnedTextfields()[i].getText() );
                        bw.newLine();
                    }
                    else {
                        bw.write( 0 );
                        bw.newLine();
                    }
                
                }
                
                bw.close();
                
            }
            catch(Exception e2) {
                e2.printStackTrace();
            }
            
            
            // creating new frame for next week panel and disposing of Covenant panel
            JFrame nwframe = new JFrame();
            nwframe.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            nwframe.setResizable(false);
            nwframe.addWindowListener(new MainWindowListener());
            
            WeekendPanel wp = new WeekendPanel(nwframe,covModel.getDate(),
                    covModel.getMode(), covModel.getWk());
            
            nwframe.add(wp);
            nwframe.pack();
            nwframe.setLocationRelativeTo(null);
            
            
            // populate data from save file
            if (covModel.getWk() == Settings.WEEK_A) {
                wp.weekA_button.setSelected(true);
                ActionEvent event = new ActionEvent(this, 0, "");
                ActionListener[] al = wp.weekA_button.getActionListeners();
                al[0].actionPerformed(event);
            }
            else if (covModel.getWk() == Settings.WEEK_B) {
                wp.weekB_button.setSelected(true);
                ActionEvent event = new ActionEvent(this, 0, "");
                ActionListener[] al = wp.weekB_button.getActionListeners();
                al[0].actionPerformed(event);
                            }
            else {
                // do nothing
            }
            
            
            covPanel.getCovFrame().setVisible(false);
            covPanel.getCovFrame().dispose();
            
            nwframe.setVisible(true);
            
        }
        
    }



    
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * @return the covModel
     */
    public CovenantModel getCovModel() {
        return covModel;
    }
    

    /**
     * @param covModel the covModel to set
     */
    public void setCovModel(CovenantModel covModel) {
        this.covModel = covModel;
    }


    /**
     * @return the covPanel
     */
    public CovenantPanel getCovPanel() {
        return covPanel;
    }


    /**
     * @param covPanel the covPanel to set
     */
    public void setCovPanel(CovenantPanel covPanel) {
        this.covPanel = covPanel;
    }
    
}
