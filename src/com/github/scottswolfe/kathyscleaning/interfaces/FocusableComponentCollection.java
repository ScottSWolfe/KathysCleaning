package com.github.scottswolfe.kathyscleaning.interfaces;

import javax.swing.JComponent;
import java.util.List;

public interface FocusableComponentCollection {

    List<? extends JComponent> getComponentsThatTransferFocusLeft();
    List<? extends JComponent> getComponentsThatTransferFocusRight();
    List<? extends JComponent> getComponentsThatTransferFocusAbove();
    List<? extends JComponent> getComponentsThatTransferFocusBelow();

    default JComponent getComponentToFocusFromLeft(int row) {
        return null;
    }

    default JComponent getComponentToFocusFromRight(int row) {
        return null;
    }

    default JComponent getComponentToFocusFromAbove(int column) {
        return null;
    }

    default JComponent getComponentToFocusFromBelow(int column) {
        return null;
    }

    default JComponent getComponentToFocusFromLeft() {
        return getComponentToFocusFromLeft(0);
    }

    default JComponent getComponentToFocusFromRight() {
        return getComponentToFocusFromRight(0);
    }

    default JComponent getComponentToFocusFromAbove() {
        return getComponentToFocusFromAbove(0);
    }

    default JComponent getComponentToFocusFromBelow() {
        return getComponentToFocusFromBelow(0);
    }
}
