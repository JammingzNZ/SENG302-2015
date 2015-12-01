package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;


/**
 * Created by Michael Wheeler on 25/07/15.
 */
public class ChangeTaskName implements Command{

    private Story story;
    private Task task;
    private String oldShortName;
    private String newShortName;

    /**
     * Create a ChangeTaskShortName command for changing the shortName of an Task
     * @param story Story that contains the Task
     * @param task Task to be changed
     * @param shortName String to change the Task shortName to
     */
    public ChangeTaskName(Story story, Task task, String shortName) {
        this.story = story;
        this.task = task;
        this.newShortName = shortName;

        this.oldShortName = task.getShortName();
    }

    /**
     * Undo the action, setting the Task shortName to the old shortName
     */
    @Override
    public void undo() {
        task.setShortName(oldShortName);
    }

    /**
     * Redo the action, setting the Task shortName to the new shortName
     */
    @Override
    public void redo() {
        task.setShortName(newShortName);
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
