package assignment2; 

import java.io.File;
import assignment2.AbstractReviewHandler;
import assignment2.MovieReview;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;


/**
*
** CS3354 Fall 2019 Review Handler Method specification
   @author Kelby Webster & Daniel Sparrow
*/
public class ReviewHandler extends AbstractReviewHandler {
	
	
	/**
     * Loads reviews from a given path. If the given path is a .txt file, then
     * a single review is loaded. Otherwise, if the path is a folder, all reviews
     * in it are loaded.
     * @param filePath The path to the file (or folder) containing the review(sentimentModel).
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     * 2 = Unknown).
     * @return A list of reviews as objects.
     */
	public void loadReviews(String filePath, int realClass) {
		//for menu option 1
		//calls classifyReviews
		
	
		
		//2. check for single review (.txt) or review folder
		if(filePath.endsWith(".txt")) { 	//path is a single review
			
			//load a single review
			
		}
		
		else {		//path is a review folder
			
			//load the entire folder
		}
		

       
    }
  
	
	/**
     * Reads a single review file and returns it as a MovieReview object. 
     * This method also calls the method classifyReview to predict the polarity
     * of the review.
     * @param reviewFilePath A path to a .txt file containing a review.
     * @param realClass The real class entered by the user.
     * @return a MovieReview object.
     * @throws IOException if specified file cannot be openned.
     */
    public MovieReview readReview(String reviewFilePath, int realClass) throws IOException {
        //for menu options 1 and 3
        //calls classifyReview, reads single file to add to database or search for substring
    }

    
    /**
     * Deletes a review from the database, given its id.
     * @param id The id value of the review.
     */
    public void deleteReview(int id) {
       
    }
    
    
    /**
     * Loads review database.
     */
	public void loadSerialDB() {
		  //initial database load
		//1. check for database.ser file
		
		
		//2. if database.ser exists deserialize, else create database.ser
	}
	
	
    /**
     * Searches the review database by id.
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not 
     * exist in the database.
     */
    public MovieReview searchById(int id) {
        //for menu option 3
        
    }
    
    
    /**
     * Searches the review database for reviews matching a given substring.
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    public List<MovieReview> searchBySubstring(String substring) {
        //for menu option 3
    }

	
    /**
     * Calls classifyReview(MovieReview review) to handle multiple reviews
     * at once.
     */
    public void classifyReviews() {
    	
    }
    
    
    /**
     * Reports the overall classification accuracy if the real class is known.
     * @param realClass The real class entered by the user.
     */
    public void reportAccuracy(int realClass) {
    
    }

}