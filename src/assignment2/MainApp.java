package assignment2;
import assignment2.ReviewHandler;
import java.io.*;
import java.util.List;
import java.util.Scanner;


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
    	
      	int realClass = 0;
    	
    	// first load from serialized file if present
    	rh.loadSerialDB();
    	
    	Scanner scan = new Scanner(System.in);
    	
    	
    	// display and loop with choices
    	while(true) { 
    		switch(menu(scan)) {
             
             case 0:
            	 // exit after saving db
            	 scan.close();
            	 rh.saveSerialDB();
            	 System.out.println("Exiting program.");
            	 System.exit(0);
            	 break;
            	 
             case 1:
            	 // ask user for path and realClass
            	 System.out.println("Enter the path for the review or folder: ");
            	 String filePath = scan.nextLine(); 
            	 System.out.println("Please enter the real class if known : (0 = negative, 1 = positive, 2 = unknown) ");
            	 realClass = Integer.parseInt(scan.nextLine());
            	 rh.loadReviews(filePath, realClass);
            	 rh.classifyReviews();
            	 rh.reportAccuracy(realClass);
            	 rh.saveSerialDB();
            	 break;
            	 
             case 2:
            	 // delete review based on its id
            	 System.out.println("Please enter the ID of the review you want to delete : ");
            	 int id = Integer.parseInt(scan.nextLine());
                 rh.deleteReview(id);
            	 break;
            	 
            	// search based on id or string and display results
             case 3:
            	 System.out.println("Enter id or substring to search for:");
                 if(scan.hasNextInt()) {
                     id = Integer.parseInt(scan.nextLine());
                     MovieReview mr = rh.searchById(id);
                     System.out.println("reviewId\tText\tPredictedClass\tRealClass");
                     if(mr!=null) {
                    	 System.out.printf("Review ID : " + mr.getId());
                		 //System.out.printf("First 50 characters of review: " + mr.getFirstFiftyChars());
                		 System.out.printf("Predicted class : " + mr.getPredictedPolarity());
                		 System.out.printf("Real class : " + mr.getRealPolarity());
                     }
                     else {
                         System.out.println("No matching reviews");
                     }
                 }
                 else {
                     String sstr = scan.nextLine();
                     System.out.println("reviewId\tText\tPredictedClass\tRealClass");
                     List<MovieReview> mrs = rh.searchBySubstring(sstr);
                     for(MovieReview mr : mrs) {
                         

                         System.out.printf("Review ID : " + mr.getId());
                		 //System.out.printf("First 50 characters of review: " + mr.getFirstFiftyChars());
                		 System.out.printf("Predicted class : " + mr.getPredictedPolarity());
                		 System.out.printf("Real class : " + mr.getRealPolarity());
                     }
                 }
                 break;
             }
             
    	}
    	
    
}
	/**
	 * Displays static menu
	 * @param scan Scanner to read in user input
	 * @return The user's selected numerical input for the menu 
	 */
    public static int menu(Scanner scan)
    {
    	System.out.println("0. Exit program.");
        System.out.println("1. Load new movie review collection (given a folder or a file path).");
        System.out.println("2. Delete movie review from database (given its id).");
        System.out.println("3. Search movie reviews in database by id or by matching a substring.");
        System.out.println("Enter choice:");
        int choice = Integer.parseInt(scan.nextLine());
        return choice;
    }   
}
