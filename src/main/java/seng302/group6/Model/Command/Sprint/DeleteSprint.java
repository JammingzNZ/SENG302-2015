package seng302.group6.Model.Command.Sprint;

import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.Command.GroupCommand;
import seng302.group6.Model.ItemClasses.Sprint;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for deleting a sprint
 * Created by Michael Wheeler on 29/07/15.
 */
public class DeleteSprint extends GroupCommand {

    private Sprint sprint;

    /**
     * Creates a DeleteSprint command for removing a sprint from the workspace
     * and adding it back
     * @param sprint sprint to be deleted
     */
    public DeleteSprint(Sprint sprint)
    {
        super();
        this.sprint = sprint;
        for (String story : sprint.getStories()) {
            addCommand(new RemoveStoryFromSprint(sprint, story));
        }
    }

    /**
     * Undo the delete sprint action, add the sprint back to the workspace
     */
    @Override
    public void undo()
    {
        Workspace.addSprint(sprint);
        super.undo();

    }

    /**
     * Redo the delete sprint action, removing the sprint from the workspace
     */
    @Override
    public void redo()
    {
        super.redo();
        Workspace.removeSprint(sprint.uid());
    }

    /**
     * Return a message indicating the properties of the command
     * @return Return a message indicating the properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.SPRINT, sprint);

        return message;
    }
}
