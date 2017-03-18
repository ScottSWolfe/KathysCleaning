package com.github.scottswolfe.kathyscleaning.weekend.model;

public class WeekendEntry {

    private boolean workedIsChecked;
    private String customer;
    private Double amountReceived;
    private String employee;
    private Double amountPaid;
    
    
    
    /**
     * @return the workedIsChecked
     */
    public boolean isWorkedIsChecked() {
        return workedIsChecked;
    }
    /**
     * @param workedIsChecked the workedIsChecked to set
     */
    public void setWorkedIsChecked(boolean workedIsChecked) {
        this.workedIsChecked = workedIsChecked;
    }
    /**
     * @return the customer
     */
    public String getCustomer() {
        return customer;
    }
    /**
     * @param customer the customer to set
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }
    /**
     * @return the amountReceived
     */
    public Double getAmountReceived() {
        return amountReceived;
    }
    /**
     * @param amountReceived the amountReceived to set
     */
    public void setAmountReceived(Double amountReceived) {
        this.amountReceived = amountReceived;
    }
    /**
     * @return the employee
     */
    public String getEmployee() {
        return employee;
    }
    /**
     * @param employee the employee to set
     */
    public void setEmployee(String employee) {
        this.employee = employee;
    }
    /**
     * @return the amountPaid
     */
    public Double getAmountPaid() {
        return amountPaid;
    }
    /**
     * @param amountPaid the amountPaid to set
     */
    public void setAmountPaid(Double amountPaid) {
        this.amountPaid = amountPaid;
    }
    
}
