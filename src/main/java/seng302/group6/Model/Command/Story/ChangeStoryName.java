package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command for changing long name of a story
 * Created by simon on 22/05/15.
 */
public class ChangeStoryName implements Command
{
    private Story story;
    private String oldLongName;
    private String newLongName;

    /**
     * Creates a ChangeStoryName command for changing story long name
     * @param story the story to change
     * @param newLongName the new long name
     */
    public ChangeStoryName(Story story, String newLongName)
    {
        this.story = story;
        this.newLongName = newLongName;
        this.oldLongName = story.getLongName();
    }

    /**
     * Undo change story name, sets long name to old long name
     */
    @Override
    public void undo()
    {
        story.setLongName(oldLongName);
    }

    /**
     * Redo change story name, sets long name to new long name
     */
    @Override
    public void redo()
    {
        story.setLongName(newLongName);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "storyLongName");

        return message;
    }
}
