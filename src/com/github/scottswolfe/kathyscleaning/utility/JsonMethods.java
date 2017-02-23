package com.github.scottswolfe.kathyscleaning.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class JsonMethods {
    
/* CLASS VARIABLES ========================================================== */
    
    /**
     * The Gson object used in all of JsonMethods methods
     */
    private static Gson gson = new Gson();
    
    
    
/* PUBLIC METHODS =========================================================== */
    
    /**
     * Saves the given object to the given file in JSON format
     * 
     * @param data the object to be jsonified
     * @param type the class of the given object
     * @param file the file to which to save the object's data
     */
    public static void saveToFileJSON(
                        Object data, Class<?> type, File file) {
        
        saveToFile(data, type, file, 0);
    }
    
    /**
     * Saves the given object to the given file in JSON format
     * 
     * @param data the object to be jsonified
     * @param type the class of the given object
     * @param file the file to which to save the object's data
     * @param lineNumber the line number on which to write the data
     */
    public static void saveToFileJSON(Object data, Class<?> type,
                                      File file, int lineNumber) {
        
        saveToFile(data, type, file, lineNumber);
    }
    
    /**
     * Returns an object with data parsed from the given JSON file
     * 
     * @param type the class of object to return
     * @param file the file to parse
     * @return the new object
     */
    public static Object loadFromFileJSON(Class<?> type, File file) {
        try {
            Object data = type.newInstance();
            Scanner input = new Scanner(file);
            String json = input.nextLine();
            data = gson.fromJson(json, type);
            input.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
/* PRIVATE METHODS ========================================================== */
    
    private static void saveToFile(Object data,
                            Class<?> type, File file, int lineNumber) {
        
        String json = gson.toJson(data, type);
        List<String> lines = getFileLines(file, lineNumber);
        writeLinesAndJson(json, lines, file, lineNumber); 
    }
    
    private static List<String> getFileLines(File file, int lineNumber) {
        List<String> lines;
        if (lineNumber > 1) {
            lines = readFile(file);
        } else {
            lines = new ArrayList<>();
        }
        return lines;
    }
    
    private static List<String> readFile(File file) {
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return lines;
    }
    
    private static void writeLinesAndJson(String json, List<String> lines,
                                          File file, int lineNumber) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < Math.max(lineNumber, lines.size()); i++) {
                if (i == lineNumber) {
                    bw.write(json);
                } else {
                    bw.write(lines.get(i));
                }
            }
            bw.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}
