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
     * Saves the given object to the given line in the file in JSON format
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
     * Returns an object with data parsed from the given JSON file and
     * 
     * @param type the class of object to return
     * @param file the file to parse
     * @return the new object
     */
    public static Object loadFromFileJSON(Class<?> type, File file) {
        return loadFromFile(type, file, 0);
    }
    
    /**
     * Returns an object with data parsed from the given line in the JSON file
     * 
     * @param type the class of object to return
     * @param file the file to parse
     * @param lineNumber the line number of the JSON object to parse
     * @return the new object
     */
    public static Object loadFromFileJSON(Class<?> type,
                                          File file, int lineNumber) {
        return loadFromFile(type, file, lineNumber);
    }
    
    
/* PRIVATE METHODS ========================================================== */
    
    private static void saveToFile(Object data,
                            Class<?> type, File file, int lineNumber) {
        
        String json = gson.toJson(data, type);
        List<String> lines = getFileLines(file);
        writeLinesAndJson(json, lines, file, lineNumber); 
    }
    
    private static Object loadFromFile(Class<?> type, File file, int lineNumber) { 
        try {
            Scanner input = new Scanner(file);
            String json = getLine(input, lineNumber);
            Object data = gson.fromJson(json, type);
            input.close();
            return data;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static List<String> getFileLines(File file) {
        List<String> lines = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                lines.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            // do nothing
        }
        return lines;
    }
    
    private static void writeLinesAndJson(String json, List<String> lines,
                                          File file, int lineNumber) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < Math.max(lineNumber + 1, lines.size()); i++) {
                if (i == lineNumber) {
                    bw.write(json);
                } else {
                    if (i < lines.size()) {
                        bw.write(lines.get(i));
                    } else {
                        bw.write("{}");
                    }
                }
                bw.write('\n');
            }
            bw.close(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getLine(Scanner input, int lineNumber) {
        for (int i = 0; i < lineNumber && input.hasNext(); i++) {
            input.nextLine();
        }
        if (input.hasNext()) {
            return input.nextLine();   
        } else {
            return "{}";
        }
    }
        
}
