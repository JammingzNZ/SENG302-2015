package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for moving a task in a story's task list
 * Created by Michael Wheeler on 25/07/15.
 */
public class MoveTask implements Command {


    private Story story;
    private Task task;
    private int oldIndex;
    private int newIndex;

    /**
     * Create a MoveTask command for moving an Task to a different place in a story's Task list
     * @param story Story to move the Task within
     * @param task Task to be moved
     * @param index Place to move the Task to
     */
    public MoveTask(Story story, Task task, int index) {
        this.story = story;
        this.task = task;
        this.newIndex = index;

        this.oldIndex = story.getAllTasks().indexOf(task);
    }

    /**
     * Undo the action, moving the task back to the old index
     */
    @Override
    public void undo() {
        story.moveTask(oldIndex, task);
    }

    /**
     * Redo the action, moving the task to the new index
     */
    @Override
    public void redo() {
        story.moveTask(newIndex, task);
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
