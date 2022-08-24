package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.controller.KeyboardFocusListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.util.Iterator;
import java.util.List;

/**
 * An abstract class with a grid of components.
 */
public abstract class ComponentGridPanel<T extends JComponent> extends JPanel {

    private final List<List<T>> components;

    protected ComponentGridPanel(
        final List<List<T>> components,
        final Color backgroundColor,
        final String constraints,
        final List<Component> componentsOnLeft,
        final List<Component> componentsOnRight,
        final List<Component> componentsAbove,
        final List<Component> componentsBelow
    ) {
        final int rowCount = components.size();
        if (rowCount == 0) {
            throw new IllegalArgumentException("Number of rows must be greater than 0.");
        }

        final int columnCount = components.get(0).size();
        if (columnCount == 0) {
            throw new IllegalArgumentException("Number of columns must be greater than 0.");
        }
        for (List<T> column : components) {
            if (column.size() != columnCount) {
                throw new IllegalArgumentException("All rows must have the same number of columns.");
            }
        }

        validateAdjacentComponents(componentsOnLeft, rowCount);
        validateAdjacentComponents(componentsOnRight, rowCount);
        validateAdjacentComponents(componentsAbove, columnCount);
        validateAdjacentComponents(componentsBelow, columnCount);

        this.components = components;

        setLayout(new MigLayout("insets 0"));
        setBackground(backgroundColor);
        addComponents(backgroundColor, constraints);
        addKeyboardFocusListeners(componentsOnLeft, componentsOnRight, componentsAbove, componentsBelow);
    }

    private void validateAdjacentComponents(final List<Component> components, int expectedSize) {
        if (components.size() != expectedSize) {
            throw new IllegalArgumentException(
                "Adjacent components must be the same size as the number of rows or columns."
            );
        }
    }

    private void addComponents(final Color backgroundColor, final String constraints) {
        for (int row = 0; row < rowCount(); row++) {
            for (int column = 0; column < columnCount(); column++) {

                final T component = getComponent(row, column);
                component.setFont(component.getFont().deriveFont(Settings.FONT_SIZE));
                component.setBackground(backgroundColor);

                if (row < rowCount() - 1 && column == columnCount() - 1) {
                    add(component, constraints + ", wrap");
                } else {
                    add(component, constraints);
                }
            }
        }
    }

    private void addKeyboardFocusListeners(
        final List<Component> componentsOnLeft,
        final List<Component> componentsOnRight,
        final List<Component> componentsAbove,
        final List<Component> componentsBelow
    ) {
        for (int row = 0; row < rowCount(); row++) {
            for (int column = 0; column < columnCount(); column++) {

                final Component componentOnLeft;
                if (column > 0) {
                    componentOnLeft = getComponent(row, column - 1);
                } else {
                    componentOnLeft = componentsOnLeft.get(row);
                }

                final Component componentOnRight;
                if (column < columnCount() - 1) {
                    componentOnRight = getComponent(row, column + 1);
                } else {
                    componentOnRight = componentsOnRight.get(row);
                }

                final Component componentAbove;
                if (row > 0) {
                    componentAbove = getComponent(row - 1, column);
                } else {
                    componentAbove = componentsAbove.get(column);
                }

                final Component componentBelow;
                if (row < rowCount() - 1) {
                    componentBelow = getComponent(row + 1, column);
                } else {
                    componentBelow = componentsBelow.get(column);
                }

                final Component componentOnEnter;
                if (column < columnCount() - 1) {
                    componentOnEnter = getComponent(row, column + 1);
                } else if (row < rowCount() - 1) {
                    componentOnEnter = getComponent(row + 1, 0);
                } else  {
                    componentOnEnter = componentsOnRight.get(row);
                }

                final KeyboardFocusListener keyboardFocusListener = KeyboardFocusListener.from(
                    getComponent(row, column),
                    componentOnLeft,
                    componentOnRight,
                    componentAbove,
                    componentBelow,
                    componentOnEnter
                );

                getComponent(row, column).addFocusListener(keyboardFocusListener);
            }
        }
    }

    public JComponent getComponentToFocusFromLeft() {
        return getComponent(0, 0);
    }

    public JComponent getComponentToFocusFromRight() {
        return getComponent(rowCount() - 1, columnCount() - 1);
    }

    protected T getComponent(int row, int column) {
        if (row < 0 || row >= rowCount() || column < 0 || column >= columnCount()) {
            throw new IllegalArgumentException(
                "row number '" + row + "' or column number '" + column + "' is out of bounds."
            );
        }
        return components.get(row).get(column);
    }

    protected int rowCount() {
        return components.size();
    }

    protected int columnCount() {
        return components.get(0).size();
    }

    protected Iterator<T> iterator() {
        return new Iterator<T>() {

            int row = 0;
            int column = 0;

            @Override
            public boolean hasNext() {
                if (row < rowCount() - 1) {
                    return true;
                } else if (row == rowCount() - 1 && column < columnCount()) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new IllegalArgumentException("Iterator does not have any elements left");
                }

                final T next = getComponent(row, column);

                if (column < columnCount() - 1) {
                    column++;
                } else {
                    row++;
                    column = 0;
                }

                return next;
            }
        };
    }
}
