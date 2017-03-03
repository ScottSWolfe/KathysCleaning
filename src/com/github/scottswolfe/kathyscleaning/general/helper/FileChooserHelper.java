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
     * Opens a dialog window that allows the user to select an existing file.
     * Returns the chosen file or null if no file is chosen. The file chooser
     * accept button says "Select".
     * 
     * @param directory the default directory
     * @param title the title for the dialog window
     * @param extension the file extension for filtering; null for no filtering
     * @return the chosen file or null if no file is chosen
     */
    public static File selectFile(File directory, String title, String extension) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, title, extension, "selectFile"); 
        File file = chooseFile(chooser, false, "selectFile");
        Settings.changeLookAndFeelProgram();
        return file;
    }
    
    /**
     * Opens a dialog window that allows the user to choose a directory. Returns
     * the chosen directory or null if no directory is chosen. The file chooser
     * accept button says "Select".
     * 
     * @param directory the default directory
     * @param title the title for the dialog window
     * @return the chosen directory or null if no file is chosen
     */
    public static File selectDirectory(File directory, String title) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, title, null, "selectDirectory"); 
        File file = chooseFile(chooser, true, "selectDirectory");
        Settings.changeLookAndFeelProgram();
        return file;
    }
    
    /**
     * Opens a dialog window that allows the user to select or create a file.
     * Returns the chosen file or null if no file is chosen. Creates a new file
     * if the file does not exist. The file chooser accept button says "Save".
     * 
     * @param directory the default directory
     * @param title the title for the dialog window
     * @param extension the file extension for filtering; null for no filtering
     * @return the chosen or created file or null if no file is chosen
     */
    public static File saveAs(File directory, String title, String extension) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, title, extension, "saveAs"); 
        File file = chooseFile(chooser, true, "saveAs");
        Settings.changeLookAndFeelProgram();
        return file;
    }
    
    /**
     * Opens a dialog window that allows the user to choose a file. Returns
     * the chosen file or null if no file is chosen. The file chooser accept
     * button says "Open".
     * 
     * @param directory the default directory
     * @param title the title for the dialog window
     * @param extension the file extension for filtering; null for no filtering
     * @param createFile true if user can create new files, false otherwise
     * @return the chosen or created file or null if no file is chosen
     */
    public static File open(File directory, String title, String extension) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, title, extension, "open"); 
        File file = chooseFile(chooser, false, "open");
        Settings.changeLookAndFeelProgram();
        return file;
    }

        
    
/* PRIVATE METHODS ========================================================== */
    
    private static JFileChooser createChooser(File directory, String title,
                                              String extension, String method) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(directory);
        chooser.setDialogTitle(title);
        setFileSelectionMode(chooser, method);
        setApproveButtonText(chooser, method);
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
    
    private static File chooseFile(JFileChooser chooser,
                                   boolean createFile, String method) {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile(chooser, createFile, method);
        } else {
            return null;
        }
    }
    
    private static File getSelectedFile(JFileChooser chooser,
                                        boolean createFile, String method) {
        File file = chooser.getSelectedFile();
        if (!file.exists()) {
            if(createFile) {
                createFile(file, method);
                return file;
            } else {
                return null;
            }
        } else {
            return file;
        }
    }
    
    private static void createFile(File file, String method) {
        try {
            if (method == "selectDirectory") {
                file.mkdir();
            } else {
                Files.createFile(file.toPath());
            }
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void setApproveButtonText(JFileChooser chooser, String method) {
        String buttonText;
        if (method == "selectFile" || method == "selectDirectory") {
            buttonText = "Select";
        } else if (method == "saveAs") {
            buttonText = "Save";
        } else {
            buttonText = "Open";
        }
        chooser.setApproveButtonText(buttonText);
    }
    
    private static void setFileSelectionMode(JFileChooser chooser, String method) {
        if (method == "selectDirectory") {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
    }

}
