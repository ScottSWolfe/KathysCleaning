package com.github.scottswolfe.kathyscleaning.general.model;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WorkerList implements Iterable<Worker> {

    /**
     * Array of workers.
     */
    private List<Worker> workers;

    public static WorkerList from(final List<List<String>> workerNames) {
        return new WorkerList(
            workerNames.stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
        );
    }

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

    public List<String> getWorkerNames() {
        return workers.stream()
            .map(Worker::getName)
            .collect(Collectors.toList());
    }

    public List<String> getSelectedWorkerNames() {
        return workers.stream()
            .filter(Worker::isSelected)
            .map(Worker::getName)
            .collect(Collectors.toList());
    }

    public List<List<Pair<String, Boolean>>> getAsWorkerSelectionGrid(final int rowCount, final int columnCount) {
        int workerCount = 0;
        final List<List<Pair<String, Boolean>>> listOfRows = new ArrayList<>();
        for (int row = 0; row < rowCount; row++) {
            final List<Pair<String, Boolean>> listOfColumns = new ArrayList<>();
            for (int column = 0; column < columnCount; column++) {
                listOfColumns.add(Pair.of(
                    workers.get(workerCount).getName(),
                    workers.get(workerCount).isSelected())
                );
                workerCount++;
            }
            listOfRows.add(listOfColumns);
        }
        return listOfRows;
    }

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
            e.printStackTrace();
            return workers;
        }
    }

    /**
     * Returns a string array of the workers.
     */
    public List<Worker> getWorkers() {
        return workers;
    }

    /**
     * @param workers the default_workers to set
     */
    public void setWorkers(ArrayList<Worker> workers) {
        this.workers = workers;
    }

    /**
     * @param workers the default_workers to set
     */
    public void setWorkers(WorkerList workers) {
        this.workers = workers.getWorkers();
    }

    @Override
    public Iterator<Worker> iterator() {
        return workers.iterator();
    }
}
