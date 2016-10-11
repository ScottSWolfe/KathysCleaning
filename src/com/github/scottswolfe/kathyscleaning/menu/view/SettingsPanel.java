package com.github.scottswolfe.kathyscleaning.menu.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.github.scottswolfe.kathyscleaning.menu.controller.SettingsPanelController;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;

import net.miginfocom.swing.MigLayout;


/**
 * This panel allows the user to adjust various settings:
 *      -Default Excel Template
 *      -Default Save Location for generated Excel Document
 *      -Default Auto-Entry Data
 *      -Font Size
 */
@SuppressWarnings("serial")
public class SettingsPanel extends JPanel {	
	
	
/* FIELDS =================================================================== */
    
    /**
     * The frame containing this panel.
     */
	JFrame settingsFrame;
	

	/**
	 * The controller for this panel.
	 */
	SettingsPanelController controller;
	
	
	
	
/* COMPONENTS =============================================================== */
	
	JTabbedPane jtp;
	JScrollPane[] jsp;
	JPanel[] p;
	
	JLabel header_label;
	
	JLabel excel_tag_label;
	JTextField excel_selection_field;
	JButton excel_view_button;
	JButton excel_edit_button;
	
	JLabel save_tag_label;
	JTextField save_selection_field;
	JButton save_view_button;
	JButton save_edit_button;
	
	JLabel edit_week_label;
	JButton edit_wkA_button;
	JButton edit_wkB_button;
	
	JLabel edit_worker_label;
	JButton edit_houses_button;
	JButton edit_covenant_button;
	
	JSeparator hseparator1;
	JSeparator hseparator2;
	JSeparator hseparator3;
	JSeparator hseparator4;
	JSeparator hseparator5;
	
	JLabel font_size_tag_label;
	JSlider font_size_slider;
	
	JLabel color_label;
	JComboBox<String> color_combobox;
	
	JButton cancel_button;
	JButton submit_button;
	
	
	
/* CONSTRUCTOR ============================================================== */
	
	public SettingsPanel(JFrame frame) {
		
		settingsFrame = frame;
		
		controller = new SettingsPanelController(this, settingsFrame);
				
		setLayout( new MigLayout("insets 0") );
		setBackground( Settings.BACKGROUND_COLOR );
		
		header_label = new JLabel();
		header_label.setText("Settings");
		header_label.setFont(
		        header_label.getFont().deriveFont(Settings.HEADER_FONT_SIZE));
		
		JPanel excel_panel = createExcelPanel();
		JPanel save_loc_panel = createSaveLocPanel();
		JPanel week_button_panel = createWeekButtonPanel();
		JPanel worker_button_panel = createWorkerButtonPanel();
		JPanel appearance_panel = createAppearancePanel();
		JPanel continue_panel = createContinuePanel();
		
		hseparator1 = new JSeparator(SwingConstants.HORIZONTAL);
		hseparator2 = new JSeparator(SwingConstants.HORIZONTAL);
		hseparator3 = new JSeparator(SwingConstants.HORIZONTAL);
		hseparator4 = new JSeparator(SwingConstants.HORIZONTAL);
		hseparator5 = new JSeparator(SwingConstants.HORIZONTAL);
		
		add(header_label, "gapleft 5, wrap 15, growx");
		
		
		
		JPanel scroll_panel = new JPanel();
		scroll_panel.setLayout(new MigLayout());
		scroll_panel.setBackground(Settings.BACKGROUND_COLOR);
		
		scroll_panel.add(excel_panel, "wrap 10, growx");
		scroll_panel.add(hseparator1, "wrap 10, growx");
		scroll_panel.add(save_loc_panel, "wrap 10, growx");
		scroll_panel.add(hseparator2, "wrap 10, growx");
		scroll_panel.add(week_button_panel, "wrap 10, growx");
		scroll_panel.add(hseparator3, "wrap 10, growx");
		scroll_panel.add(worker_button_panel, "wrap 10, growx");
		scroll_panel.add(hseparator4, "wrap 10, growx");
		scroll_panel.add(appearance_panel, "wrap 30, growx");
		
        scroll_panel.add(continue_panel, "growx");
        scroll_panel.add(continue_panel, "growx");
		
		JScrollPane sp = new JScrollPane(scroll_panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBackground( Settings.BACKGROUND_COLOR );
		sp.setBorder(BorderFactory.createEmptyBorder());
		
		add(sp, "wrap, grow");
		
		
		
	}
	
	
/* PRIVATE CONSTRUCTION METHDOS ============================================= */
	
	private JPanel createExcelPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout() );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		excel_tag_label = new JLabel();
		excel_tag_label.setText( "Excel Template: " );
		excel_tag_label.setFont(
		        excel_tag_label.getFont().deriveFont(Settings.FONT_SIZE) );
		
		excel_selection_field = new JTextField();
		excel_selection_field.setText(
		        Settings.getExcelTemplateFile().getName());
		excel_selection_field.setFont(
		        excel_selection_field.getFont().deriveFont(Settings.FONT_SIZE));
		excel_selection_field.setEditable( false );
		excel_selection_field.setColumns(15);
		
		excel_view_button = new JButton();
		excel_view_button.setText( "View" );
		excel_view_button.setFont(
		        excel_view_button.getFont().deriveFont(Settings.FONT_SIZE) );
		excel_view_button.addActionListener(controller.new ViewExcelListener());
		
		excel_edit_button = new JButton();
		excel_edit_button.setText( "Change" );
		excel_edit_button.setFont(
		        excel_view_button.getFont().deriveFont(Settings.FONT_SIZE));
		excel_edit_button.addActionListener(
		        controller.new ChangeFileListener());
		
		panel.add(excel_tag_label, "cell 0 0, growx");
		panel.add(excel_selection_field, "cell 0 1, growx");
		panel.add(excel_view_button, "cell 0 2, growx");
		panel.add(excel_edit_button, "cell 0 2, growx");
		
		return panel;
	}

	private JPanel createSaveLocPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout() );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		save_tag_label = new JLabel();
		save_tag_label.setText( "Save Location: " );
		save_tag_label.setFont( save_tag_label.getFont().deriveFont(Settings.FONT_SIZE) );
		
		save_selection_field = new JTextField();
		save_selection_field.setText(Settings.getExcelSaveLocation().getName());
		save_selection_field.setFont(
		        save_selection_field.getFont().deriveFont(Settings.FONT_SIZE));
		save_selection_field.setEditable( false );
		save_selection_field.setColumns(15);
		
		save_view_button = new JButton();
		save_view_button.setText("View");
		save_view_button.setFont(
		        save_view_button.getFont().deriveFont(Settings.FONT_SIZE) );
		save_view_button.addActionListener(
		        controller.new ViewFolderListener( ));
		
		save_edit_button = new JButton();
		save_edit_button.setText( "Change" );
		save_edit_button.setFont(
		        save_edit_button.getFont().deriveFont(Settings.FONT_SIZE) );
		save_edit_button.addActionListener(
		        controller.new ChangeFolderListener());
		
		panel.add(save_tag_label, "cell 0 0, growx");
		panel.add(save_selection_field, "cell 0 1, growx");
		panel.add(save_view_button, "cell 0 2, growx");
		panel.add(save_edit_button, "cell 0 2, growx");
		
		return panel;
	}
	
	private JPanel createWeekButtonPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("fill") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		edit_week_label = new JLabel();
		edit_week_label.setText("Edit Default Week Data: ");
		edit_week_label.setFont(
		        edit_week_label.getFont().deriveFont(Settings.FONT_SIZE) );
		
		edit_wkA_button = new JButton();
		edit_wkA_button.setText( "Week A" );
		edit_wkA_button.setFont(edit_wkA_button.getFont().deriveFont(
		        Settings.FONT_SIZE) );
		//edit_wkA_button.setBackground(Settings.MAIN_COLOR);
		//edit_wkA_button.setForeground( Settings.FOREGROUND_COLOR );
		edit_wkA_button.addActionListener(controller.new
		        EditWeekListener(Settings.WEEK_A));
		
		edit_wkB_button = new JButton();
		edit_wkB_button.setText( "Week B" );
		edit_wkB_button.setFont(edit_wkB_button.getFont().deriveFont(
		        Settings.FONT_SIZE) );
		//edit_wkB_button.setBackground(Settings.MAIN_COLOR);
		//edit_wkB_button.setForeground( Settings.FOREGROUND_COLOR );
		edit_wkB_button.addActionListener(controller.new
		        EditWeekListener(Settings.WEEK_B));
		
		panel.add(edit_week_label, "span 2, wrap, growx");
		panel.add(edit_wkA_button, "growx");
		panel.add(edit_wkB_button, "growx");
		
		return panel;
	}
	
	private JPanel createWorkerButtonPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout("fill") );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		edit_worker_label = new JLabel();
		edit_worker_label.setText( "Edit Default Workers: ");
		edit_worker_label.setFont( edit_worker_label.getFont().deriveFont(Settings.FONT_SIZE) );
		
		edit_houses_button = new JButton();
		edit_houses_button.setText( "Houses" );
		edit_houses_button.setFont(
		        edit_houses_button.getFont().deriveFont(Settings.FONT_SIZE));
		edit_houses_button.addActionListener(
		        controller.new EditWorkerListener(
		                Settings.HOUSES_WORKERS, settingsFrame));
		
		edit_covenant_button = new JButton();
		edit_covenant_button.setText( "Covenant" );
		edit_covenant_button.setFont(
		        edit_covenant_button.getFont().deriveFont(Settings.FONT_SIZE) );
		edit_covenant_button.addActionListener(
		        controller.new EditWorkerListener(
		                Settings.COVENANT_WORKERS, settingsFrame));
		
		panel.add(edit_worker_label, "span 2, wrap, growx");
		panel.add(edit_houses_button, "growx");
		panel.add(edit_covenant_button, "growx");
		
		return panel;
	}

	private JPanel createAppearancePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("center"));
		panel.setBackground(Settings.BACKGROUND_COLOR);
		
		font_size_tag_label = new JLabel();
		font_size_tag_label.setText("Font Size: ");
		font_size_tag_label.setFont(font_size_tag_label.getFont().deriveFont(
		        Settings.FONT_SIZE));
		
		font_size_slider = new JSlider();
		font_size_slider.setBackground( Settings.BACKGROUND_COLOR );
		font_size_slider.setMaximum(5);
		font_size_slider.setMinimum(1);
		font_size_slider.setValue(Settings.getFontSizeFactor());
		font_size_slider.setOrientation(SwingConstants.HORIZONTAL);
		font_size_slider.setSnapToTicks(true);
		font_size_slider.setMajorTickSpacing(1);
		font_size_slider.setPaintTicks(true);
		
		panel.add(font_size_tag_label, "growx, wrap");
		panel.add(font_size_slider, "center, growx");
		
		return panel;		
	}
	
	private JPanel createContinuePanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout(new MigLayout("align right"));
		panel.setBackground(Settings.BACKGROUND_COLOR);
		
		cancel_button = new JButton();
		cancel_button.setBackground(Settings.MAIN_COLOR);
		cancel_button.setForeground(Settings.FOREGROUND_COLOR);
		cancel_button.setFont(cancel_button.getFont().deriveFont(
		        Settings.FONT_SIZE));
		cancel_button.setText("Cancel");
		cancel_button.addActionListener(controller.new CancelButtonListener() );
		
		submit_button = new JButton();
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground(Settings.FOREGROUND_COLOR);
		submit_button.setFont(submit_button.getFont().deriveFont(
		        Settings.FONT_SIZE));
		submit_button.setText("Submit");
		submit_button.addActionListener(controller.new SubmitButtonListener() );
		
		panel.add(cancel_button, "growx");
		panel.add(submit_button, "growx");
		
		return panel;
	}

	
	


	
	
	
	
/* GETTERS/SETTERS ========================================================== */
	
	/**
	 * Set excel selection textfield.
	 */
	public void setExcelSelectionTextfield(String filename) {
	    excel_selection_field.setText(filename);
	}
	
	/**
	 * Set excel save location textfield.
	 */
	public void setExcelSaveLocationTextfield(String filename) {
	    save_selection_field.setText(filename);
	}
	
	/**
	 * Get the value of the font size slider
	 */
	public int getFontSizeSliderValue() {
	    return font_size_slider.getValue();
	}
	
	/**
	 * Set the settingsFrame.
	 */
	public void setSettingsFrame(JFrame frame) {
	    settingsFrame = frame;
	}
	
	/**
     * Get the settingsFrame.
     */
    public JFrame getSettingsFrame() {
        return settingsFrame;
    }
    
    /**
     * Set the controller.
     */
    public void setController(SettingsPanelController controller) {
        this.controller = controller;
    }
    
    /**
     * Get the controller for this panel.
     */
    public SettingsPanelController getController() {
        return controller;
    }
	
}
