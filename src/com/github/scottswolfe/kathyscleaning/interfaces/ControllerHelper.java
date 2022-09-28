package com.github.scottswolfe.kathyscleaning.interfaces;

import java.io.File;
import java.util.List;

import javax.annotation.Nonnull;
import javax.swing.JComponent;

public interface ControllerHelper<View extends JComponent, Model> {

    /**
     * Creates the model with default values.
     */
    Model initializeModel();

    /**
     * Creates a view with default values.
     */
    View initializeView();

    /**
     * Called from GeneralController::readViewAndWriteToFile. Allows specific forms to do
     * additional form-specific logic as needed.
     */
    default void readViewAndWriteToFileHook(View view) {}

    /**
     * Reads user's input in the view into a model object
     *
     * @param view the view containing the user's input
     * @return the model object
     */
    public Model readViewIntoModel(View view);

    /**
     * Writes the given model into the given view
     *
     * @param model the model object to be written into the view
     * @param view the view in which to write the model
     */
    public void writeModelToView(Model model, View view);

    /**
     * Saves the given model to the given file
     *
     * @param model the data to be saved
     */
    public void saveModelToFile(Model model, File file);

    /**
     * Loads a model object from the given file
     *
     * @param file the file from which to load the object
     */
    public Model loadFromFile(File file);

    /**
     * Updates the worker names on the model
     */
    void updateWorkersOnModel(@Nonnull final Model model, @Nonnull final List<String> workerNames);
}
