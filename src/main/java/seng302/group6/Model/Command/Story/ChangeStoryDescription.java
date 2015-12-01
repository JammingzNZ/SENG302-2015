package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command for changing description of a Story
 * Created by simon on 22/05/15.
 */
public class ChangeStoryDescription implements Command
{
    private Story story;
    private String oldDescription;
    private String newDescription;

    /**
     * Creates a ChangeStoryDescription command for changing story description
     * @param story the story to change
     * @param newDescription the new description
     */
    public ChangeStoryDescription(Story story, String newDescription)
    {
        this.story = story;
        this.newDescription = newDescription;
        this.oldDescription = story.getDescription();
    }

    /**
     * Undo change story description, sets description to old description
     */
    @Override
    public void undo()
    {
        story.setDescription(oldDescription);
    }

    /**
     * Redo change story description, sets description to new description
     */
    @Override
    public void redo()
    {
        story.setDescription(newDescription);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "storyDescription");

        return message;
    }
}
