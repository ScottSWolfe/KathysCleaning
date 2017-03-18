package com.github.scottswolfe.kathyscleaning.covenant.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.scottswolfe.kathyscleaning.general.model.WorkTime;

public class CovenantEntry {

    private String worker;
    List<WorkTime> workTimes;
    
    public CovenantEntry() {
        workTimes = new ArrayList<WorkTime>();
    }
    
    /**
     * @return the worker
     */
    public String getWorker() {
        return worker;
    }
    /**
     * @param worker the worker to set
     */
    public void setWorker(String worker) {
        this.worker = worker;
    }
    
    public void addWorkTime(WorkTime times) {
        workTimes.add(times);
    }
    public Iterator<WorkTime> workTimesIterator() {
        return workTimes.iterator();
    }
    public List<WorkTime> getWorkTimes() {
        return workTimes;
    }
    
}
