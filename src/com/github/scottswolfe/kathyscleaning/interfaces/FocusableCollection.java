package com.github.scottswolfe.kathyscleaning.interfaces;

import com.github.scottswolfe.kathyscleaning.utility.FocusableConnector;

import javax.annotation.Nonnull;
import javax.swing.JComponent;
import java.awt.event.FocusListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface FocusableCollection {

    JComponent GAP = new JComponent() {};

    Map<FocusableCollection, Map<JComponent, Set<FocusListener>>> componentToListenerMap = new HashMap<>();

    List<List<? extends JComponent>> getComponentsAsGrid();

    default void connectFocusableComponents() {
        FocusableConnector.from().connect(this);
    }

    default <T extends JComponent> void addListenerToMap(
        @Nonnull final JComponent component,
        @Nonnull final FocusListener listener
    ) {
        if (!componentToListenerMap.containsKey(this)) {
            componentToListenerMap.put(this, new HashMap<>());
        }

        if (!componentToListenerMap.get(this).containsKey(component)) {
            componentToListenerMap.get(this).put(component, new HashSet<>());
        }

        componentToListenerMap.get(this).get(component).add(listener);
    }

    default Set<FocusListener> getListenersForComponent(@Nonnull final JComponent component) {
        return componentToListenerMap.getOrDefault(this, new HashMap<>()).getOrDefault(component, new HashSet<>());
    }

    default void clearFocusListenerTracking() {
        componentToListenerMap.getOrDefault(this, new HashMap<>()).clear();
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
