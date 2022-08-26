package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;

import javax.swing.JComponent;
import java.util.Collections;
import java.util.List;

public class FocusableConnector {

    private static final FocusableConnector focusableConnector = new FocusableConnector();

    public static FocusableConnector from() {
        return focusableConnector;
    }

    private FocusableConnector() {}

    public void connect(final FocusableCollection focusableCollection) {
        final List<List<? extends JComponent>> components = focusableCollection.getComponentsAsGrid();
        validateGrid(components);

        final int rowCount = components.size();
        final int columnCount = components.get(0).size();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                connectComponentsInDirection(components, row, column, Direction.LEFT);
                connectComponentsInDirection(components, row, column, Direction.RIGHT);
                connectComponentsInDirection(components, row, column, Direction.ABOVE);
                connectComponentsInDirection(components, row, column, Direction.BELOW);
            }
        }
    }

    private <T extends JComponent> void connectComponentsInDirection(
        final List<List<? extends JComponent>> components,
        final int row,
        final int column,
        final Direction direction
    ) {
        final int rowCount = components.size();
        final int columnCount = components.get(0).size();

        final JComponent component = components.get(row).get(column);
        if (component == null) {
            return;
        }

        final List<? extends JComponent> componentsThatTransferFocus = getComponentsOnEdge(component, direction);

        List<? extends JComponent> componentsThatReceiveFocus = Collections.emptyList();
        switch (direction) {
            case LEFT:
                if (column > 0) {
                    componentsThatReceiveFocus = getComponentsOnEdge(components.get(row).get(column - 1), Direction.RIGHT);
                }
                break;
            case RIGHT:
                if (column < columnCount - 1) {
                    componentsThatReceiveFocus = getComponentsOnEdge(components.get(row).get(column + 1), Direction.LEFT);
                }
                break;
            case ABOVE:
                if (row > 0) {
                    componentsThatReceiveFocus = getComponentsOnEdge(components.get(row - 1).get(column), Direction.BELOW);
                }
                break;
            case BELOW:
                if (row < rowCount - 1) {
                    componentsThatReceiveFocus = getComponentsOnEdge(components.get(row + 1).get(column), Direction.ABOVE);
                }
                break;
            default:
                throw new RuntimeException("Unexpected direction: " + direction);
        }

        connectComponents(componentsThatTransferFocus, componentsThatReceiveFocus, direction);
    }

    private void connectComponents(
        final List<? extends JComponent> componentsThatTransferFocus,
        final List<? extends JComponent> componentsThatReceiveFocus,
        final Direction direction
    ) {
        if (componentsThatTransferFocus.isEmpty() || componentsThatReceiveFocus.isEmpty()) {
            return;
        }

        final double offsetRate = (double) componentsThatReceiveFocus.size() / (double) componentsThatTransferFocus.size();

        for (int index = 0; index < componentsThatTransferFocus.size(); index++) {
            final JComponent focusSourceComponent = componentsThatTransferFocus.get(index);
            final JComponent focusTargetComponent = componentsThatReceiveFocus.get((int) (index * offsetRate));

            final KeyboardFocusListener keyboardFocusListener = KeyboardFocusListener.from(
                focusSourceComponent,
                direction == Direction.LEFT ? focusTargetComponent : null,
                direction == Direction.RIGHT ? focusTargetComponent : null,
                direction == Direction.ABOVE ? focusTargetComponent : null,
                direction == Direction.BELOW ? focusTargetComponent : null,
                null
            );

            focusSourceComponent.addFocusListener(keyboardFocusListener);
        }
    }

    private void validateGrid(final List<List<? extends JComponent>> grid) {
        final int rowCount = grid.size();
        if (rowCount == 0) {
            throw new IllegalArgumentException("Number of rows must be greater than 0.");
        }

        final int columnCount = grid.get(0).size();
        if (columnCount == 0) {
            throw new IllegalArgumentException("Number of columns must be greater than 0.");
        }
        for (List<? extends JComponent> row : grid) {
            if (row.size() != columnCount) {
                throw new IllegalArgumentException("All rows must have the same number of columns.");
            }
        }
    }

    private List<? extends JComponent> getComponentsOnEdge(final JComponent component, final Direction direction) {

        if (!(component instanceof FocusableCollection)) {
            return Collections.singletonList(component);
        }

        final FocusableCollection collection = (FocusableCollection) component;

        switch (direction) {
            case LEFT:
                return collection.getComponentsOnLeft();
            case RIGHT:
                return collection.getComponentsOnRight();
            case ABOVE:
                return collection.getComponentsAbove();
            case BELOW:
                return collection.getComponentsBelow();
            default:
                throw new RuntimeException("Unexpected direction: " + direction);
        }
    }

    private enum Direction {
        LEFT,
        RIGHT,
        ABOVE,
        BELOW
    }
}
