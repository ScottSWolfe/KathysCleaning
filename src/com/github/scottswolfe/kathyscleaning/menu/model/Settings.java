package com.github.scottswolfe.kathyscleaning.menu.model;

import com.github.scottswolfe.kathyscleaning.general.model.ButtonColors;

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * This class manages all project-wide settings
 *
 */
public class Settings {

    /**
     * Default Look and Feel
     */
    public static final String LOOK_AND_FEEL = "Nimbus";

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
    public static final Color ADD_HOUSE_COLOR = new Color (113, 198, 113).brighter();
    public static final Color DELETE_HOUSE_COLOR = new Color (240, 128, 128);
    public static final Color DEFAULT_BUTTON_COLOR = UIManager.getColor("Button.background");
    public static final Color EDITED_BUTTON_COLOR = MAIN_COLOR;
    public static final ButtonColors QUIET_BUTTON_COLORS = ButtonColors.from(DEFAULT_BUTTON_COLOR, Color.BLACK);
    public static final ButtonColors LOUD_BUTTON_COLORS = ButtonColors.from(EDITED_BUTTON_COLOR, FOREGROUND_COLOR);
    public static final ButtonColors ALTERNATE_BUTTON_COLORS = ButtonColors.from(CHANGE_DAY_COLOR, FOREGROUND_COLOR);

    /**
     * Settings and Data Save Files
     */
    public static final File SETTINGS_SAVE_FILE = new File(System.getProperty("user.dir") + "\\save\\settings\\SettingsSaveFile" );

    /**
     * Default Excel Template.
     */
    public static final File DEFAULT_EXCEL_TEMPLATE =
            new File(System.getProperty("user.dir") + "\\lib\\Template A.xlsx");

    /**
     * Default Save Location.
     */
    public static final File DEFAULT_EXCEL_SAVE_LOCATION =
            new File(System.getProperty("user.home") + "\\Desktop");

    /**
     * Default Text Size Factor.
     */
    public static final int DEFAULT_TEXT_SIZE_FACTOR = 2;

    /**
     * Static method to change Look and Feel to Program Default.
     */
    public static void changeLookAndFeelProgram() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (LOOK_AND_FEEL.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * Static method to change Look and Feel to System Default.
     */
    public static void changeLookAndFeelSystem() {
        // Change look and feel to default system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFontSizes(int sizeFactor) {
        FONT_SIZE = FONT_SIZE_BASE + FONT_SIZE_MULTIPLIER*sizeFactor;
        HEADER_FONT_SIZE = HEADER_FONT_SIZE_BASE + FONT_SIZE_MULTIPLIER*sizeFactor;
        TAB_FONT_SIZE = FONT_SIZE_BASE + FONT_SIZE_MULTIPLIER*sizeFactor;
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
}
