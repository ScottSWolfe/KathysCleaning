package com.github.scottswolfe.kathyscleaning.covenant.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.model.WorkerList;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

/**
 * Model for Covenant Academy data and workers. 
 */
public class CovenantModel {
    
    
/* CONSTANTS ================================================================ */
    

    
    
    
/* FIELDS =================================================================== */
    
    List<CovenantEntry> covenantEntries;
    List<Double> amountsEarned;
    
    /**
     * Container for the default workers.
     */
    private WorkerList dwd;
    
    /**
     * Monday's date.
     */
    private Calendar date;
    
    /**
     * Designates whether in submit new data mode or in edit default autofill
     * data mode
     */
    private int mode;
    
    /**
     * Designates whether week A, B, or Neither
     */
    private int wk;

    
    
    public CovenantModel(WorkerList dwd2,
            Calendar date, int mode, int wk) {
    
        this.dwd = dwd2;
        this.date = date;
        this.mode = mode;
        this.wk = wk;
        
        this.dwd = new WorkerList(Settings.COV_WORKER_SAVE);
    }
    
    public CovenantModel() {
        covenantEntries = new ArrayList<CovenantEntry>();
        amountsEarned = new ArrayList<Double>();
    }

    
/* GETTERS/SETTERS ========================================================== */

    public void addEntry(CovenantEntry entry) {
        covenantEntries.add(entry);
    }
    
    public void addAmountEarned(Double amount) {
        amountsEarned.add(amount);
    }
    
    public Iterator<CovenantEntry> entryIterator() {
        return covenantEntries.iterator();
    }
    
    public List<Double> getAmountsEarned() {
        return amountsEarned;
    }
    
    public List<CovenantEntry> getEntries() {
        return covenantEntries;
    }
        
    
    /**
     * @return the dwd
     */
    public WorkerList getDwd() {
        return dwd;
    }

    /**
     * @param dwd the dwd to set
     */
    public void setDwd(WorkerList dwd) {
        this.dwd = dwd;
    }

    /**
     * @return the date
     */
    public Calendar getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Calendar date) {
        this.date = date;
    }

    /**
     * @return the mode
     */
    public int getMode() {
        return mode;
    }

    /**
     * @param mode the mode to set
     */
    public void setMode(int mode) {
        this.mode = mode;
    }

    /**
     * @return the wk
     */
    public int getWk() {
        return wk;
    }

    /**
     * @param wk the wk to set
     */
    public void setWk(int wk) {
        this.wk = wk;
    }
    
    

}
