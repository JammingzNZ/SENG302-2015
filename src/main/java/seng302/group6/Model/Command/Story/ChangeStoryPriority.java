package seng302.group6.Model.Command.Story;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the priority of a story
 * Created by Michael Wheeler on 5/07/15.
 */
public class ChangeStoryPriority implements Command {

    private Story story;
    private Integer oldPriority;
    private Integer newPriority;
    private Backlog backlog;

    /**
     * Creates a ChangeStoryPriority command for changing story current priority
     * @param story the story to change
     * @param backlog the backlog the story is in
     * @param newPriority the new priority
     */
    public ChangeStoryPriority(Story story, Backlog backlog, Integer newPriority)
    {
        this.story = story;
        this.newPriority = newPriority;
        this.oldPriority = story.getCurrentPriority();
        this.backlog = backlog;
    }

    /**
     * Undo change story priority, sets priority to old priority
     */
    @Override
    public void undo()
    {
        story.setCurrentPriority(oldPriority);
    }

    /**
     * Redo change story priority, sets priority to new priority
     */
    @Override
    public void redo()
    {
        story.setCurrentPriority(newPriority);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.BACKLOG, backlog, "backlogStories");
        message.setTab("Stories");

        return message;
    }
}
