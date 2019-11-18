package assignment3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
*
** CS3354 Fall 2019 Review Handler Method specification
   @author Kelby Webster 
*/
public class ReviewHandler extends AbstractReviewHandler {

    private static int ID;

    /**
     * Loads reviews from a given path. If the given path is a .txt file, then
     * a single review is loaded. Otherwise, if the path is a folder, all reviews
     * in it are loaded.
     * @param filePath The path to the file (or folder) containing the review(sentimentModel).
     * @param realClass The real class of the review (0 = Negative, 1 = Positive
     * 2 = Unknown).
     *
     */
    @Override
    public void loadReviews(String filePath, int realClass) {
        File fileOrFolder = new File(filePath);
        try {
            if (fileOrFolder.isFile()) {
                // File
                if (filePath.endsWith(".txt")) {
                    // Import review
                    MovieReview review = readReview(filePath, realClass);
                    // Add to database
                    database.put(review.getId(), review);
                    //Output result: single file
                    SentimentAnalysisApp.outputArea.append("Review imported.\n");
                    SentimentAnalysisApp.outputArea.append("ID: " + review.getId());
                    SentimentAnalysisApp.outputArea.append("\nText: " + review.getText());
                    SentimentAnalysisApp.outputArea.append("\n\nReal Class: " + review.getRealPolarity());
                    SentimentAnalysisApp.outputArea.append("\nClassification result: " + review.getPredictedPolarity());
                    if (realClass == 2) {
                    	SentimentAnalysisApp.outputArea.append("\nReal class unknown.");
                    } else if (realClass == review.getPredictedPolarity()) {
                        SentimentAnalysisApp.outputArea.append("\nCorrectly classified.");
                    } else {
                        SentimentAnalysisApp.outputArea.append("\nMisclassified.");
                    }
                    SentimentAnalysisApp.outputArea.append("\n");

                } else {
                    // Cannot import non-txt files
                    SentimentAnalysisApp.outputArea.append("\nInput file path is neither a txt file nor folder.");
                    return;
                }
            } else {
                // Folder
                String[] files = fileOrFolder.list();
                String fileSeparatorChar = System.getProperty("file.separator");
                int counter = 0;
                for (String fileName : files) {
                    if (fileName.endsWith(".txt")) {
                        // Only import txt files
                        // Import review
                        MovieReview review = readReview(filePath + fileSeparatorChar + fileName, realClass);
                        // Add to database
                        database.put(review.getId(), review);
                        // Count correct classified reviews, only real class is known
                        if (realClass != 2 && review.getRealPolarity() == review.getPredictedPolarity()) {
                            counter++;
                        }
                    } else {
                        continue;
                    }
                }
                // Output result: folder
                SentimentAnalysisApp.outputArea.append("Folder imported.");
                SentimentAnalysisApp.outputArea.append("\nNumber of entries: " + files.length);

                // Only output accuracy if real class is known
                if (realClass != 2) {
                    SentimentAnalysisApp.outputArea.append("\nCorrectly classified: " + counter);
                    SentimentAnalysisApp.outputArea.append("\nMisclassified: " + (files.length - counter));
                    SentimentAnalysisApp.outputArea.append("\nAccuracy: " + ((double)counter / (double)files.length * 100) + "%\n");
                }
            }
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace();
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
    @Override
    public MovieReview readReview(String reviewFilePath, int realClass) throws IOException {
        // Read file for text
        Scanner inFile = new Scanner(new FileReader(reviewFilePath));
        String text = "";
        while (inFile.hasNextLine()) {
            text += inFile.nextLine();
        }
        // Remove the <br /> occurences in the text and replace them with a space
        text = text.replaceAll("<br />"," ");

        // Create review object, assigning ID and real class
        MovieReview review = new MovieReview(ID, text, realClass);
        //inFile.close();
        // Update ID
        ID++;
        // Classify review
        classifyReview(review);

        return review;
    }

    /**
     * Deletes a review from the database, given its id.
     * @param id The id value of the review.
     */
    @Override
    public void deleteReview(int id) {

        if (!database.containsKey(id)) {
            // Review with given ID does not exist
            SentimentAnalysisApp.outputArea.append("\nID " + id + " does not exist.");
        } else {
            database.remove(id);
            SentimentAnalysisApp.outputArea.append("\nReview with ID " + id + " deleted.");
        }
        return;
    }

    /**
     * Loads review database.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadSerialDB() {
        SentimentAnalysisApp.outputArea.append("\nReading database...");
        // serialize the database
        InputStream file = null;
        InputStream buffer = null;
        ObjectInput input = null;
        try {
            file = new FileInputStream(DATA_FILE_NAME);
            buffer = new BufferedInputStream(file);
            input = new ObjectInputStream(buffer);

            database = (Map<Integer, MovieReview>)input.readObject();
            SentimentAnalysisApp.outputArea.append(database.size() + " entry(s) loaded.\n");

            // get the maximum ID
            for (Map.Entry<Integer, MovieReview> entry : database.entrySet()){
                if (entry.getKey() > ID) {
                    ID = entry.getKey();
                }
            }
            ID = ID + 1;

            input.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println(e.toString());
            e.printStackTrace();
        } finally {
            close(file);
        }
        SentimentAnalysisApp.outputArea.append("\nDone.\n");
    }

    /**
     * Searches the review database by id.
     * @param id The id to search for.
     * @return The review that matches the given id or null if the id does not
     * exist in the database.
     */
    @Override
    public MovieReview searchById(int id) {
        if (database.containsKey(id)) {
            return database.get(id);
        }
        return null;
    }

    /**
     * Searches the review database for reviews matching a given substring.
     * @param substring The substring to search for.
     * @return A list of review objects matching the search criterion.
     */
    @Override
    public List<MovieReview> searchBySubstring(String substring) {
        List<MovieReview> tempList = new ArrayList<MovieReview>();

        for (Map.Entry<Integer, MovieReview> entry : database.entrySet()){
            if (entry.getValue().getText().contains(substring)) {
                tempList.add(entry.getValue());
            }
        }

        if (tempList.size()!=0) {
            return tempList;
        } else {
            // No review has given substring
            return null;
        }

    }
}
