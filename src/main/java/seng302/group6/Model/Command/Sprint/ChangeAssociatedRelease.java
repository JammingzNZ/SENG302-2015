package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for cahnging the associated release of a sprint
 * Created by jaln on 29/07/15.
 */
public class ChangeAssociatedRelease extends GroupCommand {

    private Sprint sprint;
    private String oldRelease;
    private String newRelease;

    /**
     * Create a ChangeAssociatedRelease command for changing the Associated Release of a Sprint
     * @param sprint sprint being modified
     * @param newRelease release that is being associated to the sprint
     */
    public ChangeAssociatedRelease(Sprint sprint, String newRelease) {
        this.sprint = sprint;
        this.newRelease = newRelease;
        this.oldRelease = sprint.getAssociatedRelease();

        addCommand(new ChangeStartDate(sprint, null));
        addCommand(new ChangeEndDate(sprint, null));
    }

    /**
     * Undo the action, setting the Associated Release to the new Release
     */
    @Override
    public void undo() {
        sprint.setAssociatedRelease(oldRelease);
        super.undo();
    }

    /**
     * Redo the action, setting the Associated Release to the new Release
     */
    @Override
    public void redo() {
        super.redo();
        sprint.setAssociatedRelease(newRelease);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintAssociatedRelease");

        return message;
    }
}