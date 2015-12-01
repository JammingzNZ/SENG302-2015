package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the description of a task in a story
 * Created by Michael Wheeler on 25/07/15.
 */
public class ChangeTaskDescription implements Command{

    private Story story;
    private Task task;
    private String oldDescription;
    private String newDescription;

    /**
     * Create a ChangeTaskDescription command for changing the description of an Task
     * @param story Story that contains the Task
     * @param task Task to be changed
     * @param description String to change the AC description to
     */
    public ChangeTaskDescription(Story story, Task task, String description) {
        this.story = story;
        this.task = task;
        this.newDescription = description;

        this.oldDescription = task.getDescription();
    }

    /**
     * Undo the action, setting the Task description to the old description
     */
    @Override
    public void undo() {
        task.setDescription(oldDescription);
    }

    /**
     * Redo the action, setting the Task description to the new description
     */
    @Override
    public void redo() {
        task.setDescription(newDescription);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     * TODO find the name of the GUI element
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.STORY, story, "storyTaskList");
        message.setTab("Tasks");

        return message;
    }
}
