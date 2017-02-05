package com.github.scottswolfe.kathyscleaning.scheduled.controller;

import com.github.scottswolfe.kathyscleaning.completed.model.Data;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.interfaces.Controller;
import com.github.scottswolfe.kathyscleaning.persistance.Savable;

public class ScheduledController implements Controller, Savable {

    Data data;
    TabbedPane tp;
    
    public ScheduledController() {
        
    }
    
    
}
