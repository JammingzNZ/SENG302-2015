package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for creating a sprint
 * Created by Michael Wheeler on 29/07/15.
 */
public class CreateSprint implements Command {

    private Sprint sprint;

    /**
     * Stores the specified sprint.
     * Allows the sprint to be added / removed via redo / undo respectively.
     *
     * @param sprint a backlog that has been created.
     */
    public CreateSprint(Sprint sprint) {
        super();
        this.sprint  = sprint;
    }

    /**
     * Undo the action, removing the sprint from the workspace.
     */
    public void undo() {
        Workspace.removeSprint(sprint.uid());
    }

    /**
     * Redo the action, adding the sprint from the workspace.
     */
    public void redo() {
        Workspace.addSprint(sprint);
    }


    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.SPRINT, sprint);

        return message;
    }
}
