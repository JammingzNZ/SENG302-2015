package seng302.group6.Model.Report.Elements;

import org.jdom2.Element;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Workspace;

/**
 * Creates a Story Element for the XML reports
 * Created by simon on 8/07/15.
 */
public class StoryElement
{
    public static StoryElement story;
    private Story storyItem;
    private Element parent = new Element("Story");

    /**
     * Constructor for the Story Element
     */
    public StoryElement() {story = this; }

    /**
     * Returns the story element containing attributes:
     * ShortName
     * FullName
     * Description
     * Creator
     * @param uid is the unique ID of the story to generate an element for
     * @return the element with the story details attached
     */
    public Element getStory(String uid) {
        this.storyItem = Workspace.getStory(uid);
        parent.setAttribute("ShortName", storyItem.getShortName());
        parent.setAttribute("LongName", storyItem.getLongName());
        parent.setAttribute("Description", storyItem.getDescription());
        parent.setAttribute("Creator", storyItem.getCreatorName());
        parent.setAttribute("EstimationScale", storyItem.getScaleType().toString());
        if (storyItem.getEstimateRep() != null) {
            parent.setAttribute("Estimate", storyItem.getEstimateRep());
        } else {
            parent.setAttribute("Estimate", "");
        }
        parent.setAttribute("State", storyItem.getReadiness() ? "Ready" : "Not Ready");
        parent.setAttribute("Effort-Spent", storyItem.getEffortSpent().toString() + " minutes");
        return parent;
    }

    /**
     * Returns the story element containing attributes:
     * ShortName
     * Estimate
     * Status
     * @param uid is the unique ID of the story to generate an element for
     * @return the element with the story details attached
     */
    public Element getSprintStory(String uid) {
        this.storyItem = Workspace.getStory(uid);
        parent.setAttribute("ShortName", storyItem.getShortName());
        if (storyItem.getEstimateRep() != null) {
            parent.setAttribute("Estimate", storyItem.getEstimateRep());
        } else {
            parent.setAttribute("Estimate", "");
        }
        parent.setAttribute("Status", storyItem.getState().toString());
        parent.setAttribute("Effort-Spent", storyItem.getEffortSpent().toString() + " minutes");
        return parent;
    }
}
