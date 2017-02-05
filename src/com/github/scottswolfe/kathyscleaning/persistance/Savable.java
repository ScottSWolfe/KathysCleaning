package com.github.scottswolfe.kathyscleaning.persistance;

public interface Savable {

    /**
     * Saves data to a text file.
     * 
     * @return true if successful save, false otherwise
     */
    public void saveToFile();
    
    /**
     * Loads data from a text file.
     * 
     * @return true if successful load, false otherwise
     */
    public void loadFromFile();
    
}
