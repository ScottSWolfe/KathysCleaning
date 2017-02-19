package com.github.scottswolfe.kathyscleaning.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;

public class JsonMethods {
    
    private static Gson gson = new Gson();
    
    /**
     * Saves the given object to the given file in JSON format
     * 
     * @param data the object to be jsonified
     * @param type the class of the given object
     * @param file the file to which to save the object's data
     */
    public static void saveToFileJSON(
                        Object data, Class<?> type, File file) {
        
        String json = gson.toJson(data, type);
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(json);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    
    
    
}
