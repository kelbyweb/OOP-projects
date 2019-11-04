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
    
	 /**
		* The main function
		* @param args to be passed in
		* @throws IOException if invalid input
		*/
    public static void main(String [] args) throws IOException {
    	
    	ReviewHandler rh = new ReviewHandler();
      	
     // first load from serialized file if present
        rh.loadSerialDB();
        
        
     // TODO: Components for GUI layout
        // All of these are static private final 
        // top panel (JPanel), bottom panel (JPanel), command label (JLabel), 
        // combo box (JComboBox), database button (JButton), save button (JButton)
        
     // Output area
        // Set as global so it can be edited in different methods
        // static protected final output area (JTextArea),
        // static private final output scroll pane (JScrollPane)
        
     // Width & height of the monitor
        // Both are private static int
        // width, height  (get default screen size) 
        
     // Width & height of the JFrame window
        // Both are private static int
        // windows width and height (Both JFrame)
        
    	
    	// display and loop with choices
    	boolean continueMenu = true;
        while(continueMenu == true)
		{
			continueMenu = menuOptions(rh);
		}
             
    }
   
    /**
     * Initialize the JFrame and JPanels, and show them.
     * Also sets the location to the middle of the monitor.
     */
    private static void createAndDisplayGUI() {
    	
    	createTopPanel();
        createBottomPanel();
    	
    	
    	
    }
    
    
    /**
     * This method initializes the top panel, which is the choice menu using a ComboBox
     */
    private static void createTopPanel() {
    	
    
    	
    	
    }
    
    
    
    /**
     * This method initialize the bottom panel, which is the output area.
     * It's just a TextArea that is not editable
     */
    private static void createBottomPanel() {
    	
    	
    	
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
	public static boolean menuOptions(ReviewHandler rh) throws IOException
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
			firstChoice(rh);
		if(n == 2)
			secondChoice(rh);
		if(n == 3)
			thirdChoice(rh);
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
	* @throws InputMismatchException if invalid input
	* @param rh The ReviewHandler to be passed in
     * @throws IOException 
	*/
	public static void firstChoice(ReviewHandler rh) throws NumberFormatException
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
	* @param rh ReviewHandler to be passed in
	* @throws InputMismatchException if invalid input
	* @throws IOException if invalid file input
	*/
	public static void secondChoice(ReviewHandler rh) throws IOException
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
	* @param rh ReviewHandler to be passed in
	* @throws InputMismatchException if invalid input
	*/
	public static void thirdChoice(ReviewHandler rh)
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
