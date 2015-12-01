package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command for changing estimate scale of a story
 * Created by josh on 7/07/15.
 */
public class ChangeStoryScale implements Command
{
    private Story story;
    private ScaleType oldScale;
    private ScaleType newScale;

    /**
     * Creates a ChangeStoryName command for changing story scale type
     * @param story the story to change
     * @param newScale the new scale
     */
    public ChangeStoryScale(Story story, ScaleType newScale)
    {
        this.story = story;
        this.newScale = newScale;
        this.oldScale = story.getScaleType();
    }

    /**
     * Undo change story scale type, sets scale type to old scale type
     */
    @Override
    public void undo()
    {
        story.setScaleType(oldScale);
    }

    /**
     * Redo change story scale type, sets scale type to new scale type
     */
    @Override
    public void redo()
    {
        story.setScaleType(newScale);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "scaleCombo");

        return message;
    }
}
