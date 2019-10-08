// Kelby Webster and Daniel Sparrow
// Assignment 2, CS 3354

package assignment2;
import java.io.Serializable;



public class MovieReview implements Serializable {
	
	public int id;
	public int realClass;
	public int predictedPolarity;
	public String reviewWords;
	public String filePath;
	public String firstFiftyChars;
	

    public MovieReview(String reviewWords, String filePath, int realClass) {
        this.reviewWords = reviewWords;
        this.filePath = filePath;
        this.realClass = realClass;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
    	this.id = i;
    }
  
    public String getText() {
        return this.reviewWords;
    }

 
    public int getPredictedPolarity() {
        return this.predictedPolarity;
    }


    public void setPredictedPolarity(int predictedPolarity) {
        this.predictedPolarity = predictedPolarity;
    }

    
    public int getRealClass() {
        return this.realClass;
    }

    
    public String getFirstFiftyChars() { 
    	
        int fileSize = this.reviewWords.length();
        String fifty;
        
        if(fileSize > 50) {
            fifty = this.reviewWords.substring(0,50);
            this.firstFiftyChars = fifty;
        }
        else
            this.firstFiftyChars = this.reviewWords;
        return this.firstFiftyChars;
    }

    
    public int reviewSubstring(String substring) {
    	
        int tru = -1;
        int isTrue;
        String review = this.reviewWords;
        tru = review.indexOf(substring);    // returns -1 if not in index, otherwise returns index
        
        if(tru != -1) {        
            isTrue = 1;
        }
        else
            isTrue = 0;
        
        return isTrue;
    }
}
