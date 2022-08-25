package com.github.scottswolfe.kathyscleaning.utility;

import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.interfaces.FocusableComponentCollection;

import javax.swing.JComponent;
import java.util.List;

public class FocusableComponentConnector {

    private static final FocusableComponentConnector focusableComponentConnector = new FocusableComponentConnector();

    public static FocusableComponentConnector from() {
        return focusableComponentConnector;
    }

    private FocusableComponentConnector() {}

    public <T extends JComponent> void connect(List<List<T>> components) {
        validateGrid(components);

        final int rowCount = components.size();
        final int columnCount = components.get(0).size();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {

                final JComponent component = components.get(row).get(column);
                if (component == null) {
                    continue;
                }
                validateNotCollection(component);

                final JComponent componentOnLeft;
                if (column > 0) {
                    componentOnLeft = components.get(row).get(column - 1);
                } else {
                    componentOnLeft = null;
                }
                validateNotCollection(componentOnLeft);

                final JComponent componentOnRight;
                if (column < columnCount - 1) {
                    componentOnRight = components.get(row).get(column + 1);
                } else {
                    componentOnRight = null;
                }
                validateNotCollection(componentOnRight);

                final JComponent componentAbove;
                if (row > 0) {
                    componentAbove = components.get(row - 1).get(column);
                } else {
                    componentAbove = null;
                }
                validateNotCollection(componentAbove);

                final JComponent componentBelow;
                if (row < rowCount - 1) {
                    componentBelow = components.get(row + 1).get(column);
                } else {
                    componentBelow = null;
                }
                validateNotCollection(componentBelow);

                final KeyboardFocusListener keyboardFocusListener = KeyboardFocusListener.from(
                    component,
                    componentOnLeft,
                    componentOnRight,
                    componentAbove,
                    componentBelow,
                    null
                );

                component.addFocusListener(keyboardFocusListener);
            }
        }
    }

    public void connectCollections(final List<List<FocusableComponentCollection>> collections) {
        validateGrid(collections);

        final int rowCount = collections.size();
        final int columnCount = collections.get(0).size();

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {

                final FocusableComponentCollection collection = collections.get(row).get(column);
                if (collection == null) {
                    continue;
                }

                final List<? extends JComponent> componentsThatTransferLeft = collection.getComponentsThatTransferFocusLeft();
                final JComponent componentOnLeft;
                if (column > 0) {
                    final FocusableComponentCollection collectionOnLeft = collections.get(row).get(column - 1);
                    componentOnLeft = collectionOnLeft.getComponentToFocusFromRight();
                } else {
                    componentOnLeft = null;
                }
                connectComponents(componentsThatTransferLeft, componentOnLeft, Direction.LEFT);

                final List<? extends JComponent> componentsThatTransferRight = collection.getComponentsThatTransferFocusRight();
                final JComponent componentOnRight;
                if (column < columnCount - 1) {
                    final FocusableComponentCollection collectionOnRight = collections.get(row).get(column + 1);
                    componentOnRight = collectionOnRight.getComponentToFocusFromLeft();
                } else {
                    componentOnRight = null;
                }
                connectComponents(componentsThatTransferRight, componentOnRight, Direction.RIGHT);

                final List<? extends JComponent> componentsThatTransferAbove = collection.getComponentsThatTransferFocusAbove();
                final JComponent componentAbove;
                if (row > 0) {
                    final FocusableComponentCollection collectionAbove = collections.get(row - 1).get(column);
                    componentAbove = collectionAbove.getComponentToFocusFromBelow();
                } else {
                    componentAbove = null;
                }
                connectComponents(componentsThatTransferAbove, componentAbove, Direction.ABOVE);

                final List<? extends JComponent> componentsThatTransferBelow = collection.getComponentsThatTransferFocusBelow();
                final JComponent componentBelow;
                if (row < rowCount - 1) {
                    final FocusableComponentCollection collectionBelow = collections.get(row + 1).get(column);
                    componentBelow = collectionBelow.getComponentToFocusFromAbove();
                } else {
                    componentBelow = null;
                }
                connectComponents(componentsThatTransferBelow, componentBelow, Direction.BELOW);
            }
        }
    }

    private void connectComponents(
        final List<? extends JComponent> components,
        final JComponent adjacentComponent,
        final Direction direction
    ) {
        if (components.isEmpty() || adjacentComponent == null) {
            return;
        }

        for (JComponent component : components) {
            final KeyboardFocusListener keyboardFocusListener = KeyboardFocusListener.from(
                component,
                direction == Direction.LEFT ? adjacentComponent : null,
                direction == Direction.RIGHT ? adjacentComponent : null,
                direction == Direction.ABOVE ? adjacentComponent : null,
                direction == Direction.BELOW ? adjacentComponent : null,
                null
            );

            component.addFocusListener(keyboardFocusListener);
        }
    }

    private <T> void validateGrid(final List<List<T>> grid) {
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

    private <T extends JComponent> void validateNotCollection(final T component) {
        if (component instanceof FocusableComponentCollection) {
            throw new IllegalArgumentException("Cannot pass in a FocusableComponentCollection");
        }
    }

    private enum Direction {
        LEFT,
        RIGHT,
        ABOVE,
        BELOW
    }
}
