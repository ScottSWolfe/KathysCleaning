package com.github.scottswolfe.kathyscleaning.utility;


import java.util.List;

public class GridValidator {

    public static GridValidator from() {
        return new GridValidator();
    }

    private GridValidator() {}

    public <T> void validateGrid(final List<List<T>> grid) {
        final int rowCount = grid.size();
        if (rowCount == 0) {
            throw new IllegalArgumentException("Number of rows must be greater than 0.");
        }

        final int columnCount = grid.get(0).size();
        if (columnCount == 0) {
            throw new IllegalArgumentException("Number of columns must be greater than 0.");
        }
        for (List<T> row : grid) {
            if (row.size() != columnCount) {
                throw new IllegalArgumentException("All rows must have the same number of columns.");
            }
        }
    }
}
