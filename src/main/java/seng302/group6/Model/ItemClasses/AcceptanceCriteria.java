package seng302.group6.Model.ItemClasses;

import com.google.gson.annotations.Expose;

/**
 * Model class to represent an Acceptance Criteria
 * Created by Josh on 29/06/2015.
 */
public class AcceptanceCriteria {

    @Expose
    private String text;

    /**
     * Create a new piece of acceptance criteria.
     * @param text Text for the AC to have
     */
    public AcceptanceCriteria(String text) {
        this.text = text;
    }

    /**
     * Get the text for the acceptance criteria.
     * @return String of the AC text
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text for the acceptance criteria.
     * @param text Text to set the AC text to
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the text property of the AC
     * @return the text property of the AC
     */
    public String toString()
    {
        return getText();
    }

}
