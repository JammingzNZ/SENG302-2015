package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the end date of the sprint
 * Created by jaln on 29/07/15.
 */
public class ChangeEndDate implements Command {


    private Sprint sprint;
    private String oldEndDate;
    private String newEndDate;

    /**
     * Create a ChangeEndDate command for changing the End date of a sprint
     * @param sprint the sprint being modified
     * @param newEndDate the new end date of the sprint
     */
    public ChangeEndDate(Sprint sprint, String newEndDate) {
        this.sprint = sprint;
        this.oldEndDate = sprint.getEndDate();
        this.newEndDate = newEndDate;
    }

    /**
     * Undo the action, setting the End Date to the new End date
     */
    @Override
    public void undo() {
        sprint.setEndDate(oldEndDate);
    }

    /**
     * Redo the action, setting the End Date to the new End date
     */
    @Override
    public void redo() {
        sprint.setEndDate(newEndDate);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintEndDate");

        return message;
    }
}
