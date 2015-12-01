package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.Command.Story.Tasks.ClearTask;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for removing a story from a sprint
 * Created by jaln on 29/07/15.
 */
public class RemoveStoryFromSprint extends GroupCommand {

    private Sprint sprint;
    private String storyUID;

    /**
     * Constructor for the command class of RemoveStoryFromSprint
     * @param sprint The sprint
     * @param storyUID The uid of the story to be removed from the story
     */
    public RemoveStoryFromSprint(Sprint sprint, String storyUID) {
        this.sprint = sprint;
        this.storyUID = storyUID;

        for (Task task : Workspace.getStory(storyUID).getAllTasks()) {
            addCommand(new ClearTask(sprint, task));
        }
    }

    /**
     * Undo the action, adding the Story to the Sprint
     */
    @Override
    public void undo() {
        sprint.addStory(storyUID);
        super.undo();
    }

    /**
     * Redo the action, removing the Story to the Sprint
     */
    @Override
    public void redo() {
        super.redo();
        sprint.removeStory(storyUID);
    }

    /**
     * Return a message indicating properties of the command
     *
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintStories");
        message.setTab("Stories");

        return message;
    }
}