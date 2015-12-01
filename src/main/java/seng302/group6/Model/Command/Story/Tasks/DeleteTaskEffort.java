package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Effort;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for deleting effor on a task in a story
 * Created by Josh Norton on 21/09/15.
 */
public class DeleteTaskEffort implements Command{

    private Sprint sprint;
    private Task task;
    private Effort effort;
    private Story story;
    private TaskTab tab;

    /**
     * Create a DeleteTaskEffort command for removing effort from a Task
     * @param sprint sprint to use - (Since person allocation is done in Sprint Frame / Scrumboard)
     * @param task Task to be changed
     * @param effort Effort log to delete
     * @param story Story
     * @param tab tab
     */
    public DeleteTaskEffort(Sprint sprint, Task task, Effort effort, Story story, TaskTab tab) {
        this.sprint = sprint;
        this.task = task;
        this.effort = effort;
        this.story = story;

        this.tab = tab;
    }

    /**
     * Undo the action, adding the effort
     */
    @Override
    public void undo() {
        task.addEffort(effort);
    }

    /**
     * Redo the action, deleting the effort
     */
    @Override
    public void redo() {
        task.removeEffort(effort);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
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
