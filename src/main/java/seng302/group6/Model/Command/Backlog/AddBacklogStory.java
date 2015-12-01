package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.EstimationScale.ScaleType;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for adding a story to a backlog.
 * Created by wheeler on 5/07/15.
 */
public class AddBacklogStory implements Command {

    private Backlog backlog;
    private String story;
    private Integer priority;
    private ScaleType oldScale;

    /**
     * Create an AddBacklogStory command for adding a story to a backlog's stories list
     * @param backlog Backlog to add the story to
     * @param story Story to be added
     * @param priority priority to be added
     */
    public AddBacklogStory(Backlog backlog, String story, Integer priority){
        this.backlog = backlog;
        this.story = story;
        this.priority = priority;

        oldScale = Workspace.getStory(story).getScaleType();
    }

    /**
     * Undo the action, removing the story
     */
    @Override
    public void undo() {
        backlog.removeStory(story);
        Workspace.getStory(story).setScaleType(oldScale);
    }

    /**
     * Redo the action, adding the story
     */
    @Override
    public void redo() {
        backlog.addStory(story, priority);
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
