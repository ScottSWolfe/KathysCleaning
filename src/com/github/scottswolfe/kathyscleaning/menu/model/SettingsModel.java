package com.github.scottswolfe.kathyscleaning.menu.model;

import java.io.File;

import com.github.scottswolfe.kathyscleaning.utility.JsonMethods;

public class SettingsModel {

    /**
     * Saved Excel Template File.
     */
    private static File excelTemplateFile;
    
    /**
     * Saved Excel Template Save Location.
     */
    private static File excelSaveLocation;
    
    /**
     * Saved Font Size Factor.
     */
    private static int fontSizeFactor;
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    public static void initialize() {
        SettingsModel.excelTemplateFile = Settings.DEFAULT_EXCEL_TEMPLATE;
        SettingsModel.excelSaveLocation = Settings.DEFAULT_EXCEL_SAVE_LOCATION;
        SettingsModel.fontSizeFactor = Settings.DEFAULT_TEXT_SIZE_FACTOR;
    }
    
    public static void save(File file) {
        SettingsSaveObject object = 
                new SettingsModel()
               .new SettingsSaveObject(SettingsModel.excelTemplateFile,
                                       SettingsModel.excelSaveLocation, 
                                       SettingsModel.fontSizeFactor);
        object.save(file);
    }
    
    public static void load(File file) {
        SettingsSaveObject object = (SettingsSaveObject) JsonMethods.loadFromFileJSON(SettingsSaveObject.class, file);
        object.load();
    }
    
    

/* PRIVATE CLASS ============================================================ */
    
    private class SettingsSaveObject {
        
        private File excelTemplateFile;
        private File excelSaveLocation;
        private int fontSizeFactor;
        
        SettingsSaveObject(File excelTemplateFile,
                           File excelSaveLocation,
                           int fontSizeFactor) {
            this.excelTemplateFile = excelTemplateFile;
            this.excelSaveLocation = excelSaveLocation;
            this.fontSizeFactor = fontSizeFactor;
        }
                        
        void save(File file) {
            JsonMethods.saveToFileJSON(this, SettingsSaveObject.class, file);
        }
        
        void load() {
            SettingsModel.setExcelTemplateFile(excelTemplateFile);
            SettingsModel.setExcelSaveLocation(excelSaveLocation);
            SettingsModel.setFontSizeFactor(fontSizeFactor);
        }
        
    }
    
    
    
/* GETTERS/SETTERS ========================================================== */
    
    /**
     * @return the excelTemplateFile
     */
    public static File getExcelTemplateFile() {
        return SettingsModel.excelTemplateFile;
    }

    /**
     * @param excelTemplateFile the excelTemplateFile to set
     */
    public static void setExcelTemplateFile(File excelTemplateFile) {
        SettingsModel.excelTemplateFile = excelTemplateFile;
    }

    /**
     * @return the excelSaveLocation
     */
    public static File getExcelSaveLocation() {
        return SettingsModel.excelSaveLocation;
    }

    /**
     * @param excelSaveLocation the excelSaveLocation to set
     */
    public static void setExcelSaveLocation(File excelSaveLocation) {
        SettingsModel.excelSaveLocation = excelSaveLocation;
    }

    /**
     * @return the fontSizeFactor
     */
    public static int getFontSizeFactor() {
        return SettingsModel.fontSizeFactor;
    }

    /**
     * @param fontSizeFactor the fontSizeFactor to set
     */
    public static void setFontSizeFactor(int fontSizeFactor) {
        SettingsModel.fontSizeFactor = fontSizeFactor;
        Settings.setFontSizes(fontSizeFactor);
    }
    
}
