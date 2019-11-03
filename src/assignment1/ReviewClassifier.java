// Kelby Webster, CS3354, Assignment 01

package movieReviewClassification;

import java.io.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * Class that supports Tweet Classification Mechanism
 * Dictionary of positive words, negative words and all tweets are kept in this class.
 */
public class ReviewClassifier{
    /**
     * Positive word dictionary. Import from file.
    */
    protected HashSet<String> positiveWords =  new HashSet<String>();
    /**
     * Negative word dictionary. Import from file.
     */
    protected HashSet<String> negativeWords = new HashSet<String>();

    /**
     * Number of positive review files
     */
    private float posFilesCount = 0;
    /**
     * Number of negative review files
     */
    private float negFilesCount = 0;
    /**
     * Number of correct classified positive files
     */
    private float correctPosCount = 0;
    /**
     * Number of correct classified negative files
     */
    private float correctNegCount = 0;


    /**
     * Read in files and start classification
     *
     * @param pathToPosWords Path to positive word file
     * @param pathToNegWords Path to negative word file
     * @param pathToPosReviewsFolder Path to folder holding positive reviews
     * @param pathToNegReviewsFolder Path to folder holding negative reviews
     *
     * @throws IOException
     *
     */
    public void readInFiles(String pathToPosWords, String pathToNegWords, String pathToPosReviewsFolder, String pathToNegReviewsFolder) throws IOException {
        //Read in positive words
        readInWords(pathToPosWords, positiveWords);
        System.out.println(positiveWords.size() + " positive words loaded.\n");

        //Read in negative words
        readInWords(pathToNegWords, negativeWords);
        System.out.println(negativeWords.size() + " negative words loaded.\n");

        //Classify positive reviews
        classifyReviews(pathToPosReviewsFolder, true);

        //Classify negative reviews
        classifyReviews(pathToNegReviewsFolder, false);
    }

    /**
     * Read positive/negative words in to HashSet
     *
     * @param fileName path to file containing words
     * @param dictionary word dictionary
     *
     * @throws IOException
     *
     */
    private void readInWords(String fileName, HashSet<String> dictionary) throws IOException {
    	
    	FileReader fr = new FileReader(fileName);
    	Scanner sc = new Scanner(fr);
    	
    	while(sc.hasNextLine()) {
    	String line = sc.nextLine();
    	if (line.startsWith(";") || line.equals("")) {
    		continue;
    	} else {

    	dictionary.add(line);
    	  }
    	}
    	fr.close();
    	sc.close();
    }

    /**
     * Read positive/negative reviews, and call for classification
     *
     * @param folderPath path to review folder containing words
     * @param target target polarity. True = positive, False = negative
     *
     * @throws IOException
     *
     */
    private void classifyReviews(String folderPath, boolean target) throws IOException {
    	
    	File folder = new File(folderPath);
    	String[] fileNames = folder.list();
    	String fileSeparatorChar = System.getProperty("file.separator");
    	String groundTruth = null;
        String analysis = null;
    	
    	for (String file : fileNames) { 
    	if(file.endsWith(".txt")) {
    	String fullPath = folderPath + fileSeparatorChar +file;

    	if (target) {
    		posFilesCount++;
    		groundTruth = "Positive";
    	}
    	
    	else if (!target)
    	{
    		negFilesCount++;
    		groundTruth = "Negative";
    	}
    	
    	boolean result = classifyReview(fullPath);
    	
    	if(result){
    		analysis = "Positive";
    		if(analysis == groundTruth) {
    		correctPosCount++;
    		}
    	}
    	
    	else if (!result){
    		analysis = "Negative";
    		if (analysis == groundTruth) {
    			correctNegCount++;
    	   }
    	 }
    	
    	System.out.println("File path: " + fullPath);
        System.out.println("File name: " + file );
        System.out.println("Ground Truth: " + groundTruth);
        System.out.println("Classification Result: " + analysis + "\n");
    	}
      }
    }

    /**
     * Read positive/negative reviews, and call for classification
     *
     * @param fileName path to review file
     *
     * @return True = positive review, False = negative review
     *
     * @throws IOException
     *
     */
    public boolean classifyReview(String fileName) throws IOException {

        int positive = 0;
        int negative = 0;
    
        FileReader fr = new FileReader(fileName);
        Scanner sc = new Scanner(fr);

        while(sc.hasNextLine()) {
        	String text = sc.next();
        	text = text.replaceAll("<br />", " ");
        	text = text.replaceAll("\\p{Punct}", " ");
        	text = text.toLowerCase();
        	String[] words = text.split("\\s+");
        	
        	for(String word : words) {
        		if(positiveWords.contains(word)) {
        			positive++;
        		} else if(negativeWords.contains(word)) {
        			negative++;
        		}
        	}
        }
        
        sc.close();
        fr.close();
        return (positive > negative);

    }

    /**
     * Output the result
     *
     */
    public void outputResult() {
    	
    	DecimalFormat df = new DecimalFormat("#.#%");
    	DecimalFormat nondf = new DecimalFormat("#");
    	double posAccuracy = (correctPosCount/posFilesCount);
    	double negAccuracy = (correctNegCount/negFilesCount);
    	double overallAccuracy = ((correctPosCount + correctNegCount)/(posFilesCount + negFilesCount));
        
    	System.out.println( "********************************************");
    	System.out.println( "Size of positive dictionary: " + positiveWords.size() + " words");
    	System.out.println( "Size of negative dictionary: " + negativeWords.size() + " words \n");
    	System.out.println( "Number of positive reviews: " + (nondf.format(posFilesCount)));
    	System.out.println( "Correctly classified positive reviews: " + (nondf.format(correctPosCount)));
    	System.out.println( "Misclassified positive reviews: " + (nondf.format(posFilesCount - correctPosCount)));
    	System.out.println( "Correct positive classification rate: " + ((df.format(posAccuracy))+ "\n"));
    	System.out.println( "Number of negative reviews: " + (nondf.format(negFilesCount)));
    	System.out.println( "Correctly classified  negative reviews: " + (nondf.format(correctNegCount)));
    	System.out.println( "Misclassified negative reviews: " + (nondf.format(negFilesCount - correctNegCount)));
    	System.out.println( "Correct negative classification rate: " + (df.format(negAccuracy))+ "\n");
    	System.out.println( "Number of all reviews: " + (nondf.format(posFilesCount + negFilesCount)));
    	System.out.println( "All correctly classified: " + (nondf.format(correctPosCount + correctNegCount)));
    	System.out.println( "All misclassified: " + (nondf.format((posFilesCount - correctPosCount) + (negFilesCount - correctNegCount))));
    	System.out.println( "Overall correct classification rate: " +(df.format(overallAccuracy)));
    	System.out.println( "********************************************");
    	
    }
}