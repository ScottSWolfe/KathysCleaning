package com.github.scottswolfe.kathyscleaning.general.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

public class FileChooserHelper {

/* CLASS VARIABLES ========================================================== */
    
    /**
     * The user's desktop
     */
    public final static File DESKTOP =
            new File(System.getProperty("user.home") + "\\Desktop");
    
    /**
     * The application's save file directory
     */
    public final static File SAVE_FILE_DIR =
            new File(System.getProperty("user.dir") + "\\save\\user");
    
    public final static String XLSX = "xlsx";
    public final static String TXT = "txt";
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    /**
     * Creates a dialog window that allows the user to choose a file. Returns
     * the chosen file or null if no file is chosen. Creates a new file if the
     * file does not exist.
     * 
     * @param directory the default directory
     * @param title the title for the dialog window
     * @param extension the file extension for filtering; null for no filtering
     * @param createFile true if user can create new files, false otherwise
     * @return the chosen or created file or null if no file is chosen
     */
    public static File chooseFile(File directory, String title, String extension, boolean createFile) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, title, extension); 
        File file = chooseFile(chooser, createFile);
        Settings.changeLookAndFeelProgram();
        return file;
    }
 
    public static File chooseFile(File directory, String title, String extension) {
        return chooseFile(directory, title, extension, false);
    }
    
    public static File chooseFile(File directory, String title, boolean createFile) {
        return chooseFile(directory, title, null, createFile);
    }
    
    public static File chooseFile(File directory, String title) {
        return chooseFile(directory, title, null, false);
    }
    
    
    
/* PRIVATE METHODS ========================================================== */
    
    private static JFileChooser createChooser(File directory, String title, String extension) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(directory);
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        setFilter(chooser, extension);
        return chooser;
    }
    
    private static void setFilter(JFileChooser chooser, String extension) {
        if (extension == null) {
            return;
        }
        FileNameExtensionFilter filter =
                new FileNameExtensionFilter(null, extension);
        chooser.setFileFilter(filter);
    }
    
    private static File chooseFile(JFileChooser chooser, boolean createFile) {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile(chooser, createFile);
        } else {
            return null;
        }
    }
    
    private static File getSelectedFile(JFileChooser chooser, boolean createFile) {
        File file = chooser.getSelectedFile();
        if (!file.exists()) {
            if(createFile) {
                createFile(file);
                return file;
            } else {
                return null;
            }
        } else {
            return file;
        }
    }
    
    private static void createFile(File file) {
        try {
            Files.createFile(file.toPath());
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
