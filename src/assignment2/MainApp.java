package assignment2;
import assignment2.AbstractReviewHandler;
import assignment2.ReviewHandler;
import java.util.*;
import java.io.*;
/**
 *
    @author Kelby Webster & Daniel Sparrow
 */

//still need to add check for existing database before load
//assign id for each review, make sure it's non-conflicting 
public class MainApp {
    
	
    public static void main(String [] args) throws IOException {
    	
    	String path;
    	int polarity;
    	displayMenuOptions();
    
    }
    
    
    public static void displayMenuOptions() {
    	
    	Scanner scan = new Scanner(System.in);
    	
    	int userInput;
    	int delete;
    	ReviewHandler rh = new ReviewHandler();
    	rh.loadSerialDB();
    	
    	do { 
    		 System.out.println("0. Exit program.");
             System.out.println("1. Load new movie review collection (given a folder or a file path).");
             System.out.println("2. Delete movie review from database (given its id).");
             System.out.println("3. Search movie reviews in database by id or by matching a substring.");
             userInput = scan.nextInt();
             
             switch(userInput) {
             
             case 0:
            	 rh.saveSerialDB();
            	 break;
            	 
             case 1:
            	 String path;
            	 int polarity;
            	 path = getUserInput();
            	 polarity = getPolarity(path);
            	 System.out.println("Please enter the polarity if known : (0 = negative, 1 = positive, 2 = unknown) ");
            	 polarity = scan.nextInt();
            	 rh.loadReviews(path, polarity);
            	 rh.saveSerialDB();
            	 break;
            	 
             case 2:
            	 System.out.println("Please enter the ID you want to delete : ");
            	 delete = scan.nextInt();
            	 rh.deleteReview(delete);
            	 break;
            	 
             case 3:
            	 System.out.println("Please enter 1 to search by ID or 2 to search by substring : ");
            	 Scanner choice = new Scanner(System.in);
            	 int choiceInt = (int)choice.nextInt();
            	 
            	 if(choiceInt !=1 && choiceInt != 2)
            	 {
            		 System.out.println("Invalid input.");
            		 return;
            	 }
            	 
            	 if(choiceInt == 1)
            	 {
            		 System.out.println("Enter the ID you want to search for :");
            		 Scanner choiceId = new Scanner(System.in);
            		 MovieReview searchedMovie = rh.searchById(choiceId.nextInt());
            		 
            		 if(searchedMovie == null)
            		 {
            			 System.out.println("The ID you entered does not match a movie. ");
            		 }
            		 
            		System.out.println("review ID: " + searchedMovie.getId());
         			System.out.println("Review text: " + searchedMovie.getFirstFiftyChars());
         			System.out.println("Predicted class: " + searchedMovie.getPredictedPolarity());
         			System.out.println("Real class: " + searchedMovie.getRealClass());
         			System.out.println();
            	 }
            	 
            	 if(choiceInt == 2)
            	 {
            		 System.out.println("Enter the substring you want to search for : ");
            		 Scanner userSubstring = new Scanner(System.in);
            		 String substring = userSubstring.nextLine();
                	 List<MovieReview> searchedReviews = rh.searchBySubstring(substring);
                	 
                	 if(searchedReviews.isEmpty()) 
                	 {
                		 System.out.println("No reviews were found that contain that substring. ");
                		 System.out.println();
                		 return;
                	 }
                	 
                	 for(MovieReview i: searchedReviews) {
                		 
                		 System.out.printf("Review ID : " + i.getId());
                		 System.out.printf("First 50 characters of review: " + i.getFirstFiftyChars());
                		 System.out.printf("Predicted class : " + i.getPredictedPolarity());
                		 System.out.printf("Real class : " + i.getRealClass());
                	 }
            	 }
            	 
            	 break;
            	 
             }
             
    	} while(userInput != 0);
    	
    }
    
    
    
    public static String getUserInput() {
    	
    	String path;
    	Scanner scan = new Scanner(System.in);
    	
    	System.out.println("Please enter the path of the file : ");
    	path = scan.nextLine();
    
    	return path;
    }
    
    
    public static int getPolarity(String path) {
    	
    	String lastPath = path.substring(path.length() - 3);
    	
    	if(lastPath.equals("pos") || lastPath.equals("os/"))
    		return 1; // positive case
    	else if(lastPath.equals("neg") || lastPath.equals("eg"))
    		return 0; // negative case
    	else
    		return 2; // unknown case
    }
<<<<<<< HEAD
}}
=======
}
>>>>>>> 0e823ee07b40f5c4460ef266dc2d2536c366a10c
