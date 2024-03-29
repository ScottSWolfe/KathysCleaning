package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.interfaces.FocusableCollection;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.utility.GridValidator;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An abstract class with a grid of components.
 */
public abstract class ComponentGridPanel<T extends JComponent> extends JPanel implements FocusableCollection {

    private final List<List<T>> components;

    protected ComponentGridPanel(
        final List<List<T>> components,
        final Color backgroundColor,
        final MigLayout migLayout,
        final ConstraintsBuilder constraintsBuilder
    ) {
        GridValidator.from().validateGrid(components);
        this.components = components;

        setLayout(migLayout);
        setBackground(backgroundColor);
        addComponents(backgroundColor, constraintsBuilder);

        connectFocusableComponents();
    }

    private void addComponents(final Color backgroundColor, final ConstraintsBuilder constraintsBuilder) {
        for (int row = 0; row < rowCount(); row++) {
            for (int column = 0; column < columnCount(); column++) {

                final T component = getComponent(row, column);
                component.setFont(component.getFont().deriveFont(Settings.FONT_SIZE));
                component.setBackground(backgroundColor);

                final String constraints = constraintsBuilder.buildConstraints(row, column, rowCount(), columnCount());
                if (row < rowCount() - 1 && column == columnCount() - 1) {
                    add(component, constraints + ", wrap");
                } else {
                    add(component, constraints);
                }
            }
        }
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

            final GridIterator gridIterator = new GridIterator();

            @Override
            public boolean hasNext() {
                return gridIterator.hasNext();
            }

            @Override
            public T next() {
                return gridIterator.next().getLeft();
            }
        };
    }

    protected GridIterator gridIterator() {
        return new GridIterator();
    }

    protected class GridIterator implements Iterator<Triple<T, Integer, Integer>> {

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
        public Triple<T, Integer, Integer> next() {
            if (!hasNext()) {
                throw new IllegalArgumentException("Iterator does not have any elements left");
            }

            final Triple<T, Integer, Integer> next = new ImmutableTriple<>(getComponent(row, column), row, column);

            if (column < columnCount() - 1) {
                column++;
            } else {
                row++;
                column = 0;
            }

            return next;
        }
    }

    @Override
    public List<List<? extends Component>> getComponentsAsGrid() {
        return components.stream().map(ArrayList::new).collect(Collectors.toList());
    }

    protected interface ConstraintsBuilder {
        String buildConstraints(int row, int column, int rowCount, int columnCount);
    }
}
