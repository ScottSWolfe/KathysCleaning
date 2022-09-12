package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import javax.swing.JComboBox;
import java.util.List;

public class KcComboBox extends JComboBox<String> {

    private static final int DEFAULT_SIZE = 10;

    public static KcComboBox from(final List<String> items, final String selectedItem) {
        return new KcComboBox(items, selectedItem, DEFAULT_SIZE);
    }

    public static KcComboBox from(final List<String> items, final String selectedItem, final int size) {
        return new KcComboBox(items, selectedItem, size);
    }

    private KcComboBox(final List<String> items, final String selectedItem, final int size) {
        setEditable(true);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));
        setSize(size, UNDEFINED_CONDITION);

        addItem("");
        items.stream().distinct().sorted().forEach(this::addItem);
        setSelectedItem(selectedItem);
    }
}
