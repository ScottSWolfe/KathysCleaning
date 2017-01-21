package com.github.scottswolfe.kathyscleaning.general.view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuFrame extends JFrame {

    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem saveMenuItem;
    JMenuItem loadMenuItem;
    
    public MenuFrame() {
        super();
        createAndAddMenu();
    }
    
    private void createAndAddMenu() {
        
        // Create the menu bar
        menuBar = new JMenuBar();
        
        // Build the file menu
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
     
        // Build the save menu item
        saveMenuItem = new JMenuItem("Save");
        saveMenuItem.setMnemonic(KeyEvent.VK_S);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(saveMenuItem);
        
        // Build the load menu item
        loadMenuItem = new JMenuItem("Load");
        loadMenuItem.setMnemonic(KeyEvent.VK_L);
        loadMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_L, ActionEvent.CTRL_MASK));
        fileMenu.add(loadMenuItem);

        // Add the menu bar to the frame
        this.setJMenuBar(menuBar);
    }

    

    

/*
    ImageIcon icon = createImageIcon("images/middle.gif");
    menuItem = new JMenuItem("Both text and icon", icon);
    menuItem.setMnemonic(KeyEvent.VK_B);
    menu.add(menuItem);

    menuItem = new JMenuItem(icon);
    menuItem.setMnemonic(KeyEvent.VK_D);
    menu.add(menuItem);

    //a group of radio button menu items
    menu.addSeparator();
    ButtonGroup group = new ButtonGroup();

    rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
    rbMenuItem.setSelected(true);
    rbMenuItem.setMnemonic(KeyEvent.VK_R);
    group.add(rbMenuItem);
    menu.add(rbMenuItem);

    rbMenuItem = new JRadioButtonMenuItem("Another one");
    rbMenuItem.setMnemonic(KeyEvent.VK_O);
    group.add(rbMenuItem);
    menu.add(rbMenuItem);

    //a group of check box menu items
    menu.addSeparator();
    cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
    cbMenuItem.setMnemonic(KeyEvent.VK_C);
    menu.add(cbMenuItem);

    cbMenuItem = new JCheckBoxMenuItem("Another one");
    cbMenuItem.setMnemonic(KeyEvent.VK_H);
    menu.add(cbMenuItem);

    //a submenu
    menu.addSeparator();
    submenu = new JMenu("A submenu");
    submenu.setMnemonic(KeyEvent.VK_S);

    menuItem = new JMenuItem("An item in the submenu");
    menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_2, ActionEvent.ALT_MASK));
    submenu.add(menuItem);

    menuItem = new JMenuItem("Another item");
    submenu.add(menuItem);
    menu.add(submenu);

    //Build second menu in the menu bar.
    menu = new JMenu("Another Menu");
    menu.setMnemonic(KeyEvent.VK_N);
    menu.getAccessibleContext().setAccessibleDescription(
            "This menu does nothing");
    menuBar.add(menu);

    return menuBar;
}

public Container createContentPane() {
    //Create the content-pane-to-be.
    JPanel contentPane = new JPanel(new BorderLayout());
    contentPane.setOpaque(true);

    //Create a scrolled text area.
    output = new JTextArea(5, 30);
    output.setEditable(false);
    scrollPane = new JScrollPane(output);

    //Add the text area to the content pane.
    contentPane.add(scrollPane, BorderLayout.CENTER);

    return contentPane;
}
*/
/** Returns an ImageIcon, or null if the path was invalid. */
    /*
protected static ImageIcon createImageIcon(String path) {
    java.net.URL imgURL = MenuLookDemo.class.getResource(path);
    if (imgURL != null) {
        return new ImageIcon(imgURL);
    } else {
        System.err.println("Couldn't find file: " + path);
        return null;
    }
}*/

/**
 * Create the GUI and show it.  For thread safety,
 * this method should be invoked from the
 * event-dispatching thread.
 */
    /*
private static void createAndShowGUI() {
    //Create and set up the window.
    JFrame frame = new JFrame("MenuLookDemo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //Create and set up the content pane.
    MenuLookDemo demo = new MenuLookDemo();
    frame.setJMenuBar(demo.createMenuBar());
    frame.setContentPane(demo.createContentPane());

    //Display the window.
    frame.setSize(450, 260);
    frame.setVisible(true);
    */
}
