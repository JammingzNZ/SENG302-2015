package seng302.group6.Model.Command.Story.Tasks;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.*;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Command class which allows undo/redo functionality for adding effort to a task in a story
 * Created by Josh Norton on 10/09/15.
 * Michael Wheeler helped.
 */
public class AddTaskEffort implements Command{

    private Sprint sprint;
    private Task task;
    private Effort effort;
    private Story story;
    private EffortLeft effortLeft;
    private Double oldEffortLeft;
    private Double newEffortLeft;
    private TaskTab tab;

    /**
     * Create a AddTaskEffort command for adding effort to a Task
     * @param sprint sprint to use - (Since person allocation is done in Sprint Frame / Scrumboard)
     * @param task Task to be changed
     * @param effort Effort log to add
     * @param story Story
     * @param tab tab to go back to
     * @param effortLeft effort left
     */
    public AddTaskEffort(Sprint sprint, Task task, Effort effort, Story story, Double effortLeft, TaskTab tab) {
        this.sprint = sprint;
        this.task = task;
        this.effort = effort;
        this.story = story;
        this.effortLeft = new EffortLeft(LocalDateTime.parse(effort.getStartDate(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME), effortLeft
        );
        this.oldEffortLeft = task.getEffortLeft();
        this.newEffortLeft = effortLeft;
        
        this.tab = tab;
    }

    /**
     * Undo the action, removing the effort
     */
    @Override
    public void undo() {
        task.removeEffort(effort);

        if (task.getEffortLeftList().contains(effortLeft)) {
            task.removeEffortLeft(effortLeft);
        }
        task.setEffortLeft(oldEffortLeft);
    }

    /**
     * Redo the action, adding the effort
     */
    @Override
    public void redo() {
        task.addEffort(effort);

        // Log the effort left object if the date of the log is before tomorrow
        if (LocalDateTime.parse(effortLeft.getDate()).isBefore(LocalDateTime.now().plusDays(1))) {
            task.addEffortLeft(effortLeft);
        }
        task.setEffortLeft(newEffortLeft);
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
