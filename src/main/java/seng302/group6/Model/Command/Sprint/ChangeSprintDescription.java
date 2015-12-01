package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the description of a sprint
 * Created by jaln on 29/07/15.
 */
public class ChangeSprintDescription implements Command {

    private Sprint sprint;
    private String oldDescription;
    private String newDescription;

    /**
     * Constructor for teh command class of ChangeSprintDescription
     * @param sprint teh sprint
     * @param newDescription the new description of the sprint
     */
    public ChangeSprintDescription(Sprint sprint, String newDescription) {
        this.sprint = sprint;
        this.newDescription = newDescription;
        this.oldDescription = sprint.getDescription();
    }

    /**
     * Undo the action, changing the Sprint's Description to the OldDescription
     */
    @Override
    public void undo() {
        sprint.setDescription(oldDescription);
    }

    /**
     * Redo the action, changing the Sprint's Description to the newDescription
     */
    @Override
    public void redo() {
        sprint.setDescription(newDescription);
    }

    /**
     * Return a message indicating properties of the command
     *
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintDescription");

        return message;
    }
}