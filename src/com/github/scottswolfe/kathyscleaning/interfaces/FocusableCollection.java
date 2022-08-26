package com.github.scottswolfe.kathyscleaning.interfaces;

import com.github.scottswolfe.kathyscleaning.utility.FocusableConnector;

import javax.swing.JComponent;
import java.util.List;
import java.util.stream.Collectors;

public interface FocusableCollection {

    List<List<? extends JComponent>> getComponentsAsGrid();

    default void connectFocusableComponents() {
        FocusableConnector.from().connect(this);
    }

    default List<? extends JComponent> getComponentsOnLeft() {
        return getComponentsAsGrid().stream()
            .map(row -> row.get(0))
            .collect(Collectors.toList());
    }

    default List<? extends JComponent> getComponentsOnRight() {
        return getComponentsAsGrid().stream()
            .map(row -> row.get(getComponentsAsGrid().get(0).size() - 1))
            .collect(Collectors.toList());
    }

    default List<? extends JComponent> getComponentsAbove() {
        return getComponentsAsGrid().get(0);
    }

    default List<? extends JComponent> getComponentsBelow() {
        return getComponentsAsGrid().get(getComponentsAsGrid().size() - 1);
    }

    default JComponent getComponentOnLeft() {
        return getComponentsOnLeft().get(0);
    }

    default JComponent getComponentOnRight() {
        return getComponentsOnRight().get(0);
    }

    default JComponent getComponentAbove() {
        return getComponentsAbove().get(0);
    }

    default JComponent getComponentBelow() {
        return getComponentsBelow().get(0);
    }
}
