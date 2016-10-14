package com.github.scottswolfe.kathyscleaning.general.model;

import java.math.BigDecimal;
import java.time.LocalTime;

/**
 * @author Scott
 * Edited 9/23/2016
 * 
 * Represents a house that was cleaned previously. 
 */
public class CompletedHouse extends CompletedCleaning {
    
    public CompletedHouse(String name) {
        super(name);
    }

    /**
     * The amount of money earned by cleaning this house
     */
    private BigDecimal price;
    
    /**
     * The starting time for cleaning this house
     */
    private LocalTime beginTime;
    
    /**
     * The ending time for cleaning this house
     */
    private LocalTime endTime;
    
    /*
     * To be implemented...
     * 
     * private Worker[] workers;
     */
    
    //public SubmitHouse() {
     //   super();
    //}

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setBeginTime(LocalTime beginTime) {
        this.beginTime = beginTime;
    }
    
    public LocalTime getBeginTime() {
        return beginTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    
    
}
