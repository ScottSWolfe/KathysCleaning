package com.github.scottswolfe.kathyscleaning.general.view;

import com.github.scottswolfe.kathyscleaning.general.controller.FlexibleFocusListener;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  This panel is used to update the names in a list of workers.
 */
public class EditWorkersPanel extends JPanel {

    public EditWorkersPanel(
		List<String> currentWorkerNames,
		List<String> availableWorkerNames,
		Runnable onCancel,
		Consumer<List<String>> onSubmit
	) {
		final List<JComboBox<String>> workerComboBoxes = createWorkerComboBoxes(
			currentWorkerNames, availableWorkerNames
		);
		final JButton cancelButton = createCancelButton(onCancel);
		final JButton submitButton = createSubmitButton(onSubmit, workerComboBoxes);

		setLayout(new MigLayout());
		setBackground(Settings.BACKGROUND_COLOR);

		for (JComboBox<String> workerComboBox : workerComboBoxes) {
			add(workerComboBox, "wrap 10");
		}
		add(cancelButton, "split 2");
		add(submitButton, "");

		addFlexibleFocusListeners(workerComboBoxes);
	}

	private List<JComboBox<String>> createWorkerComboBoxes(
		List<String> currentWorkerNames,
		List<String> availableWorkerNames
	) {
		final List<JComboBox<String>> workerComboBoxes = new ArrayList<>();

		for (String currentWorkerName : currentWorkerNames) {

			JComboBox<String> workerComboBox = new JComboBox<>();
			workerComboBox.setEditable(true);
			workerComboBox.setSize(10, UNDEFINED_CONDITION);
			workerComboBox.setFont(workerComboBox.getFont().deriveFont(Settings.FONT_SIZE));

			// Add combo box options
			Set<String> availableNamesAdded = new HashSet<>();

			String emptyChoice = "";
			workerComboBox.addItem(emptyChoice);
			availableNamesAdded.add(emptyChoice);

			List<String> allWorkerNames = Stream.concat(
				availableWorkerNames.stream(),
				currentWorkerNames.stream()
			).collect(Collectors.toList());

			for (String workerName : allWorkerNames) {
				if (!availableNamesAdded.contains(workerName)) {
					workerComboBox.addItem(workerName);
					availableNamesAdded.add(workerName);
				}
			}

			workerComboBox.setSelectedItem(currentWorkerName);

			workerComboBoxes.add(workerComboBox);
		}

		return workerComboBoxes;
	}

	private JButton createCancelButton(Runnable onCancel) {
		final JButton cancelButton = new JButton("Cancel");
		cancelButton.setFont( cancelButton.getFont().deriveFont( Settings.FONT_SIZE ) );
		cancelButton.setBackground(Settings.MAIN_COLOR);
		cancelButton.setForeground( Settings.FOREGROUND_COLOR );
		cancelButton.addActionListener((event) -> onCancel.run());
		return cancelButton;
	}

	private JButton createSubmitButton(Consumer<List<String>> onSubmit, List<JComboBox<String>> workerComboBoxes) {
		final JButton submitButton = new JButton("Submit");
		submitButton.setFont( submitButton.getFont().deriveFont( Settings.FONT_SIZE ) );
		submitButton.setBackground(Settings.MAIN_COLOR);
		submitButton.setForeground( Settings.FOREGROUND_COLOR );
		submitButton.addActionListener((event) -> onSubmit.accept(getSelectedWorkers(workerComboBoxes)));
		return submitButton;
	}

	private void addFlexibleFocusListeners (List<JComboBox<String>> workerComboBoxes) {
		for (int i = 0; i < workerComboBoxes.size(); i++) {
			
			JComboBox<String> up_cb = null;
			JComboBox<String> down_cb = null;
			
			if (i > 0) {
				up_cb = workerComboBoxes.get(i - 1);
			}
			if (i < workerComboBoxes.size() - 1) {
				down_cb = workerComboBoxes.get(i + 1);
			}
			
			workerComboBoxes.get(i).getEditor().getEditorComponent().addFocusListener(
				new FlexibleFocusListener(
					workerComboBoxes.get(i),
					FlexibleFocusListener.COMBOBOX,
					null,
					null,
					up_cb,
					down_cb,
					null
				)
			);
		}
	}

    private List<String> getSelectedWorkers(List<JComboBox<String>> workerComboBoxes) {
		return workerComboBoxes.stream()
			.map(JComboBox::getSelectedItem)
			.map(String::valueOf)
			.collect(Collectors.toList());
    }
}	
