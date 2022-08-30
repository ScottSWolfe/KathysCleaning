package com.github.scottswolfe.kathyscleaning.interfaces;

import com.github.scottswolfe.kathyscleaning.utility.FocusableConnector;

import javax.swing.JComponent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface FocusableCollection {

    JComponent GAP = new JComponent() {};

    List<List<? extends JComponent>> getComponentsAsGrid();

    default void connectFocusableComponents() {
        FocusableConnector.from().connect(this);
    }

    default List<? extends JComponent> getComponentsOnLeft() {
        return getComponentsAsGrid().stream()
            .map(row -> row.get(0))
            .map(component -> component instanceof FocusableCollection ?
                ((FocusableCollection) component).getComponentsOnLeft()
                : Collections.singletonList(component))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    default List<? extends JComponent> getComponentsOnRight() {
        return getComponentsAsGrid().stream()
            .map(row -> row.get(getComponentsAsGrid().get(0).size() - 1))
            .map(component -> component instanceof FocusableCollection ?
                ((FocusableCollection) component).getComponentsOnRight()
                : Collections.singletonList(component))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    default List<? extends JComponent> getComponentsAbove() {
        return getComponentsAsGrid().get(0).stream()
            .map(component -> component instanceof FocusableCollection ?
                ((FocusableCollection) component).getComponentsAbove()
                : Collections.singletonList(component))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    default List<? extends JComponent> getComponentsBelow() {
        return getComponentsAsGrid().get(getComponentsAsGrid().size() - 1).stream()
            .map(component -> component instanceof FocusableCollection ?
                ((FocusableCollection) component).getComponentsBelow()
                : Collections.singletonList(component))
            .flatMap(List::stream)
            .collect(Collectors.toList());
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
