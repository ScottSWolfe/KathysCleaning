package com.github.scottswolfe.kathyscleaning.persistance;

public interface FileManager {

    /**
     * Saves data to a text file.
     * 
     * @return true if successful save, false otherwise
     */
    public boolean saveToFile();
    
    /**
     * Loads data from a text file.
     * 
     * @return true if successful load, false otherwise
     */
    public boolean loadFromFile();
    
}
