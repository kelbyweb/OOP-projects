package assignment2;
import assignment2.AbstractReviewHandler;
import assignment2.ReviewHandler;
import java.util.*;
import java.io.*;
/**
 *
    @author Kelby Webster & Daniel Sparrow
 */
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
    	String substring;
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
            	 System.out.println("Please enter the polarity : (0 = negative, 1 = positive, 2 = unknown) ");
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
            	 substring = scan.nextLine();
            	 System.out.println("Please enter word : ");
            	 substring = scan.nextLine();
            	 List<MovieReview> searchedReviews = rh.searchBySubstring(substring);
            	 
            	 for(MovieReview i: searchedReviews) {
            		 
            		 System.out.printf("%-10d %10s %10d %21d %n",i.getId(),i.getFirstFiftyChars(),i.getPredictedPolarity(),i.getRealClass());
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
