package com.github.scottswolfe.kathyscleaning.completed.model;


public class ExceptionEntry {

    private String worker_name;
    private String time_begin;
    private String time_end;
    
    public ExceptionEntry(String worker_name, String time_begin, String time_end) {
        this.worker_name = worker_name;
        this.time_begin = time_begin;
        this.time_end = time_end;
    }
    
    /**
     * @return the worker_name
     */
    public String getWorker_name() {
        return worker_name;
    }

    /**
     * @param worker_name the worker_name to set
     */
    public void setWorker_name(String worker_name) {
        this.worker_name = worker_name;
    }

    /**
     * @return the time_begin
     */
    public String getTime_begin() {
        return time_begin;
    }

    /**
     * @param time_begin the time_begin to set
     */
    public void setTime_begin(String time_begin) {
        this.time_begin = time_begin;
    }

    /**
     * @return the time_end
     */
    public String getTime_end() {
        return time_end;
    }

    /**
     * @param time_end the time_end to set
     */
    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
    
}
