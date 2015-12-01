package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command for changing estimate for a story
 * Created by josh on 7/07/15.
 */
public class ChangeStoryEstimate implements Command
{
    private Story story;
    private Integer oldEstimate;
    private Integer newEstimate;
    private Boolean ready;

    /**
     * Creates a ChangeStoryName command for changing story estimate
     * @param story the story to change
     * @param newEstimate the new estimate
     */
    public ChangeStoryEstimate(Story story, Integer newEstimate)
    {
        this.story = story;
        this.newEstimate = newEstimate;
        this.oldEstimate = story.getEstimate();
        this.ready = story.getReadiness();
    }

    /**
     * Undo change story estimate, sets estimate to old estimate
     */
    @Override
    public void undo()
    {
        story.setEstimate(oldEstimate);

        if (ready) {
            story.setReadinessToReady();
        }
    }

    /**
     * Redo change story estimate, sets estimate to new estimate
     */
    @Override
    public void redo()
    {
        story.setEstimate(newEstimate);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "estimateCombo");

        return message;
    }
}
