package de.thb;

import de.thb.view.DashboardUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;


public class ApplicationMain extends JPanel {

    public ApplicationMain(int anzViews) {
        super(new GridLayout(1, 1));

        //Define the tab component to hold two views
        JTabbedPane tabbedPaneViewChooser = new JTabbedPane();

        tabbedPaneViewChooser.addTab("Dashboard", null, new DashboardUI(anzViews),
                "Dashboard");

        tabbedPaneViewChooser.setMnemonicAt(0, KeyEvent.VK_1);

        //        //Add the tabbed pane to this panel.
        add(tabbedPaneViewChooser);

        //The following line enables to use scrolling tabs.
        tabbedPaneViewChooser.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }



    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("ApplicationMain");
        JFrame frame2 = new JFrame("ApplicationMain2");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ApplicationMain applicationMain = new ApplicationMain(1);
        ApplicationMain applicationMain2 = new ApplicationMain(2);
        applicationMain.setPreferredSize((new Dimension(800,400)));
        applicationMain2.setPreferredSize((new Dimension(800,400)));
        frame.add(applicationMain, BorderLayout.CENTER);
        frame2.add(applicationMain2, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        
        frame2.pack();
        frame2.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.TRUE);
                createAndShowGUI();
            }
        });
    }

}
