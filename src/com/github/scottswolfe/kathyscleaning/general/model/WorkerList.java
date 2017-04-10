package com.github.scottswolfe.kathyscleaning.general.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.JOptionPane;




public class WorkerList implements Iterable<Worker> {
	
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
    
    
    
/* FIELDS =================================================================== */
	
    /**
     * Array of workers.
     */
	private List<Worker> workers;
			
	
	
/* CONSTRUCTOR ============================================================== */
	
	
	public WorkerList() {
	    workers = new ArrayList<>();
	}
	
	
	public WorkerList(ArrayList<Worker> workers)  {
	    
	    this.workers = new ArrayList<>();
	    if (workers == null) {
	        throw new IllegalArgumentException("Argument is null.");
	    }
	    //this.workers = workers;
	    for (Worker worker : workers) {
	        if (worker == null) {
	            throw new IllegalArgumentException("A worker is null.");
	        }
	        this.workers.add(worker);
	    }
	}
	
	public WorkerList(File file) {
	    this.workers = readWorkers(file);
	}
	
	public WorkerList(List<String> workerNames) {
	    workers = new ArrayList<>();
	    for (String name : workerNames) {
	        workers.add(new Worker(name));
	    }
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
	public boolean add(Worker worker) {
	    
	    if (worker == null) {
	        throw new IllegalArgumentException("Argument is null.");
	    }
	    if (workers.contains(worker)) {
	        return false;
	    }
	    return workers.add(worker);
	}
	
	/**
     * Adds a worker with the given name to the list.
     */
    public boolean add(String name) {
        return workers.add(new Worker(name));
    }
		
	/**
	 * Removes a worker from the list.
	 */
	public boolean remove(Worker worker) {
	    if (worker == null) {
            throw new IllegalArgumentException("Argument is null.");
        }
        return workers.remove(worker);
	}
	
	/**
	 * Gets a specific index from the list.
	 */
	public Worker get(int index) {
	    
	    if (index < 0 || index >= workers.size()) {
	        throw new IndexOutOfBoundsException();
	    }
	    
	    return workers.get(index);
	}
	
	/**
     * Gets the workers name for the given index.
     */
	public String getName(int index) {
        if (index < 0 || index >= workers.size()) {
            throw new IndexOutOfBoundsException();
        }
        return workers.get(index).getName();
	}

	public boolean containsName(Worker worker) {
	    for (Worker w : workers) {
	        if (w.getName().equals(worker.getName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	
/* PRIVATE METHODS ========================================================== */
	
	private static List<Worker> readWorkers(File file) {
		
	    List<Worker> workers = new ArrayList<>();
	    
		try {   		
    		Scanner input = new Scanner(file);
    		while(input.hasNext()) {
    			workers.add(new Worker(input.nextLine()));
    		}
    		input.close();
    		return workers; 
    		
		} catch (IOException e) {
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
    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * @param default_workers the default_workers to set
     */
    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }
    
    /**
     * @param default_workers the default_workers to set
     */
    public void setWorkers(WorkerList workers) {
        this.workers = workers.getWorkers();
    }

    @Override
    public Iterator<Worker> iterator() {
        return workers.iterator();
    }
	
}
