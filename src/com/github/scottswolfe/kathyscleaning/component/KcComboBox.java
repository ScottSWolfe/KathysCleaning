package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import javax.swing.JComboBox;
import java.util.List;

public class KcComboBox extends JComboBox<String> {

    private static final int DEFAULT_SIZE = 10;

    public static KcComboBox from(final List<String> items, final String selectedItem) {
        return new KcComboBox(items, selectedItem, true, DEFAULT_SIZE);
    }

    public static KcComboBox from(final List<String> items, final String selectedItem, final boolean isEditable) {
        return new KcComboBox(items, selectedItem, isEditable, DEFAULT_SIZE);
    }

    public static KcComboBox from(final List<String> items, final String selectedItem, final int size) {
        return new KcComboBox(items, selectedItem, true, size);
    }

    public static KcComboBox from(
        final List<String> items,
        final String selectedItem,
        final boolean isEditable,
        final int size
    ) {
        return new KcComboBox(items, selectedItem, isEditable, size);
    }

    private KcComboBox(
        final List<String> items,
        final String selectedItem,
        final boolean isEditable,
        final int size
    ) {
        setEditable(isEditable);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));
        setSize(size, UNDEFINED_CONDITION);

        final String emptyItem = "";
        if (!items.contains(emptyItem)) {
            addItem(emptyItem);
        }
        items.stream().distinct().sorted().forEach(this::addItem);
        setSelectedItem(selectedItem);
    }
}
