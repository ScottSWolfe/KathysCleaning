package com.github.scottswolfe.kathyscleaning.enums;

public enum Form {
    COMPLETED(0),
    COVENANT(1),
    WEEKEND(2),
    SCHEDULED(3);
    
    private int num;
    
    private Form(int num) {
        this.num = num;
    }
    
    public int getNum() {
        return num;
    }
}
