package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Calendar;

import javax.swing.JFrame;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.covenant.model.CovenantModel;
import com.github.scottswolfe.kathyscleaning.enums.Form;
import com.github.scottswolfe.kathyscleaning.general.controller.GeneralController;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.view.MainFrame;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.ControllerHelper;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.menu.view.ChooseWeekPanel;
import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class ScheduledControllerHelper 
                        implements ControllerHelper<TabbedPane, Data>{

    @Override
    public Data readViewIntoModel(TabbedPane view) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void writeModelToView(Data model, TabbedPane view) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void saveToFile(CovenantModel model, File file) {
        JsonMethods.saveToFileJSON(model, CovenantModel.class, file, Form.COVENANT.getNum());
    }

    @Override
    public CovenantModel loadFromFile(File file) {
        return (CovenantModel) JsonMethods.loadFromFileJSON(CovenantModel.class, file, Form.COVENANT.getNum());
    }

    @Override
    public void initializeForm(GeneralController<TabbedPane, Data> controller, Calendar date, int mode, int wk) {

        // creating new frame for next week panel and disposing of Weekend panel
        MainFrame<TabbedPane, Data> nwframe = new MainFrame<>(controller);
            
        ChooseWeekPanel cwp = new ChooseWeekPanel(nwframe, ChooseWeekPanel.NEXT_WEEK);
                
        nwframe.add(cwp);
        nwframe.setLocationRelativeTo(null);
        nwframe.pack();
                    
        // set 
        if ( wk == Settings.WEEK_A ) {
            cwp.week_B_rbutton.setSelected(true);
        }
        else if ( wk == Settings.WEEK_B ) {
            cwp.week_A_rbutton.setSelected(true);
        }
        else {
            // do nothing
        }
        nwframe.setVisible(true);
    }

}
