package com.github.scottswolfe.kathyscleaning.general.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class KeyboardFocusListener implements FocusListener {

    private final Component thisComponent;
    private final Component componentOnLeft;
    private final Component componentOnRight;
    private final Component componentAbove;
    private final Component componentBelow;
    private final Component componentOnEnter;

    private final KeyboardKeyListener keyListener;

    public static KeyboardFocusListener from(
        final Component thisComponent,
        final Component componentOnLeft,
        final Component componentOnRight,
        final Component componentAbove,
        final Component componentBelow,
        final Component componentOnEnter
    ) {
        return new KeyboardFocusListener(
            thisComponent,
            componentOnLeft,
            componentOnRight,
            componentAbove,
            componentBelow,
            componentOnEnter
        );
    }

    private KeyboardFocusListener(
        final Component thisComponent,
        final Component componentOnLeft,
        final Component componentOnRight,
        final Component componentAbove,
        final Component componentBelow,
        final Component componentOnEnter
    ) {
        // todo: remove commented out code once verified it's not necessary
        /*
        if (thisComponent instanceof JComboBox) {
            final JComboBox comboBox = (JComboBox) thisComponent;
            this.thisComponent = comboBox.getEditor().getEditorComponent();
        } else {F
            this.thisComponent = thisComponent;
        }
         */

        this.thisComponent = thisComponent;
        this.componentOnLeft = componentOnLeft;
        this.componentOnRight = componentOnRight;
        this.componentAbove = componentAbove;
        this.componentBelow = componentBelow;
        this.componentOnEnter = componentOnEnter;

        this.keyListener = new KeyboardKeyListener();
    }

    @Override
    public void focusGained(FocusEvent arg0) {

        thisComponent.addKeyListener(keyListener);

        if (thisComponent instanceof JTextField) {
            final JTextField textField = (JTextField) thisComponent;
            textField.selectAll();
        } else if (thisComponent instanceof JComboBox) {
            final JComboBox comboBox = (JComboBox) thisComponent;
            comboBox.getEditor().selectAll();
        }
    }

    @Override
    public void focusLost(FocusEvent arg0) {
        thisComponent.removeKeyListener(keyListener);
    }

    private class KeyboardKeyListener implements KeyListener {

        @Override
        public void keyPressed(KeyEvent arg0) {

            // Right Arrow
            if (arg0.getKeyCode() == KeyEvent.VK_RIGHT && componentOnRight != null) {
                componentOnRight.requestFocusInWindow();
            }

            // Left Arrow
            else if (arg0.getKeyCode() == KeyEvent.VK_LEFT && componentOnLeft != null) {
                componentOnLeft.requestFocusInWindow();
            }

            // Up Arrow
            else if (arg0.getKeyCode() == KeyEvent.VK_UP && componentAbove != null) {
                componentAbove.requestFocusInWindow();
            }

            // Down Arrow
            else if (arg0.getKeyCode() == KeyEvent.VK_DOWN && componentBelow != null) {
                componentBelow.requestFocusInWindow();
            }

            // Enter
            else if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
                if (thisComponent instanceof JButton) {
                    // todo: check if this code is actually doing anything
                    final JButton button = (JButton) thisComponent;
                    final ActionEvent actionEvent = new ActionEvent(this, 0, "");
                    final ActionListener[] actionListeners = button.getActionListeners();
                    for (ActionListener actionListener : actionListeners) {
                        actionListener.actionPerformed(actionEvent);
                    }
                } else if (thisComponent instanceof JCheckBox) {
                    final JCheckBox checkbox = (JCheckBox) thisComponent;
                    checkbox.setSelected(!checkbox.isSelected());
                }

                if (componentOnEnter != null) {
                    componentOnEnter.requestFocusInWindow();
                } else if (componentOnRight != null) {
                    componentOnRight.requestFocusInWindow();
                } else if (componentBelow != null) {
                    componentBelow.requestFocusInWindow();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent arg0) {
        }

        @Override
        public void keyTyped(KeyEvent arg0) {
        }
    }
}
