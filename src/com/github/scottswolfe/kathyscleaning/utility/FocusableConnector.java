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
        if (component == null || component == FocusableCollection.GAP) {
            return;
        }

        final List<? extends JComponent> componentsThatTransferFocus = getComponentsOnEdge(
            components, row, column, direction
        );

        List<? extends JComponent> componentsThatReceiveFocus = Collections.emptyList();
        switch (direction) {
            case LEFT:
                if (column > 0) {
                    componentsThatReceiveFocus = getComponentsOnEdge(
                        components, row, column - 1, Direction.RIGHT
                    );
                }
                break;
            case RIGHT:
                if (column < columnCount - 1) {
                    componentsThatReceiveFocus = getComponentsOnEdge(
                        components, row, column + 1, Direction.LEFT
                    );
                }
                break;
            case ABOVE:
                if (row > 0) {
                    componentsThatReceiveFocus = getComponentsOnEdge(
                        components, row - 1, column, Direction.BELOW
                    );
                }
                break;
            case BELOW:
                if (row < rowCount - 1) {
                    componentsThatReceiveFocus = getComponentsOnEdge(
                        components, row + 1, column, Direction.ABOVE
                    );
                }
                break;
            default:
                throw createUnexpectedDirectionException(direction);
        }

        connectComponents(componentsThatTransferFocus, componentsThatReceiveFocus, direction);
    }

    private List<? extends JComponent> getComponentsOnEdge(
        final List<List<? extends JComponent>> components,
        final int row,
        final int column,
        final Direction direction
    ) {
        final JComponent component = findNonGapComponent(components, row, column, direction);

        if (component == FocusableCollection.GAP) {
            return Collections.emptyList();
        }

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
                throw createUnexpectedDirectionException(direction);
        }
    }

    private JComponent findNonGapComponent(
        final List<List<? extends JComponent>> components,
        final int row,
        final int column,
        final Direction direction
    ) {
        JComponent currentComponent = components.get(row).get(column);
        if (currentComponent != FocusableCollection.GAP) {
            return currentComponent;
        }

        switch (direction) {
            case LEFT:
            case RIGHT:
                currentComponent = lookInDirectionForNonGapComponent(components, row, column, Direction.ABOVE);
                if (currentComponent != FocusableCollection.GAP) {
                    return currentComponent;
                }

                currentComponent = lookInDirectionForNonGapComponent(components, row, column, Direction.BELOW);
                if (currentComponent != FocusableCollection.GAP) {
                    return currentComponent;
                }

                break;

            case ABOVE:
            case BELOW:
                currentComponent = lookInDirectionForNonGapComponent(components, row, column, Direction.LEFT);
                if (currentComponent != FocusableCollection.GAP) {
                    return currentComponent;
                }

                currentComponent = lookInDirectionForNonGapComponent(components, row, column, Direction.RIGHT);
                if (currentComponent != FocusableCollection.GAP) {
                    return currentComponent;
                }

                break;

            default:
                throw createUnexpectedDirectionException(direction);
        }

        return currentComponent;
    }

    private JComponent lookInDirectionForNonGapComponent(
        final List<List<? extends JComponent>> components,
        int row,
        int column,
        final Direction direction
    ) {
        int currentRow = row;
        int currentColumn = column;
        JComponent currentComponent = components.get(currentRow).get(currentColumn);

        while (currentComponent == FocusableCollection.GAP) {
            switch (direction) {
                case LEFT:
                    currentColumn--;
                    if (currentColumn < 0) {
                        return currentComponent;
                    }
                    break;
                case RIGHT:
                    currentColumn++;
                    if (currentColumn == components.get(currentRow).size()) {
                        return currentComponent;
                    }
                    break;
                case ABOVE:
                    currentRow--;
                    if (currentRow < 0) {
                        return currentComponent;
                    }
                    break;
                case BELOW:
                    currentRow++;
                    if (currentRow == components.size()) {
                        return currentComponent;
                    }
                    break;
                default:
                    throw createUnexpectedDirectionException(direction);
            }

            currentComponent = components.get(currentRow).get(currentColumn);
        }

        return currentComponent;
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

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (grid.get(row).get(column) == null) {
                    throw new IllegalArgumentException(
                        String.format("Component is null at row %s and column %s", row, column)
                    );
                }
            }
        }
    }

    private RuntimeException createUnexpectedDirectionException(final Direction direction) {
        return new RuntimeException("Unexpected direction: " + direction);
    }

    private enum Direction {
        LEFT,
        RIGHT,
        ABOVE,
        BELOW
    }
}
