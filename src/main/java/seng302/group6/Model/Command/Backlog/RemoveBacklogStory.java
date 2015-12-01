package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for removing a backlog story.
 * Created by Michael Wheeler on 5/07/15.
 */
public class RemoveBacklogStory implements Command {

    private Backlog backlog;
    private String story;
    private Integer priority;
    private Boolean ready;

    /**
     * Create an RemoveBacklogStory command for removing a story from a backlog's stories list
     * @param backlog Backlog to remove the story from
     * @param story Story to be removed
     * @param priority priority to be removed
     */
    public RemoveBacklogStory(Backlog backlog, String story, Integer priority){
        this.backlog = backlog;
        this.story = story;
        this.priority = priority;

        this.ready = Workspace.getStory(story).getReadiness();
    }


    /**
     * Undo the action, removing the story
     */
    @Override
    public void undo() {
        backlog.addStory(story, priority);

        if (ready) {
            Workspace.getStory(story).setReadinessToReady();
        }
    }

    /**
     * Redo the action, adding the story
     */
    @Override
    public void redo() {
        backlog.removeStory(story);
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
