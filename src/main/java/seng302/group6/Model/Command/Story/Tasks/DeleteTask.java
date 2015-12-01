package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for deleting a task in a story
 * Created by Michael Wheeler on 25/07/15.
 */
public class DeleteTask implements Command{

    private Story story;
    private Task task;
    private int oldIndex;

    /**
     * Create an DeleteTask command for deleting a Task from a story
     * @param story Story to remove the Task from
     * @param task Task to be removed
     */
    public DeleteTask(Story story, Task task) {
        this.story = story;
        this.task = task;
        this.oldIndex = story.getAllTasks().indexOf(task);
    }

    /**
     * Undo the action, adding the Task
     */
    @Override
    public void undo() {
        story.addTask(task);
        story.moveTask(oldIndex, task);
    }

    /**
     * Redo the action, deleting the Task
     */
    @Override
    public void redo() {
        story.deleteTask(task);
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
