package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for creating a backlog
 */
public class CreateBacklog implements Command {

    private Backlog backlog;

    /**
     * Stores the specified backlog.
     * Allows the backlog to be added / removed via redo / undo respectively.
     *
     * @param backlog a backlog that has been created.
     */
    public CreateBacklog(Backlog backlog) {
        super();
        this.backlog = backlog;
        assert this.backlog != null;
    }

    /**
     * Undo the action, removing the backlog from the workspace.
     */
    public void undo() {
        Workspace.removeBacklog(backlog.uid());
    }

    /**
     * Redo the action, adding the backlog from the workspace.
     */
    public void redo() {
        Workspace.addBacklog(backlog);
    }


    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.BACKLOG, backlog);

        return message;
    }
}
