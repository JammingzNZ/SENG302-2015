package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for locking a sprint
 * Created by Josh 13/08/15.
 */
public class LockSprint implements Command {

    private Sprint sprint;

    /**
     * Constructor for the command class of LockSprint
     * @param sprint sprint
     */
    public LockSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    /**
     * Undo the action, putting the sprint in set up mode
     */
    @Override
    public void undo() {
        sprint.setSettingUpOn();
    }

    /**
     * Redo the action, taking the sprint out of set up mode
     */
    @Override
    public void redo() {
        sprint.setSettingUpOff();
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "lockButton");

        return message;
    }

}
