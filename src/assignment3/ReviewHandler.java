package assignment3; 

import java.io.FileNotFoundException;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import sentiment.Sentiment;
import java.util.ArrayList;
import java.io.*;


/**
*
** CS3354 Fall 2019 Review Handler Method specification
   @author Kelby Webster & Daniel Sparrow
*/
public class ReviewHandler extends AbstractReviewHandler {
	
	/**
     * Constructor.
     */
	public ReviewHandler()
	 {
		 super();
	 }
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
		//calls classifyReview
		
		 int key;
		 File f = new File(filePath);
		 Scanner inReview;
		 MovieReview temp;

		 try
		 {
		 if(f.isFile()) //adds singular file to database
		 {
			 key = ((int) f.getName().charAt(0)) * f.getName().length() * ( (int) Math.random() * 40 + 1);
			 inReview = new Scanner(f);
			 String text = "";
			 while(inReview.hasNextLine())
				 text += inReview.nextLine();
			 temp = new MovieReview(key, text, realClass);
			 temp.setPredictedPolarity(classifyReview(temp));
			 database.put(key, temp);
		 }
		 else if(f.isDirectory()) //access directory
		 {
			 File[] reviewsDir = f.listFiles();
			 for(File files: reviewsDir)
			 {
				key = ((int) files.getName().charAt(0)) * files.getName().length() * ( (int) Math.random() * 40 + 1);
				inReview = new Scanner(files);
				String text = "";
				while(inReview.hasNextLine())
					text += inReview.nextLine();
				temp = new MovieReview(key, text, realClass);
				temp.setPredictedPolarity(classifyReview(temp));
				database.put(key, temp);
			 }
		 }
		 }
		 catch(Exception e)
		 {
			 System.out.println("Difficulty accessing file/directory.");
			 System.out.println(e.getMessage());
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
    	 File fi = new File(reviewFilePath);
		
		 int key = ((int) reviewFilePath.charAt(0)) * reviewFilePath.length() * ( (int) Math.random() * 40 + 1);
		 Scanner inReview = new Scanner(fi);
		 String text = "";
		 while(inReview.hasNextLine())
			 text += inReview.nextLine();
		 MovieReview review = new MovieReview(key, text, realClass);
		 review.setPredictedPolarity(classifyReview(review));
		 
		 return review;
    }

    
    /**
     * Deletes a review from the database, given its id.
     * @param id The id value of the review.
     */
    public void deleteReview(int id) {
    	
            database.remove(id);
    }
    
    
    /**
     * Loads review database.
     */
	public void loadSerialDB() {
		File data = new File(DATA_FILE_NAME);
		if(data.exists() == false)
		{
		try {
		 FileOutputStream fileOut = new FileOutputStream(DATA_FILE_NAME);
         ObjectOutputStream out = new ObjectOutputStream(fileOut);
         out.writeObject(database);
         out.close();
         fileOut.close();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
		}
		try
		{
			FileInputStream fileIn = new FileInputStream(DATA_FILE_NAME);
			ObjectInputStream objectIn = new ObjectInputStream(fileIn);
			database = (HashMap) objectIn.readObject();
			objectIn.close();
			fileIn.close();
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
		catch(ClassNotFoundException i) {
			System.out.println(i.getMessage());
		}
	}
	
	
    /**
     * Searches the review database by id.
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not 
     * exist in the database.
     */
    public MovieReview searchById(int id) {
        
    	return database.get(id);
    }
    
    
    /**
     * Searches the review database for reviews matching a given substring.
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    public List<MovieReview> searchBySubstring(String substring) {
        //for menu option 3
    	List<MovieReview> reviewList = new ArrayList<MovieReview>();
        for(MovieReview review : database.values()) {
            if (review.getText().indexOf(substring) >= 0)
            	reviewList.add(review);
        }
        return reviewList;
    }

	
    /**
     * Calls classifyReview(MovieReview review) to handle multiple reviews
     * at once.
     */
    public void classifyReviews() {
    	for (MovieReview mr : database.values()) {
            classifyReview(mr);
        }
    }
    
    
    /**
     * Reports the overall classification accuracy if the real class is known.
     * @param realClass The real class entered by the user.
     */
    public void reportAccuracy(int realClass) {
    	
    	if(realClass == 2) return;

        int total = database.values().size();
        int count = 0;
        for (MovieReview mr : database.values()) {
            int review = mr.getPredictedPolarity();
            if (review == realClass)
                count++;
        }
        double accuracy = (count * 100.0 / total);
        System.out.format("Accuracy of classification : %.2f", accuracy);
        System.out.println("%");
    }

}
