package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

/**
 * Created by Michael Wheeler on 25/07/15.
 */
public class AddTask implements Command{

    private Story story;
    private Task task;

    /**
     * Create an AddTask command for adding an Task to a story
     * @param story Story to add the Task to
     * @param task Task to be added
     */
    public AddTask(Story story, Task task) {
        this.story = story;
        this.task = task;
    }

    /**
     * Undo the action, removing the Task
     */
    @Override
    public void undo() {
        story.deleteTask(task);
    }

    /**
     * Redo the action, adding the Task
     */
    @Override
    public void redo() {
        story.addTask(task);
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
