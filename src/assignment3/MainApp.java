package assignment3;
import assignment3.ReviewHandler;
import java.io.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;

/**
*
** CS3354 Fall 2019 Review Handler Main Application
   @author Kelby Webster & Daniel Sparrow
*/

public class MainApp {
    
 	// make review handler object global 
	private static final ReviewHandler rh = new ReviewHandler();
	
	 /**
		* The main function
		* @param args to be passed in
		* @throws IOException if invalid input
		*/
    public static void main(String [] args) throws IOException {
    	
      	
     // first load from serialized file if present
        rh.loadSerialDB();

     // call method to create/display GUI
     // DON'T UNCOMMENT THIS until you're finished with creating panels
        // createAndDisplayGUI();
    	
    // display and loop with choices
    	boolean continueMenu = true;
        while(continueMenu == true)
		{
			continueMenu = menuOptions();
		}
             
    }
    
    // Components for GUI layout
    // All of these are global and static private final 
       static private final JPanel topPanel = new JPanel();
       static private final JPanel bottomPanel = new JPanel();
       static private final JLabel commandLabel = new JLabel("Please select your choice :", JLabel.RIGHT);
       static private final JComboBox comboBox = new JComboBox();
       static private final JButton dbButton = new JButton("Display Database");
       static private final JButton saveDbButton = new JButton("Save Database");
       
    // Output area
       static protected final JTextArea outputArea = new JTextArea();
       static private final JScrollPane outputScrollPane = new JScrollPane(outputArea);
       
    // Width & height of the monitor (get default screen size) 
       private static int width = Toolkit.getDefaultToolkit().getScreenSize().width;
       private static int height = Toolkit.getDefaultToolkit().getScreenSize().height;
       
    // Width & height of the JFrame windows in pixels
       private static int windowWidth = 800;
       private static int windowHeight = 600;
    
    
   
    /**
     * Initialize the JFrame and JPanels, and show them.
     * Also sets the location to the middle of the monitor.
     */
    private static void createAndDisplayGUI() {
    	
    	createTopPanel();
        createBottomPanel();
    	
        // container for top & bottom panel
        JPanel panelContainer = new JPanel();
        panelContainer.setLayout(new GridLayout(2,0));
        panelContainer.add(topPanel);
        panelContainer.add(bottomPanel);
        
        // main window
        JFrame frame = new JFrame("MainApp");
        panelContainer.setOpaque(true);
        frame.setContentPane(panelContainer);
        frame.setVisible(true);
    	
    }
    
    
    /**
     * This method initializes the top panel, which is the choice menu using a ComboBox
     */
    private static void createTopPanel() {
    	
    // each menu option shown as a comboBox.addItem("option text")
    // add item listener for when an option is selected 
    // comboBox.AddItemListener(new ItemListener() and then method ItemStateChanged(event e)
    // add all JLabels to topPanel then call updateUI
    // if e.getItem().equals("option text") then call appropriate method (firstChoice, secondChoice, etc)
    // add action listener (with actionPerformed runnable/thread with run method) for database button and save button 
    // create grid layout and add command label and combo box to it
    // add all JLabels and buttons to topPanel then call updateUI
    	
    }
    
    
    
    /**
     * This method initialize the bottom panel, which is the output area.
     * It's just a TextArea that is not editable
     */
    private static void createBottomPanel() {
    	
    // create new Font (Arial or Times 18 pt) and set to output area
    // display welcome message with outputArea.setText("Welcome...");
    // set text in output area as not editable (false)
    // create black line border then set with outputArea.setBorder
    // create vertical & horizontal scroll bar for outputScrollPane
    // set layout of bottomPanel to a new GridLayout(1, 0)
    // add outputScrollPane to bottomPanel
    	
    }
    
    
    
    /**
     * Print out the formatted JTable for list
     @param target_List
     */
    public static void printJTable(List<MovieReview> target_List) {
    	
    	// Create columns names
    	
    	// Create some data from getters/setters: predicted polarity vs target 
    	
    	// Create a new table instance
    	
    	// Add the table to a scrolling pane
    	
    }
    
    
    
    
	 /**
     * Handles the menu
     * @return Whether or not to close the menu
	 * @param rh ReviewHandler to be passed in
	 * @throws InputMismatchException if invalid input
	 * @throws IOException if invalid file input
     */
	public static boolean menuOptions() throws IOException
	{
		try
		{
		System.out.println("Choose an option: ");
		System.out.println("0. Exit program.");
		System.out.println("1. Load new movie review collection (given a folder or a file path).");
		System.out.println("2. Delete movie review from database (given its id).");
		System.out.println("3. Search movie reviews in database by id or by matching a substring.");
		Scanner reader = new Scanner(System.in); 
		int n;
		n = reader.nextInt(); 
		if(n > 3 || n < 0)
		{
			System.out.println("Invalid input.");
			return true;
		}
		
		if(n == 0)
		{
			rh.saveSerialDB();
			System.out.println("Closing...");
			System.exit(0);
        	return false;
		}
		if(n == 1)
			firstChoice();
		if(n == 2)
			secondChoice();
		if(n == 3)
			thirdChoice();
		}
		catch(InputMismatchException x)
		{
			System.out.println("Invalid input.");
			System.out.println();
		}
		return true;
		
	}
	
	
    /**
     * Menu choice 0: save and quit.
     */
	 public static void exit() {
		 
		 
	 }
    
    
    /**
	* Takes user input to add a review or directory of reviews to the database.
	* @throws NumberFormatException if invalid number
	*/
	public static void firstChoice() throws NumberFormatException
	{
		try
		{
	 // ask user for path and realClass
	 Scanner pathIn = new Scanner(System.in);
   	 System.out.println("Enter the path for the review or folder: ");
     String path = pathIn.nextLine(); 
   	 System.out.println("Please enter the real class if known : (0 = negative, 1 = positive, 2 = unknown) ");
   	 int classReal = Integer.parseInt(pathIn.nextLine());
   	 if(classReal > 2 || classReal < 0)
		{
			System.out.println("Invalid input.");
			return;
		}
   	 rh.loadReviews(path, classReal);
   	 rh.classifyReviews();
   	 if(classReal != 2) // if class is known, output accuracy
   	 {
     rh.reportAccuracy(classReal);
   	 }
     rh.saveSerialDB();
		}
		catch(InputMismatchException x)
		{
			System.out.println("Invalid input.");
			System.out.println();
		}
		
	}
    
    
	/**
	* Takes user input to delete a review from the database
	* @throws IOException if invalid file input
	*/
	public static void secondChoice() throws IOException
	{
		try
		{
	 // delete review based on its id
   	 System.out.println("Please enter the ID of the review you want to delete : ");
   	 Scanner idIn = new Scanner(System.in);
		 rh.deleteReview(idIn.nextInt());
		}
		catch(InputMismatchException p)
		{
			System.out.println("Invalid input.");
			System.out.println();
		}
	}
    
    
	/**
	* Takes user input to search the database by ID or substring
	* @throws InputMismatchException if invalid input
	*/
	public static void thirdChoice()
	{
		 System.out.println("Enter 1 to search by ID or 2 to search by substring :");
    	 Scanner choice = new Scanner(System.in);
    	 
    	 try
			{
    	 
    	 int choiceInt = (int) choice.nextInt();
    	 if(choiceInt != 1 && choiceInt != 2)
 		{
 			System.out.println("Invalid input.");
 			System.out.println();
 			return;
 		}
    	
 		if(choiceInt == 1) // search by ID case
 		{
 			System.out.println("Enter the id you want to search for :");
			Scanner idChoice = new Scanner(System.in);
			
             MovieReview searchedMovie = rh.searchById(idChoice.nextInt());
             if(searchedMovie == null) {
            	 System.out.println("That ID does not match with a movie.");
            	 System.out.println();
 				return;
             }
             else if (searchedMovie != null) {
            	 String firstFifty = searchedMovie.getText().length() > 50 ? searchedMovie.getText().substring(0,50) : searchedMovie.getText();
            	 
            	 System.out.printf("Review ID : " + searchedMovie.getId() + "\n");
        		 System.out.printf("First 50 characters of review: " + firstFifty + "\n");
        		 System.out.printf("Predicted class : " + searchedMovie.getPredictedPolarity() + "\n");
        		 System.out.printf("Real class : " + searchedMovie.getRealPolarity());
        		 System.out.println();
             }
 		}
        
         if(choiceInt == 2) // search by substring case
         {
        	 
        	System.out.println("Enter the substring you want to search for :");
        	Scanner choiceIn = new Scanner(System.in);
			String choiceString = choiceIn.nextLine();
			List<MovieReview> reviewList = rh.searchBySubstring(choiceString);
			if(reviewList.isEmpty())
			{
				System.out.println("No reviews were found containing that string.");
				System.out.println();
				return;
			}
             
             for(MovieReview movie : reviewList) {  
            	 
            	 String firstFifty = movie.getText().length() > 50 ? movie.getText().substring(0,50) : movie.getText();
            	 
                 System.out.printf("Review ID : " + movie.getId() + "\n");
        		 System.out.printf("First 50 characters of review: " + firstFifty + "\n");
        		 System.out.printf("Predicted class : " + movie.getPredictedPolarity() + "\n");
        		 System.out.printf("Real class : " + movie.getRealPolarity());
        		 System.out.println();
             } 
         }
	}
 		catch(InputMismatchException i)
		{
			System.out.println("Invalid input.");
			System.out.println();
		}
	}
}
