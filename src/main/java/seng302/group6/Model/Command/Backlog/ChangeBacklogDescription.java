package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for cahnging a backlog description.
 * Created by Michael Wheeler on 3/07/15.
 */
public class ChangeBacklogDescription implements Command {

    private Backlog backlog;
    private String oldDescription;
    private String newDescription;

    /**
     * Create a ChangeDescription command for changing the description of a backlog
     * @param backlog Backlog to change the description of
     * @param newDescription String to change the description to
     */
    public ChangeBacklogDescription(Backlog backlog, String newDescription) {
        this.backlog = backlog;
        this.newDescription = newDescription;
        this.oldDescription = backlog.getDescription();
    }

    /**
     * Undo the action, setting the description to the old description
     */
    @Override
    public void undo() {
        backlog.setDescription(oldDescription);
    }

    /**
     * Redo the action, setting the description to the new description
     */
    @Override
    public void redo() {
        backlog.setDescription(newDescription);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.BACKLOG, backlog, "backlogDescription");

        return message;
    }

}
