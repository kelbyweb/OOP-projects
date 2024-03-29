package assignment2;
import java.io.Serializable;


/**
*
** CS3354 Fall 2019 Movie Review Class implementation
   @author Kelby Webster & Daniel Sparrow
*/
public class MovieReview implements Serializable {
	private static final long serialVersionUID=1L; //prevents output errors

	
    /**
     * The id of the review (e.g. 2087).
     */
    private final int id;

    /**
     *  The text of the review.
     */
    private final String text;

    /**
     * The predicted polarity  (0 = negative, 1 = positive).
     */
    private int predictedPolarity;

    /**
     * The ground truth polarity (0 = negative, 1 = positive, 2 = unknown).
     */
    private final int realPolarity;
	
	
    /**
     * Constructor.
     * @param id
     * @param text
     * @param realPolarity
     */
    public MovieReview(int id, String text, int realPolarity) {
        this.id = id;
        this.text = text;
        this.realPolarity = realPolarity;
        this.predictedPolarity = 0; // Set a default value. To be changed later.
    }

    
    /**
     *
     * @return id field
     */
    public int getId() {
        return id;
    }

    
    /**
     *
     * @return text field
     */
    public String getText() {
        return text;
    }

    
    /**
     *
     * @return predictedPolarity field
     */
    public int getPredictedPolarity() {
        return predictedPolarity;
    }

    
    /**
     *
     * @param predictedPolarity
     */
    public void setPredictedPolarity(int predictedPolarity) {
        this.predictedPolarity = predictedPolarity;
    }

    
    /**
     *
     * @return realPolarity
     */
    public int getRealPolarity() {
        return realPolarity;
    }
    
}

