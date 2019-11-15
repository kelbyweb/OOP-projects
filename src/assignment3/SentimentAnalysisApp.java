package src.assignment3;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.*;

/**
*
** CS3354 Fall 2019 Review Handler Main Application
   @author Kelby Webster 
*/
public class SentimentAnalysisApp extends Thread {
	
	// make review handler object global 
    private static final ReviewHandler rh = new ReviewHandler();

    //Log
    static protected final Logger log = Logger.getLogger("SentimentAnalysis");

    /**
     * Main method demonstrates how to use Stanford NLP library classifier.
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        FileHandler fh;
        try {
            // This block configure the logger with handler and formatter
            fh = new FileHandler("SentimentAnalysis.%u.%g.log");
            log.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.setLevel(Level.INFO);

        // First load database
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                // Load database if it is present
                File databaseFile = new File(ReviewHandler.DATA_FILE_NAME);
                if (databaseFile.exists()) {
                    rh.loadSerialDB();
                }

            }
        });
    }
    //Components for the layout
    static private final JPanel topPanel = new JPanel();
    static private final JPanel bottomPanel = new JPanel();;
    static private final JLabel commandLabel = new JLabel("Please select the command",JLabel.RIGHT);
    static private final JComboBox<String> comboBox = new JComboBox<String>();
    static private final JButton databaseButton = new JButton("Show Database");
    static private final JButton saveButton = new JButton("Save Database");
    //Output area. Set as global to be edit in different methods.
    static protected final JTextArea outputArea = new JTextArea();
    static private final JScrollPane outputScrollPane = new JScrollPane(outputArea);
    //width and height of the monitor
    private static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
    //width and height of the window (JFrame)
    private static int windowsWidth = 800;
    private static int windowsHeight = 600;

    /**
     * Initialize the JFrame and JPanels, and show them.
     * Also set the location to the middle of the monitor.
     */
    private static void createAndShowGUI() {

        createTopPanel();
        createBottomPanel();

        // container for top & bottom panel
        topPanel.getIgnoreRepaint();
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new GridLayout(2,0));
        panelContainer.add(topPanel);
        panelContainer.add(bottomPanel);

        // main window
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("Main Application GUI Window");

        // Save when quitting
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                log.info("Closing window.");
                outputArea.append("Closing window. Database will be saved.\n");
                super.windowClosing(e);
                log.info("Saving database.");
                rh.saveSerialDB();
                log.info("System shutdown.");
                System.exit(0);
            }

        });
        panelContainer.setOpaque(true);
        frame.setBounds((width - windowsWidth) / 2,
                (height - windowsHeight) / 2, windowsWidth, windowsHeight);
        frame.setContentPane(panelContainer);

        frame.setVisible(true);


    }
    /**
     * This method initialize the top panel, which is the commands using a ComboBox
     */
    private static void createTopPanel() {
    	// each menu option shown as a comboBox.addItem("option text")
        comboBox.addItem("Please select...");
        comboBox.addItem(" 1. Load new movie review collection (given a folder or a file path).");
        comboBox.addItem(" 2. Delete movie review from database (given its id).");
        comboBox.addItem(" 3. Search movie reviews in database by id.");
        comboBox.addItem(" 4. Search movie reviews in database by substring.");
        comboBox.addItem(" 0. Exit program.");
        comboBox.setSelectedIndex(0);

        
        comboBox.addItemListener(new ItemListener() {
        	@Override
            public void itemStateChanged(ItemEvent e) {
                log.info("Command chosen, Item = " + e.getItem());
                log.info("StateChange = " + e.getStateChange());
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().equals("Please select...")) {
                        outputArea.setText("");
                        outputArea.append(rh.database.size() + " records in database.\n");
                        outputArea.append("Please select a command to continue.\n");
                        topPanel.removeAll();
                        topPanel.add(commandLabel);
                        topPanel.add(comboBox);
                        //Keep the comboBox at the first line.
                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());

                        topPanel.add(new JLabel());
                        topPanel.add(new JLabel());
                        topPanel.add(databaseButton);
                        topPanel.add(saveButton);
                        topPanel.updateUI();
                    } else if (e.getItem().equals(" 1. Load new movie review collection (given a folder or a file path).")) {
                        loadReviews();
                    } else if (e.getItem().equals(" 2. Delete movie review from database (given its id).")) {
                        deleteReviews();
                    } else if (e.getItem().equals(" 3. Search movie reviews in database by id.")) {
                        searchReviewsId();
                    } else if (e.getItem().equals(" 4. Search movie reviews in database by substring.")) {
                        searchReviewsSubstring();
                    } else if (e.getItem().equals(" 0. Exit program.")) {
                        exit();
                    }
                }

            }
        });

        // add action listener (with actionPerformed runnable/thread with run method) 
        // for database button and save button 
        databaseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.info("database button clicked.");
                Runnable myRunnable = new Runnable() {

                    public void run() {
                        printJTable(rh.searchBySubstring(""));
                    }
                };

                Thread thread = new Thread(myRunnable);
                thread.start();
            }

        });

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.info("Save button clicked.");
                Runnable myRunnable = new Runnable() {

                    public void run() {
                        rh.saveSerialDB();
                        outputArea.append("Database has been saved.\n");

                    }
                };

                Thread thread = new Thread(myRunnable);
                thread.start();
            }

        });
        
        // create grid layout and add command label and combo box to it
        GridLayout topPanelGridLayout = new GridLayout(0,2,10,10);

        topPanel.setLayout(topPanelGridLayout);
        topPanel.add(commandLabel);
        topPanel.add(comboBox);
        //Keep the comboBox at the first line
        // add all JLabels and buttons to topPanel then call updateUI
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());

        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(databaseButton);
        topPanel.add(saveButton);
        topPanel.updateUI();
    }

    /**
     * This method initialize the bottom panel, which is the output area.
     * Just a TextArea that not editable.
     */
    private static void createBottomPanel() {
    	// create new Font (Arial 18 pt) and set to output area
        final Font fontArial = new Font("Arial", Font.BOLD, 20);
        DefaultCaret caret = (DefaultCaret)outputArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        outputArea.setFont(fontArial);
        
        // display welcome message
        outputArea.setText("Welcome to the Sentiment Analysis Review handling system!\n");
       
        // set text in output area as not editable (false)
        outputArea.setEditable(false);

        // create black line border then set with outputArea.setBorder
        final Border border = BorderFactory.createLineBorder(Color.MAGENTA);
        
        // create vertical & horizontal scroll bar for outputScrollPane
        outputArea.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        outputScrollPane.createVerticalScrollBar();
        outputScrollPane.createHorizontalScrollBar();
        
        // set layout of bottomPanel to a new GridLayout(1, 0)
        bottomPanel.setLayout(new GridLayout(1,0));
        bottomPanel.add(outputScrollPane);
    }

    /**
     * Method 1: load new reviews text file.
     *
     */
    static int realClass = 0;
    public static void loadReviews() {

        Thread threadLoad = new Thread()
        {
            public void run(){

                outputArea.setText("");
                outputArea.append(rh.database.size() + " records currently in database.\n");
                outputArea.append("Command 1:\n");
                outputArea.append("Please input the path of file or folder:\n");

                topPanel.removeAll();
                topPanel.add(commandLabel);
                topPanel.add(comboBox);

                final JLabel pathLabel = new JLabel("File path:",JLabel.RIGHT);
                final JTextField pathInput = new JTextField("");

                final JLabel realClassLabel = new JLabel("Real class:",JLabel.RIGHT);
                final JComboBox<String> realClassComboBox = new JComboBox<String>();
                realClassComboBox.addItem("Positive");
                realClassComboBox.addItem("Negative");
                realClassComboBox.addItem("Unknown");
                //realClassComboBox.setSelectedIndex(0);

                realClassComboBox.addItemListener(new ItemListener() {
                	@Override
                    public void itemStateChanged(ItemEvent e) {
                        log.info("Real class chosen, real class = " + e.getItem());
                        log.info("StateChange = " + e.getStateChange());
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            if (e.getItem().equals("Negative")) {
                                realClass = 0;
                            } else if (e.getItem().equals("Positive")) {
                                realClass = 1;
                            } else if (e.getItem().equals("Unknown")) {
                                realClass = 2;
                            }
                        }

                    }
                });

                final JButton confirmButton = new JButton("Confirm");

                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        log.info("Confirm button clicked. (Command 1)");
                        Runnable myRunnable = new Runnable() {

                            public void run() {
                                String path = pathInput.getText();
                                rh.loadReviews(path, realClass);

                            }
                        };

                        Thread thread = new Thread(myRunnable);
                        thread.start();
                    }

                });
                topPanel.add(pathLabel);
                topPanel.add(pathInput);
                topPanel.add(realClassLabel);
                topPanel.add(realClassComboBox);
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());

                topPanel.add(new JLabel());
                topPanel.add(confirmButton);
                topPanel.add(databaseButton);
                topPanel.add(saveButton);
                topPanel.updateUI();

                outputArea.append(rh.database.size() + " records currently in database.\n");

            }
        };
        threadLoad.start();
    }

    /**
     * Method 2: delete reviews from database.
     *
     */
    public static void deleteReviews() {

        Thread threadDelete = new Thread()	{
            public void run(){

                outputArea.setText("");
                outputArea.append(rh.database.size() + " records currently in database.\n");
                outputArea.append("Command 2:\n");
                outputArea.append("Please input the review ID:\n");

                topPanel.removeAll();
                topPanel.add(commandLabel);
                topPanel.add(comboBox);

                final JLabel reviewIdLabel = new JLabel("Review ID:",JLabel.RIGHT);
                final JTextField reviewIdInput = new JTextField("");

                final JButton confirmButton = new JButton("Confirm");

                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        log.info("Confirm button clicked. (Command 2)");
                        Runnable myRunnable = new Runnable() {

                            public void run() {
                                String idStr = reviewIdInput.getText();
                                if (!idStr.matches("-?(0|[1-9]\\d*)")) {
                                    // Input is not an integer
                                    outputArea.append("Illegal input.\n");
                                } else {
                                    int id = Integer.parseInt(idStr);
                                    rh.deleteReview(id);
                                }
                            }
                        };

                        Thread thread = new Thread(myRunnable);
                        thread.start();
                    }

                });
                topPanel.add(reviewIdLabel);
                topPanel.add(reviewIdInput);
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());

                topPanel.add(new JLabel());
                topPanel.add(confirmButton);
                topPanel.add(databaseButton);
                topPanel.add(saveButton);
                topPanel.updateUI();

                outputArea.append(rh.database.size() + " records currently in database.\n");

            }};
        threadDelete.start();
    }

    /**
     * Method 3: search reviews from database by Id.
     *
     */
    public static void searchReviewsId() {

        Thread threadSearchID = new Thread()	{
            public void run(){

                outputArea.setText("");
                outputArea.append(rh.database.size() + " records currently in database.\n");
                outputArea.append("Command 3:\n");
                outputArea.append("Please input the review ID:\n");

                topPanel.removeAll();
                topPanel.add(commandLabel);
                topPanel.add(comboBox);

                final JLabel reviewIdLabel = new JLabel("Review ID:",JLabel.RIGHT);
                final JTextField reviewIdInput = new JTextField("");

                final JButton confirmButton = new JButton("Confirm");

                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        log.info("Confirm button clicked. (Command 3)");
                        Runnable myRunnable = new Runnable() {

                            public void run() {
                                String idStr = reviewIdInput.getText();
                                if (!idStr.matches("-?(0|[1-9]\\d*)")) {
                                    // Input is not an integer
                                    outputArea.append("Illegal input.\n");
                                } else {
                                    int id = Integer.parseInt(idStr);
                                    MovieReview mr = rh.searchById(id);
                                    if (mr != null) {
                                        List<MovieReview> reviewList = new ArrayList<MovieReview>();
                                        reviewList.add(mr);
                                        printJTable(reviewList);
                                    } else {
                                        outputArea.append("Review was not found.\n");
                                    }
                                }
                            }
                        };

                        Thread thread = new Thread(myRunnable);
                        thread.start();
                    }

                });
                topPanel.add(reviewIdLabel);
                topPanel.add(reviewIdInput);
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());

                topPanel.add(new JLabel());
                topPanel.add(confirmButton);
                topPanel.add(databaseButton);
                topPanel.add(saveButton);
                topPanel.updateUI();

                outputArea.append(rh.database.size() + " records currently in database.\n");

            }};
        threadSearchID.start();
    }

    /**
     * Method 4: search reviews from database by Id.
     *
     */
    public static void searchReviewsSubstring() {

        Thread threadSearchStr = new Thread()	{
            public void run(){

                outputArea.setText("");
                outputArea.append(rh.database.size() + " records currently in database.\n");
                outputArea.append("Command 4:\n");
                outputArea.append("Please input the review substring:\n");

                topPanel.removeAll();
                topPanel.add(commandLabel);
                topPanel.add(comboBox);

                final JLabel subStringLabel = new JLabel("Review ID:",JLabel.RIGHT);
                final JTextField subStringInput = new JTextField("");

                final JButton confirmButton = new JButton("Confirm");

                confirmButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        log.info("Confirm button clicked. (Command 4)");
                        Runnable myRunnable = new Runnable() {

                            public void run() {

                                String substring = subStringInput.getText();
                                List<MovieReview> reviewList = rh.searchBySubstring(substring);
                                if (reviewList != null) {
                                    printJTable(reviewList);
                                    outputArea.append(reviewList.size() + " reviews found.\n");

                                } else {
                                    outputArea.append("Review was not found.\n");
                                }

                            }
                        };

                        Thread thread = new Thread(myRunnable);
                        thread.start();
                    }

                });
                topPanel.add(subStringLabel);
                topPanel.add(subStringInput);
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());
                topPanel.add(new JLabel());

                topPanel.add(new JLabel());
                topPanel.add(confirmButton);
                topPanel.add(databaseButton);
                topPanel.add(saveButton);
                topPanel.updateUI();

                outputArea.append(rh.database.size() + " records currently in database.\n");

            }};
        threadSearchStr.start();
    }

    /**
     * Method 0: save and quit.
     */
    public static void exit() {

        outputArea.setText("");
        outputArea.append(rh.database.size() + " records currently in database.\n");
        outputArea.append("Command 0:\n");
        outputArea.append("Please click Confirm to save and exit the system.\n");

        topPanel.removeAll();
        topPanel.add(commandLabel);
        topPanel.add(comboBox);

        final JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.info("Confirm button clicked. (Command 0)");
                Runnable myRunnable = new Runnable() {

                    public void run() {
                        log.info("Saving database");
                        rh.saveSerialDB();

                        outputArea.append("Database saved. Window will be closed in 5 seconds.\n");
                        outputArea.append("Thank you for using the system!\n");

                        log.info("Exit the database. (Command 0)");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        log.info("System shutdown.");
                        System.exit(0);
                    }
                };

                Thread thread = new Thread(myRunnable);
                thread.start();
            }

        });

        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());
        topPanel.add(new JLabel());

        topPanel.add(new JLabel());
        topPanel.add(confirmButton);
        topPanel.add(databaseButton);
        topPanel.add(saveButton);
        topPanel.updateUI();
        topPanel.updateUI();
    }


    /**
     * Print out the formatted JTable for list
     @param target_List
     */
    public static void printJTable(List<MovieReview> target_List) {
        // Create columns names
        String columnNames[] = {"ID", "Predicted", "Real", "Text"};
        // Create some data
        String dataValues[][]= new String[target_List.size()][4];
        for(int i = 0; i < target_List.size(); i++) {
            String predicted = "";
            if (target_List.get(i).getPredictedPolarity() == 0) {
                predicted = "Negative";
            } else if (target_List.get(i).getPredictedPolarity() == 1) {
                predicted = "Positive";
            } else if (target_List.get(i).getPredictedPolarity() == 2) {
                predicted = "Unknown";
            }
            String real = "";
            if (target_List.get(i).getRealPolarity() == 0) {
                real = "Negative";
            } else if (target_List.get(i).getRealPolarity() == 1) {
                real = "Positive";
            } else if (target_List.get(i).getRealPolarity() == 2) {
                real = "Unknown";
            }
            dataValues[i][0] = String.valueOf(target_List.get(i).getId());
            dataValues[i][1] = predicted;
            dataValues[i][2] = real;
            dataValues[i][3] = target_List.get(i).getText();

        }
        // Create a new table instance
        JTable table = new JTable(dataValues, columnNames) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        // Add the table to a scrolling pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();
        scrollPane.createVerticalScrollBar();
        scrollPane.createHorizontalScrollBar();
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame resultFrame = new JFrame("Search Results:");
        resultFrame.setBounds((width - windowsWidth) / 4,
                (height - windowsHeight) / 4, windowsWidth, windowsHeight/2);
        resultFrame.setContentPane(scrollPane);
        resultFrame.setVisible(true);
    }
}
