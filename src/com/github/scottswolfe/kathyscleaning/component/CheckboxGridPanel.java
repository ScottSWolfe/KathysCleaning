package com.github.scottswolfe.kathyscleaning.component;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.JCheckBox;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public abstract class CheckboxGridPanel extends ComponentGridPanel<JCheckBox> {

    protected CheckboxGridPanel(
        List<List<Pair<String, Boolean>>> checkBoxLabelsAndStatus,
        final Color backgroundColor,
        final List<Component> componentsOnLeft,
        final List<Component> componentsOnRight,
        final List<Component> componentsAbove,
        final List<Component> componentsBelow
    ) {
        super(
            createCheckBoxes(checkBoxLabelsAndStatus),
            backgroundColor,
            createConstraints(),
            componentsOnLeft,
            componentsOnRight,
            componentsAbove,
            componentsBelow
        );
    }

    private static List<List<JCheckBox>> createCheckBoxes(List<List<Pair<String, Boolean>>> checkBoxLabelsAndStatus) {
        final List<List<JCheckBox>> checkBoxes = new ArrayList<>();
        for (List<Pair<String, Boolean>> checkBoxLabelsAndStatusInRow : checkBoxLabelsAndStatus) {
            final List<JCheckBox> checkBoxesInRow = new ArrayList<>();
            checkBoxes.add(checkBoxesInRow);
            for (Pair<String, Boolean> checkBoxLabelAndStatus : checkBoxLabelsAndStatusInRow) {
                final JCheckBox checkBox = new JCheckBox();
                checkBox.setText(checkBoxLabelAndStatus.getLeft());
                checkBox.setSelected(checkBoxLabelAndStatus.getRight());
                checkBoxesInRow.add(checkBox);
            }
        }
        return checkBoxes;
    }

    private static String createConstraints() {
        return "grow";
    }

    protected String getCheckBoxLabel(int row, int column) {
        return getComponent(row, column).getText();
    }

    protected boolean getCheckBoxStatus(int row, int column) {
        return getComponent(row, column).isSelected();
    }

    protected void setCheckBoxLabel(int row, int column, final String label) {
        getComponent(row, column).setText(label);
    }

    protected void setCheckBoxStatus(int row, int column, boolean isSelected) {
        getComponent(row, column).setSelected(isSelected);
    }

    protected void uncheckAllBoxes() {
        for (int row = 0; row < rowCount(); row++) {
            for (int column = 0; column < columnCount(); column++) {
                getComponent(row, column).setSelected(false);
            }
        }
    }
}
