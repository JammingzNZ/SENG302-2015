package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the startdate of a sprint
 * Created by jaln on 29/07/15.
 */
public class ChangeStartDate implements Command {


    private Sprint sprint;
    private String oldStartDate;
    private String newStartDate;

    /**
     * Create a ChangeStartDate command for changing the start date of a sprint
     * @param sprint the sprint being modified
     * @param newstartDate the new start date of the sprint
     */
    public ChangeStartDate(Sprint sprint, String newstartDate) {
        this.sprint = sprint;
        this.oldStartDate = sprint.getStartDate();
        this.newStartDate = newstartDate;
    }

    /**
     * Undo the action, setting the start Date to the new start date
     */
    @Override
    public void undo() {
        sprint.setStartDate(oldStartDate);
    }

    /**
     * Redo the action, setting the start Date to the new start date
     */
    @Override
    public void redo() {
        sprint.setStartDate(newStartDate);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintStartDate");

        return message;
    }
}
