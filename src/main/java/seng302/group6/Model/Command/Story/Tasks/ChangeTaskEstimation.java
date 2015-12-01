package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.EffortLeft;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Story;
import seng302.group6.Model.ItemClasses.Task;
import seng302.group6.Model.ItemType;

import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for changing the estimation of a task in a story
 *
 * Created by Michael Wheeler on 25/07/15.
 */
public class ChangeTaskEstimation implements Command {

    private Story story;
    private Task task;
    private Double oldEstimation;
    private Double newEstimation;
    private Sprint sprint;
    private EffortLeft effortLeft;
    private TaskTab tab;

    /**
     * Create a ChangeTaskEstimation command for changing the estimation of an Task
     * @param story Story that contains the Task
     * @param task Task to be changed
     * @param estimation Double to change the Task estimation to
     * @param sprint Sprint to use - XOXO
     * @param tab the tab the change was made on
     * @param effortLeft effort left
     */
    public ChangeTaskEstimation(Story story, Task task, Double estimation, Sprint sprint, EffortLeft effortLeft, TaskTab tab) {
        this.story = story;
        this.task = task;
        this.newEstimation = estimation;

        this.sprint = sprint;

        this.oldEstimation = task.getEstimation();

        this.effortLeft = effortLeft;

        this.tab = tab;
    }

    /**
     * Undo the action, setting the Task estimation to the old estimation
     */
    @Override
    public void undo() {
        task.setEstimation(oldEstimation);
    }

    /**
     * Redo the action, setting the Task estimation to the new estimation
     */
    @Override
    public void redo() {
        task.setEstimation(newEstimation);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     * TODO find the name of the GUI element
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message;
        if (tab == TaskTab.STORY_TASKS) {
            message = new CommandMessage("Change", ItemType.STORY, story, "taskTable");
            message.setTab("Tasks");
        } else if (tab == TaskTab.SCRUM_BOARD) {
            message = new CommandMessage("Change", ItemType.SPRINT, sprint, "taskTable");
            message.setTab("Scrum Board");
        } else {
            message = new CommandMessage("Change", ItemType.SPRINT, sprint, "taskTable");
            message.setTab("All Tasks");
        }
        ArrayList<Object> scl = new ArrayList<>();
        scl.add(story);
        scl.add(task);
        message.setSpecialCaseList(scl);

        return message;
    }


}
