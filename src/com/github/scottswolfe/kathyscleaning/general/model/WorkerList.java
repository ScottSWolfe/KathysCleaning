package com.github.scottswolfe.kathyscleaning.general.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import javax.swing.JOptionPane;




public class WorkerList implements Iterable<String> {
	
/* CONSTANTS ================================================================ */
    
    /**
     * Save file with list of default house workers.
     */
    public static final File HOUSE_WORKERS = new File(
            (System.getProperty("user.dir") + "\\save\\HouseWorkerSaveFile"));
    
    /**
     * Save file with list of default Covenant workers.
     */
    public static final File COVENANT_WORKERS = new File(
            (System.getProperty("user.dir") +
                    "\\save\\CovenantWorkerSaveFile"));
    
    /**
     * Save file with list of default Weekend workers.
     */
    public static final File WEEKEND_WORKERS = new File(
            (System.getProperty("user.dir") + "\\save\\WeekendWorkSaveFile"));

    
    
    
/* FIELDS =================================================================== */
	
    /**
     * Array of workers.
     */
	private ArrayList<String> workers;
		
	
	
	
/* CONSTRUCTOR ============================================================== */
	
	
	public WorkerList() {
	    workers = new ArrayList<>();
	}
	
	
	public WorkerList(ArrayList<String> workers)  {
	    
	    this.workers = new ArrayList<String>();
	    if (workers == null) {
	        throw new IllegalArgumentException("Argument is null.");
	    }
	    //this.workers = workers;
	    for (String s : workers) {
	        if (s == null) {
	            throw new IllegalArgumentException("A string is null.");
	        }
	        this.workers.add(s);
	    }
	}
	
	
	public WorkerList(File file) {
	    this(readWorkers(file));
	}
	
	
	

/* PUBLIC METHODS =========================================================== */
	
	/**
	 * Returns the number of workers in the list.
	 */
	public int size() {
	    return workers.size();
	}
	
	/**
	 * Adds a worker to the list.
	 */
	public boolean add(String worker) {
	    
	    if (worker == null) {
	        throw new IllegalArgumentException("Argument is null.");
	    }
	    if (workers.contains(worker)) {
	        return false;
	    }
	    return workers.add(worker);
	}
	
	/**
	 * Removes a worker from the list.
	 */
	public boolean remove(String worker) {
	    if (worker == null) {
            throw new IllegalArgumentException("Argument is null.");
        }
        return workers.remove(worker);
	}
	
	/**
	 * Gets a specific index from the list.
	 */
	public String get(int index) {
	    
	    if (index < 0 || index >= workers.size()) {
	        throw new IndexOutOfBoundsException();
	    }
	    
	    return workers.get(index);
	}
	
	
	
/* PRIVATE METHODS ========================================================== */
	
	private static ArrayList<String> readWorkers(File file) {
		
	    ArrayList<String> workers = new ArrayList<String>();
	    
		try {   		
    		Scanner input = new Scanner(file);
    		while(input.hasNext()) {
    			workers.add(input.nextLine());
    		}
    		input.close();
    		return workers; 
    		
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error: Could not find file: " +
			        file.getAbsolutePath(), "Error Reading Save File",
			        JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return workers;
		}
	}

	
	
	
/* GETTERS/SETTERS ========================================================== */
	
    /**
     * Returns a string array of the workers.
     */
    public ArrayList<String> getWorkers() {
        return workers;
    }

    /**
     * @param default_workers the default_workers to set
     */
    public void setWorkers(ArrayList<String> workers) {
        this.workers = workers;
    }
    
    /**
     * @param default_workers the default_workers to set
     */
    public void setWorkers(WorkerList workers) {
        this.workers = workers.getWorkers();
    }

    @Override
    public Iterator<String> iterator() {
        return workers.iterator();
    }
	
	
}
