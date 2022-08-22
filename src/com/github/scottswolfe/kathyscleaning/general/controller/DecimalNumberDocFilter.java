package com.github.scottswolfe.kathyscleaning.general.controller;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * This document filter only allows numeric digits and decimals. Everything else is filtered out.
 */
public class DecimalNumberDocFilter extends DocumentFilter {

    public static DecimalNumberDocFilter from() {
        return new DecimalNumberDocFilter();
    }

    private DecimalNumberDocFilter() {}

    public void replace(
        DocumentFilter.FilterBypass fb,
        int offset,
        int length,
        String text,
        AttributeSet attrs
    )
        throws BadLocationException
    {
        final char[] newCharArray = new char[text.length()];
        int count = 0;
        for (char character : text.toCharArray()) {
            if (Character.isDigit(character) || character == '.') {
                newCharArray[count] = character;
                count++;
            }
        }

        final String newText = new String(newCharArray, 0, count);

        super.replace(fb, offset, length, newText, attrs);
    }
}
