package com.github.scottswolfe.kathyscleaning.enums;

public enum SaveType {

    COMPLETED(0),
    COVENANT(1),
    WEEKEND(2),
    SCHEDULED(3),
    SESSION(4),
    LBC(5);

    private final int lineNumber;

    SaveType(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
