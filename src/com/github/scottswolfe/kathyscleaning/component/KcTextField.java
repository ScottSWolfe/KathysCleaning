package com.github.scottswolfe.kathyscleaning.component;

import com.github.scottswolfe.kathyscleaning.general.controller.TimeDocumentFilter;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import java.awt.event.KeyListener;
import java.util.function.Function;

public class KcTextField extends JTextField {

    private static final int DEFAULT_COLUMN_COUNT = 6;

    public static KcTextField from() {
        return new KcTextField("", DEFAULT_COLUMN_COUNT, null);
    }

    public static KcTextField from(
        final String text
    ) {
        return new KcTextField(text, DEFAULT_COLUMN_COUNT, null);
    }

    public static KcTextField from(
        final String text,
        final int columnCount
    ) {
        return new KcTextField(text, columnCount, null);
    }

    public static KcTextField from(
        final String text,
        final Function<TimeDocumentFilter, KeyListener> keyListenerBuilder
    ) {
        return new KcTextField(text, DEFAULT_COLUMN_COUNT, keyListenerBuilder);
    }

    public static KcTextField from(
        final String text,
        final int columnCount,
        final Function<TimeDocumentFilter, KeyListener> keyListenerBuilder
    ) {
        return new KcTextField(text, columnCount, keyListenerBuilder);
    }

    private KcTextField(
        final String text,
        final int columnCount,
        final Function<TimeDocumentFilter, KeyListener> keyListenerBuilder
    ) {
        super(text);

        setBackground(Settings.BACKGROUND_COLOR);
        setColumns(columnCount);
        setFont(getFont().deriveFont(Settings.FONT_SIZE));

        if (keyListenerBuilder != null) {
            final TimeDocumentFilter timeDocumentFilter = new TimeDocumentFilter(this);
            ((AbstractDocument) getDocument()).setDocumentFilter(timeDocumentFilter);
            addKeyListener(keyListenerBuilder.apply(timeDocumentFilter));
        }
    }
}
