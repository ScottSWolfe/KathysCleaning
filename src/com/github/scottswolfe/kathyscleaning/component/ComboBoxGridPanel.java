package com.github.scottswolfe.kathyscleaning.component;

import net.miginfocom.swing.MigLayout;

import javax.swing.JComboBox;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ComboBoxGridPanel extends ComponentGridPanel<JComboBox<String>> {

    protected ComboBoxGridPanel(
        final List<List<String>> comboBoxSelectedValues,
        final List<String> availableOptions,
        final Color backgroundColor
    ) {
        super(
            createComboBoxes(comboBoxSelectedValues, availableOptions),
            backgroundColor,
            createMigLayout(comboBoxSelectedValues),
            ComboBoxGridPanel::createConstraints
        );
    }

    private static List<List<JComboBox<String>>> createComboBoxes(
        final List<List<String>> comboBoxSelectedValues,
        final List<String> availableOptions
    ) {
        final List<String> comboBoxOptions = Stream.concat(
                comboBoxSelectedValues.stream().flatMap(Collection::stream),
                availableOptions.stream()
            )
            .distinct()
            .sorted()
            .collect(Collectors.toList());

        final List<List<JComboBox<String>>> comboBoxes = new ArrayList<>();
        for (List<String> comboBoxSelectionsInRow : comboBoxSelectedValues) {
            final List<JComboBox<String>> comboBoxesInRow = new ArrayList<>();
            comboBoxes.add(comboBoxesInRow);
            for (String comboBoxSelection : comboBoxSelectionsInRow) {

                final JComboBox<String> comboBox = new JComboBox<>();
                comboBox.setEditable(true);
                comboBox.setSize(10, UNDEFINED_CONDITION);

                final String emptyChoice = "";
                comboBox.addItem(emptyChoice);
                comboBoxOptions.forEach(comboBox::addItem);
                comboBox.setSelectedItem(comboBoxSelection);

                comboBoxesInRow.add(comboBox);
            }
        }
        return comboBoxes;
    }

    private static MigLayout createMigLayout(final List<List<String>> comboBoxSelectedValues) {
        return new MigLayout();
    }

    private static String createConstraints(int row, int column, int rowCount, int columnCount) {
        return "grow";
    }
}
