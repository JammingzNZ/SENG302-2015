package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for cahnging the sprint name
 * Created by jaln on 29/07/15.
 */
public class ChangeSprintName implements Command {

    private Sprint sprint;
    private String oldFullName;
    private String newFullName;

    public ChangeSprintName(Sprint sprint, String newFullName) {
        this.sprint = sprint;
        this.newFullName = newFullName;
        this.oldFullName = sprint.getFullName();
    }

    /**
     * Undo the action, changing the Sprint's Full Name to the OldFullName
     */
    @Override
    public void undo() {
        sprint.setFullName(oldFullName);
    }

    /**
     * Redo the action, changing the Sprint's Full Name to the newFullName
     */
    @Override
    public void redo() {
        sprint.setFullName(newFullName);
    }

    /**
     * Return a message indicating properties of the command
     *
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintFullName");

        return message;
    }
}