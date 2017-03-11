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
    

    public final static String XLSX = ".xlsx";
    public final static String TXT = ".txt";
    
/* PRIVATE CLASS VARIABLES ================================================== */
    
    private enum Method {
        selectFile, selectDirectory, saveAs, open;
    }
        
    private final static String selectFileTitle = "Select a file";
    private final static String selectDirectoryTitle = "Select a folder";
    private final static String saveAsTitle = "Save As ...";
    private final static String openTitle = "Select a file to open";
    
    
    
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
    public static File selectFile(File directory, String extension) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, selectFileTitle,
                                             extension, Method.selectFile); 
        File file = chooseFile(chooser, false, Method.selectFile);
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
    public static File selectDirectory(File directory) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, selectDirectoryTitle,
                                             null, Method.selectDirectory); 
        File file = chooseFile(chooser, true, Method.selectDirectory);
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
     * @param fileName the suggested name for the file
     * @return the chosen or created file or null if no file is chosen
     */
    public static File saveAs(File directory, String fileName, String extension) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, saveAsTitle,
                                             extension, Method.saveAs);
        setSelectedFile(chooser, directory, fileName, extension);
        File file = chooseFile(chooser, true, Method.saveAs);
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
    public static File open(File directory, String extension) {
        Settings.changeLookAndFeelSystem();
        JFileChooser chooser = createChooser(directory, openTitle,
                                             extension, Method.open); 
        File file = chooseFile(chooser, false, Method.open);
        Settings.changeLookAndFeelProgram();
        return file;
    }

        
    
/* PRIVATE METHODS ========================================================== */
    
    private static JFileChooser createChooser(File directory, String title,
                                              String extension, Method method) {
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
                                   boolean createFile, Method method) {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return getSelectedFile(chooser, createFile, method);
        } else {
            return null;
        }
    }
    
    private static File getSelectedFile(JFileChooser chooser,
                                        boolean createFile, Method method) {
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
    
    private static void createFile(File file, Method method) {
        try {
            if (method == Method.selectDirectory) {
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
    
    private static void setApproveButtonText(JFileChooser chooser, Method method) {
        String buttonText;
        if (method == Method.selectFile || method == Method.selectDirectory) {
            buttonText = "Select";
        } else if (method == Method.saveAs) {
            buttonText = "Save";
        } else {
            buttonText = "Open";
        }
        chooser.setApproveButtonText(buttonText);
    }
    
    private static void setFileSelectionMode(JFileChooser chooser, Method method) {
        if (method == Method.selectDirectory) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
    }
    
    private static void setSelectedFile(JFileChooser chooser, File directory,
                                        String fileName, String extension) {
        if (directory == null || fileName == null || extension == null) {
            return;
        }
        String fullFileName = directory.getAbsolutePath() + "/" + fileName + extension; 
        chooser.setSelectedFile(new File(fullFileName));
    }

}
