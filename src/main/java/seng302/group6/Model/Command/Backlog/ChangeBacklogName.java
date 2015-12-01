package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing a backlog name
 * Created by Michael Wheeler on 3/07/15.
 */
public class ChangeBacklogName implements Command {

    private Backlog backlog;
    private String oldName;
    private String newName;

    /**
     * Create a ChangeName command for changing the full name of a backlog
     * @param backlog Backlog to change the name of
     * @param newName String to change the name to
     */
    public ChangeBacklogName(Backlog backlog, String newName) {
        this.backlog = backlog;
        this.newName = newName;
        this.oldName = backlog.getFullName();
    }

    /**
     * Undo the action, setting the name to the old name
     */
    @Override
    public void undo() {
        backlog.setFullName(oldName);
    }

    /**
     * Redo the action, setting the name to the new name
     */
    @Override
    public void redo() {
        backlog.setFullName(newName);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.BACKLOG, backlog, "backlogName");

        return message;
    }





}
