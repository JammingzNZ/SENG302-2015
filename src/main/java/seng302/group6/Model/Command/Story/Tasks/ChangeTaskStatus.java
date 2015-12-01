package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;
import seng302.group6.Model.Status.StatusType;

import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for changing the status of a task in a story
 * Created by Michael Wheeler on 25/07/15.
 */
public class ChangeTaskStatus implements Command{

    private Story story;
    private Task task;
    private StatusType oldStatus;
    private StatusType newStatus;
    private Sprint sprint;
    private TaskTab tab;

    /**
     * Create a ChangeTaskStatus command for changing the status of an Task
     * @param story Story that contains the Task
     * @param task Task to be changed
     * @param status StatusType to change the Task status to
     * @param sprint Sprint to use
     * @param tab the tab the change was made on
     */
    public ChangeTaskStatus(Story story, Task task, StatusType status, Sprint sprint, TaskTab tab) {
        this.story = story;
        this.task = task;
        this.newStatus = status;

        this.oldStatus = task.getStatus();

        this.sprint = sprint;

        this.tab = tab;
    }

    /**
     * Undo the action, setting the Task status to the old status
     */
    @Override
    public void undo() {
        task.setStatus(oldStatus);
    }

    /**
     * Redo the action, setting the Task status to the new status
     */
    @Override
    public void redo() {
        task.setStatus(newStatus);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     * TODO find the name of the GUI element
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "taskTable");

        if (tab == TaskTab.SCRUM_BOARD) {
            message.setTab("Scrum Board");
        } else {
            message.setTab("All Tasks");
        }

        ArrayList<Object> scl = new ArrayList<>();
        scl.add(story);
        scl.add(task);
        message.setSpecialCaseList(scl);

        return message;
    }
}
