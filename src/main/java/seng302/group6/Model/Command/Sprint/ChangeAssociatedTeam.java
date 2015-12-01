package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the associated team of a sprint
 * Created by jaln on 29/07/15.
 */
public class ChangeAssociatedTeam implements Command {

    private Sprint sprint;
    private String oldTeam;
    private String newTeam;

    /**
     * Create a ChangeAssociatedTeam command for changing the Associated Team of a Sprint
     * @param sprint the sprint being modified
     * @param newTeam the uid of the team being associated to the sprint
     */
    public ChangeAssociatedTeam(Sprint sprint, String newTeam) {
        this.sprint = sprint;
        this.newTeam = newTeam;
        this.oldTeam = sprint.getAssociatedTeam();
    }

    /**
     * Undo the action, setting the Associated Team to the new Team
     */
    @Override
    public void undo() {
        sprint.setAssociatedTeam(oldTeam);
    }

    /**
     * Redo the action, setting the Associated Team to the new Team
     */
    @Override
    public void redo() {
        sprint.setAssociatedTeam(newTeam);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SPRINT, sprint, "sprintAssociatedTeam");

        return message;
    }
}