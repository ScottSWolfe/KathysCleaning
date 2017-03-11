package com.github.scottswolfe.kathyscleaning.weekend.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.weekend.view.WeekendPanel;

public class WeekendController implements Controller {

    @Override
    public void readInputAndWriteToFile() {
        // TODO Auto-generated method stub

    }

    @Override
    public void readFileAndWriteToView() {
        // TODO Auto-generated method stub

    }
    
    /**
     * Initializes and launches a frame with a Weekend panel.
     */
    public void initializeWeekendPanelFrame(JFrame oldFrame, 
                            Calendar date, int mode, int wk) {
        
        WeekendPanel wp = new WeekendPanel(date, mode, wk);
        WeekendController controller = new WeekendController();
        // TODO set controller's view and model

        JFrame weekendFrame = new MainFrame(controller);
        weekendFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        weekendFrame.setResizable(false);
        weekendFrame.addWindowListener( new MainWindowListener() );
        
        wp.setFrame(weekendFrame);          
        
        weekendFrame.add(wp);
        weekendFrame.pack();
        weekendFrame.setLocationRelativeTo( null );
                    
        oldFrame.setVisible( false );
        oldFrame.dispose();
        
        // populate data from save file
        if ( wk == Settings.WEEK_A ) {
            wp.weekA_button.setSelected(true);
            ActionEvent event = new ActionEvent(this, 0, "");
            ActionListener[] al = wp.weekA_button.getActionListeners();
            al[0].actionPerformed(event);
        }
        else if (wk == Settings.WEEK_B) {
            wp.weekB_button.setSelected(true);
            ActionEvent event = new ActionEvent(this, 0, "");
            ActionListener[] al = wp.weekB_button.getActionListeners();
            al[0].actionPerformed(event);
        }
        else {
            // do nothing
        }
        
        wp.weekA_button.setEnabled(false);
        wp.weekB_button.setEnabled(false);
        wp.neither_button.setEnabled(false);
         
        weekendFrame.setVisible(true);
    }

    
    
/* GETTERS/SETTERS ========================================================== */

    @Override
    public void setView(Object obj) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setModel(Object obj) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getModel() {
        // TODO Auto-generated method stub
        return null;
    }
    


}
