package com.github.scottswolfe.kathyscleaning.menu.model;

import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;

public class Settings {
    
    /**
     * Default font sizes.
     */
    public static float FONT_SIZE = 20;
    public static float HEADER_FONT_SIZE = 28;
    public static float TAB_FONT_SIZE = 20;
    
    /**
     * Font size bases for calculating font size based on selected size factor.
     */
    public static final int FONT_SIZE_BASE = 12;
    public static final int HEADER_FONT_SIZE_BASE = 20;
    public static final int FONT_SIZE_MULTIPLIER = 4;
    
    /**
     * Default colors
     */
    public final static Color BACKGROUND_COLOR = Color.WHITE;
    public final static Color MAIN_COLOR = new Color(105,139,105).darker(); // dark sea green
    public final static Color FOREGROUND_COLOR = Color.WHITE;
    public static final Color HEADER_BACKGROUND = new Color(245,245,245); 
    public static final Color CHANGE_DAY_COLOR = new Color (100, 149, 237);
    public static final Color ADD_HOUSE_COLOR = new Color (113, 198, 113).brighter() ;
    public static final Color DELETE_HOUSE_COLOR = new Color (240, 128, 128) ;
    
    
    
    /**
     * Calculates the general font size based on the given sizing factor
     * 
     * Sets FONT_SIZE. Also sets font size of OptionPanes.
     * @param sizeFactor
     */
    public static void setFontSize(int sizeFactor) {
        FONT_SIZE = FONT_SIZE_BASE + FONT_SIZE_MULTIPLIER*sizeFactor;
        try {
            UIManager.put("OptionPane.messageFont", 
                    new Font("System", Font.PLAIN, (int) FONT_SIZE));
            UIManager.put("OptionPane.buttonFont", 
                    new Font("System", Font.PLAIN, (int) FONT_SIZE));
        }
        catch (Exception e) {
            UIManager.put("OptionPane.messageFont", 
                    new Font("System", Font.PLAIN, 24));
            UIManager.put("OptionPane.buttonFont", 
                    new Font("System", Font.PLAIN, 24));
        }
    }
    
    /**
     * Calculates the header font size based on the given sizing factor
     * 
     * @param sizeFactor
     */
    public static void setHeaderFontSize(int sizeFactor) {
        HEADER_FONT_SIZE = HEADER_FONT_SIZE_BASE + FONT_SIZE_MULTIPLIER*sizeFactor;
    }
    
    /**
     * Calculates the tab font size based on the given sizing factor
     * 
     * @param sizeFactor
     */
    public static void setTabFontSize(int sizeFactor) {
        TAB_FONT_SIZE = FONT_SIZE_BASE + FONT_SIZE_MULTIPLIER*sizeFactor;
    }
}
