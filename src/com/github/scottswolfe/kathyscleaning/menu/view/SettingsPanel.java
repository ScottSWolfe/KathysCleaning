package com.github.scottswolfe.kathyscleaning.menu.view;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.github.scottswolfe.kathyscleaning.general.controller.FrameCloseListener;
import com.github.scottswolfe.kathyscleaning.general.controller.MainWindowListener;
import com.github.scottswolfe.kathyscleaning.general.controller.StaticMethods;
import com.github.scottswolfe.kathyscleaning.general.model.DefaultWorkerData;
import com.github.scottswolfe.kathyscleaning.general.view.TabbedPane;
import com.github.scottswolfe.kathyscleaning.menu.model.Settings;
import com.github.scottswolfe.kathyscleaning.submit.controller.TabChangeListener;
import com.github.scottswolfe.kathyscleaning.submit.view.DayPanel;

import net.miginfocom.swing.MigLayout;


public class SettingsPanel extends JPanel {

	/**
	 * This panel allows the user to adjust various settings:
	 * 		-Default Excel Template
	 * 		-Default Save Location for generated Excel Document
	 * 		-Default Auto-Entry Data
	 * 		-Font Size
	 * 		-Color Theme
	 */
	
	private static final long serialVersionUID = 7058678465401764742L;
	
	
	
	// FIELDS
	
	JFrame frame;
	JFrame menuFrame;
	public static final File SETTINGS_SAVE_FILE = new File( System.getProperty("user.dir") + "\\save\\SettingstingsSaveFile" );
	
	public static File excel_template_file;
	public static File save_location_file;
	public static int font_size;
	public static String color_theme_selection;
	
	String[] color_theme = {"Default"};  // TODO
	
	public static final int TRUE_MODE = 0;
	public static final int EDIT_MODE = 1;
	
	public static final int WEEK_A = 0;
	public static final int WEEK_B = 1;
	public static final int NEITHER = 2;
	
	public static final File SUBMIT_WEEK_A = new File(System.getProperty("user.dir") + "\\save\\WeekASubmitWeekSaveFile");
	public static final File NEXT_WEEK_A = new File(System.getProperty("user.dir") + "\\save\\WeekANextWeekSaveFile");
	public static final File WEEKEND_WEEK_A = new File(System.getProperty("user.dir") + "\\save\\WeekAWeekendSaveFile");
	public static final File SUBMIT_WEEK_B = new File(System.getProperty("user.dir") + "\\save\\WeekBSubmitWeekSaveFile");
	public static final File NEXT_WEEK_B = new File(System.getProperty("user.dir") + "\\save\\WeekBNextWeekSaveFile");
	public static final File WEEKEND_WEEK_B = new File(System.getProperty("user.dir") + "\\save\\WeekBWeekendSaveFile");
	
	public static final File SAVED_SCHEDULE = new File(System.getProperty("user.dir") + "\\save\\SavedSchedule");
	public static final File COV_WORKER_SAVE = new File(System.getProperty("user.dir") + "\\save\\CovenantWorkerSaveFile");
	
	public static final int HOUSES_WORKERS = 0;
	public static final int COVENANT_WORKERS = 1;
	
	
	
	// COMPONENT FIELDS
	
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
	
	
	
	// CONSTRUCTOR
	public SettingsPanel( JFrame frame, JFrame menuFrame ) {
		
		this.frame = frame;
		this.menuFrame = menuFrame;
		
		
		readSettingstingsSaveFile();		// read SettingstingsSaveFile for saved settings
		
		setLayout( new MigLayout("insets 0") );
		setBackground( Settings.BACKGROUND_COLOR );
		
		header_label = new JLabel();
		header_label.setText("Settingstings");
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
		
		//add(hseparator5, "wrap 30, growx");
        scroll_panel.add(continue_panel, "growx");
        scroll_panel.add(continue_panel, "growx");
		
		JScrollPane sp = new JScrollPane(scroll_panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setBackground( Settings.BACKGROUND_COLOR );
		sp.setBorder(BorderFactory.createEmptyBorder());
		
		//sp.add(scroll_panel);
		add(sp, "wrap, grow");
		
		
		
	}
	/*
	public void paintComponent(Graphics g) {
		g.drawImage(new ImageIcon("Galapagos Island.jpg").getImage(), 0, 0, null);
	}
	*/
	
	
	// PRIVATE CONSTRUCTION METHDOS
	
	private JPanel createExcelPanel() {
		
		JPanel panel = new JPanel();
		panel.setLayout( new MigLayout() );
		panel.setBackground( Settings.BACKGROUND_COLOR );
		
		excel_tag_label = new JLabel();
		excel_tag_label.setText( "Excel Template: " );
		excel_tag_label.setFont( excel_tag_label.getFont().deriveFont(Settings.FONT_SIZE) );
		
		excel_selection_field = new JTextField();
		excel_selection_field.setText( excel_template_file.getName() );
		excel_selection_field.setFont( excel_selection_field.getFont().deriveFont(Settings.FONT_SIZE) ); // TODO need a lighter, smaller font...
		excel_selection_field.setEditable( false );
		excel_selection_field.setColumns(15);
		
		excel_view_button = new JButton();
		excel_view_button.setText( "View" );
		excel_view_button.setFont( excel_view_button.getFont().deriveFont(Settings.FONT_SIZE) );
		//excel_view_button.setBackground(Settings.MAIN_COLOR);
		//excel_view_button.setForeground( Settings.FOREGROUND_COLOR );
		excel_view_button.addActionListener( new ViewExcelListener() );
		
		excel_edit_button = new JButton();
		excel_edit_button.setText( "Change" );
		excel_edit_button.setFont( excel_view_button.getFont().deriveFont(Settings.FONT_SIZE) );
		//excel_edit_button.setBackground(Settings.MAIN_COLOR);
		//excel_edit_button.setForeground( Settings.FOREGROUND_COLOR );
		excel_edit_button.addActionListener( new ChangeFileListener( ) );
		
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
		save_selection_field.setText( save_location_file.getName() );
		save_selection_field.setFont( save_selection_field.getFont().deriveFont(Settings.FONT_SIZE) ); // TODO need a lighter, smaller font...
		save_selection_field.setEditable( false );
		save_selection_field.setColumns(15);
		
		save_view_button = new JButton();
		save_view_button.setText( "View" );
		save_view_button.setFont( save_view_button.getFont().deriveFont(Settings.FONT_SIZE) );
		//save_view_button.setBackground(Settings.MAIN_COLOR);
		//save_view_button.setForeground( Settings.FOREGROUND_COLOR );
		save_view_button.addActionListener( new ViewFolderListener( ) );
		
		save_edit_button = new JButton();
		save_edit_button.setText( "Change" );
		save_edit_button.setFont( save_edit_button.getFont().deriveFont(Settings.FONT_SIZE) );
		//save_edit_button.setBackground(Settings.MAIN_COLOR);
		//save_edit_button.setForeground( Settings.FOREGROUND_COLOR );
		save_edit_button.addActionListener( new ChangeFolderListener() );
		
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
		edit_week_label.setText( "Edit Default Week Data: ");
		edit_week_label.setFont( edit_week_label.getFont().deriveFont(Settings.FONT_SIZE) );
		
		edit_wkA_button = new JButton();
		edit_wkA_button.setText( "Week A" );
		edit_wkA_button.setFont( edit_wkA_button.getFont().deriveFont(Settings.FONT_SIZE) );
		//edit_wkA_button.setBackground(Settings.MAIN_COLOR);
		//edit_wkA_button.setForeground( Settings.FOREGROUND_COLOR );
		edit_wkA_button.addActionListener( new EditWeekListener( WEEK_A ) );
		
		edit_wkB_button = new JButton();
		edit_wkB_button.setText( "Week B" );
		edit_wkB_button.setFont( edit_wkB_button.getFont().deriveFont(Settings.FONT_SIZE) );
		//edit_wkB_button.setBackground(Settings.MAIN_COLOR);
		//edit_wkB_button.setForeground( Settings.FOREGROUND_COLOR );
		edit_wkB_button.addActionListener( new EditWeekListener( WEEK_B ) );
		
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
		edit_houses_button.setFont( edit_houses_button.getFont().deriveFont(Settings.FONT_SIZE) );
		edit_houses_button.addActionListener( new EditWorkerListener( HOUSES_WORKERS, frame ) );
		
		edit_covenant_button = new JButton();
		edit_covenant_button.setText( "Covenant" );
		edit_covenant_button.setFont( edit_covenant_button.getFont().deriveFont(Settings.FONT_SIZE) );
		edit_covenant_button.addActionListener( new EditWorkerListener( COVENANT_WORKERS, frame ) );
		
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
		font_size_slider.setValue(font_size);
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
		cancel_button.addActionListener( new CancelButtonListener() );
		
		submit_button = new JButton();
		submit_button.setBackground(Settings.MAIN_COLOR);
		submit_button.setForeground(Settings.FOREGROUND_COLOR);
		submit_button.setFont(submit_button.getFont().deriveFont(
		        Settings.FONT_SIZE));
		submit_button.setText("Submit");
		submit_button.addActionListener( new SubmitButtonListener() );
		
		panel.add(cancel_button, "growx");
		panel.add(submit_button, "growx");
		
		return panel;
	}
	
	
	
	// PRIVATE METHODS
	
	// read Settingstings_IO for saved files and file locations
	private void readSettingstingsSaveFile () {

		try {
			Scanner input = new Scanner( SETTINGS_SAVE_FILE );
									
			excel_template_file = new File( input.nextLine() );
			save_location_file = new File( input.nextLine() );
			font_size = input.nextInt();
			input.nextLine();
			input.close();
		}
		catch ( Exception e ) {
			// TODO work needed here, lots of possible issues...
			
			excel_template_file = new File("");
			save_location_file = new File("");
			//...
		}
		
	}
	
	
	
	// PUBLIC METHODS
	
	public static File getDefaultExcelFile () {
		
		File excel_template_file;
		try {
			Scanner input = new Scanner(SETTINGS_SAVE_FILE);
			excel_template_file = new File( input.nextLine() );
			input.close();
		}
		catch (Exception e) {
			// TODO
			excel_template_file = new File( "TemplateA" );
		}
		
		return excel_template_file;
		
	}
	
	public static File getDefaultSaveLocation () {
		
		File default_save_location;
		try {
			Scanner input = new Scanner(SETTINGS_SAVE_FILE);
			input.nextLine();
			default_save_location = new File( input.nextLine() );
			input.close();
		}
		catch (Exception e) {
			default_save_location = new File(System.getProperty("user.home"), "Desktop");
		}
		
		return default_save_location;
		
	}
	
	
	
	// LISTENERS
	
	private class ViewExcelListener implements ActionListener {
		
		// ACTION LISTENER
		public void actionPerformed( ActionEvent e ) {
			
			try {
				Desktop.getDesktop().open( excel_template_file );
			} catch (IllegalArgumentException e1) {
					System.out.println("Desktop Open Error");
					JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen document could not be opened");
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				System.out.println("i/o exception");
				e2.printStackTrace();
			} 

		}
		
	}

	
	private class ViewFolderListener implements ActionListener {
		
		// ACTION LISTENER
		public void actionPerformed( ActionEvent e ) {
			
			try {
				Desktop.getDesktop().open( save_location_file );
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen folder could not be viewed.");
			} catch(NullPointerException e2) {
				JOptionPane.showMessageDialog(new JFrame(), "Error: the chosen folder could not be viewed.");
			}
			
		}
		
	}

	
	private class ChangeFileListener implements ActionListener {
		
		// ACTION LISTENER
		public void actionPerformed( ActionEvent e ) {
			
			// Change look and feel to default system look and feel TODO create static method to do this
			try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }
			
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory( new File(System.getProperty("user.home") + "\\Desktop") );
			chooser.setDialogTitle("Choose Default Excel Template");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
			chooser.setFileFilter(filter);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				
				excel_template_file = chooser.getSelectedFile();
				excel_selection_field.setText( excel_template_file.getName() );
				

			} else {
			  // do nothing
			}
			
			
			// change look and feel back to nimbus   TODO create static method to do this
			try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			} catch (Exception e1) {
				// if nimbus is not available, use system default
				try {
	                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
	            }
				
			}
			
		}
		
	}
	
	
	private class ChangeFolderListener implements ActionListener {
		
		// ACTION LISTENER
		public void actionPerformed( ActionEvent e ) {
			
			// Change look and feel to default system look and feel TODO create static method to do this
			try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            }
			
			JFileChooser chooser = new JFileChooser();
			chooser.setCurrentDirectory( new File(System.getProperty("user.home") + "\\Desktop") );
			chooser.setDialogTitle("Choose Save Location");
			//FileNameExtensionFilter filter = new FileNameExtensionFilter("XLSX files", "xlsx");
			//chooser.setFileFilter(filter);
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				
				save_location_file = chooser.getSelectedFile();
				save_selection_field.setText( save_location_file.getName() );
				

			} else {
			  // do nothing
			}
			
			
			// change look and feel back to nimbus   TODO create static method to do this
			try {
			    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			        if ("Nimbus".equals(info.getName())) {
			            UIManager.setLookAndFeel(info.getClassName());
			            break;
			        }
			    }
			} catch (Exception e1) {
				// if nimbus is not available, use system default
				try {
	                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
	            }
				
			}
			
		}
		
	}

	
	private class EditWeekListener implements ActionListener {
		
		// FIELDS
		int wk;
		
		
		
		// CONSTRUCTOR
		public EditWeekListener ( int wk ) {
			this.wk = wk;
		}
		
		
		
		// ACTION LISTENER
		
		public void actionPerformed( ActionEvent e ) {
			
			menuFrame.setVisible(false);
			menuFrame.dispose();
			
			JFrame nframe = new JFrame();
			nframe.setResizable( false );
			nframe.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
			nframe.addWindowListener( new MainWindowListener() );
			
			
			//Reading Default Worker Data from save file
			DefaultWorkerData dwd = null;
			try {
				dwd = new DefaultWorkerData(DefaultWorkerData.HOUSE_WORKERS);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			TabbedPane tp = new TabbedPane();
			tp.setFont(tp.getFont().deriveFont(Settings.TAB_FONT_SIZE));
			tp.setBackground(Settings.BACKGROUND_COLOR);
			
			// getting dates from Monday to Friday
			Calendar date = Calendar.getInstance();
			while (date.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				date.add(Calendar.DAY_OF_MONTH, 1);
			}
			Calendar[] day = new Calendar[5];
			for(int i=0; i<day.length; i++) {	
				day[i] = (Calendar) date.clone();
				date.add(Calendar.DATE, 1);
			}
			
			
			DayPanel[] dp = new DayPanel[5];
			for(int i=0; i<5; i++){
				dp[i] = new DayPanel(tp, dwd, day[i], nframe, EDIT_MODE, wk);
			}
			tp.day_panel = dp;
			
			tp.addTab("Monday", dp[0]);
			tp.addTab("Tuesday", dp[1]);
			tp.addTab("Wednesday", dp[2]);
			tp.addTab("Thursday", dp[3]);
			tp.addTab("Friday", dp[4]);
			
			tp.changePreviousTab(0);
			tp.addChangeListener( new TabChangeListener(tp, nframe) );
			
			
			nframe.add( tp );
			nframe.pack();

			Rectangle effectiveScreenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			int difference = (int) effectiveScreenSize.getWidth() - nframe.getWidth();
			int new_x = (int) difference/2;
			nframe.setLocation( new Point(new_x , 20) );
			
			frame.setVisible( false );
			frame.dispose();
			
			ChooseWeekPanel.fillWeek(tp, nframe, wk);
			
			for (int i=0; i<5; i++) {
				dp[i].header_panel.week_A.setEnabled(false);
				dp[i].header_panel.week_B.setEnabled(false);
				dp[i].header_panel.neither.setEnabled(false);
			}
			
			nframe.setVisible( true );
		
			
		}
		
	}

	
	private class EditWorkerListener implements ActionListener {

		// FIELDS
		int type;
		JFrame container_frame;
		
		
		// CONSTRUCTOR
		public EditWorkerListener ( int type, JFrame container_frame ) {
			this.type = type;
			this.container_frame = container_frame;
		}
		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			JFrame new_frame = new JFrame();
			new_frame.setResizable(false);
			new_frame.addWindowListener( new FrameCloseListener(container_frame) );
			
			
			
			if ( type == HOUSES_WORKERS ) {
				MenuEditHousesWorkersPanel panel = new MenuEditHousesWorkersPanel( new_frame );
				new_frame.add(panel);
			}
			else {
				MenuEditCovenantWorkersPanel panel = new MenuEditCovenantWorkersPanel( new_frame );
				new_frame.add(panel);
			}
			
			new_frame.pack();
			StaticMethods.findSetLocation(new_frame);
			new_frame.setVisible(true);
			
		}
	
		
		
	}
	
	
	private class CancelButtonListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			
			frame.setVisible( false );
			frame.dispose();
			
		}
		
	}
	
	
	private class SubmitButtonListener implements ActionListener {
		
		public void actionPerformed( ActionEvent e ) {
			
			// Write all settings data to SettingsSaveFile
			BufferedWriter buffWriter;
			try {
				
				FileWriter fileWriter = new FileWriter(SETTINGS_SAVE_FILE);
				buffWriter = new BufferedWriter(fileWriter);
				
				buffWriter.write(excel_template_file.getAbsolutePath());
				buffWriter.newLine();
				buffWriter.write(save_location_file.getAbsolutePath());
				buffWriter.newLine();
				buffWriter.write(String.valueOf( font_size_slider.getValue()));
				buffWriter.newLine();
				buffWriter.write(color_theme_selection);
				
				buffWriter.close();
				
			} catch (IOException e1) {
				System.out.println("i/o error");
				e1.printStackTrace();
			}
			
			// changing the font size (if changed)
			int textSizeFactor = font_size_slider.getValue();
				
			Settings.setFontSize(textSizeFactor);
            Settings.setHeaderFontSize(textSizeFactor);
            Settings.setTabFontSize(textSizeFactor);
								
			menuFrame.setVisible(false);
			menuFrame.dispose();
				
			JFrame newMenuFrame = new JFrame();
			MenuPanel menuPanel = new MenuPanel( newMenuFrame );
				
			newMenuFrame.add(menuPanel);
			newMenuFrame.setResizable(false);
			newMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
			newMenuFrame.pack();

				
			newMenuFrame.setLocationRelativeTo(null);
			newMenuFrame.setVisible(true);

			
			// close Settings Frame
			frame.setVisible(false);
			frame.dispose();
			

		}
		
	}
	
}
