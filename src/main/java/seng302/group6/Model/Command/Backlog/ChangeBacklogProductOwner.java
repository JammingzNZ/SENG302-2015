package seng302.group6.Model.Command.Backlog;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing a backlog PO
 * Created by Michael Wheeler on 3/07/15.
 */
public class ChangeBacklogProductOwner implements Command {

    private Backlog backlog;
    private String oldPo;
    private String newPo;

    /**
     * Create a ChangeProductOwner command for changing the product owner of a backlog
     * @param backlog Backlog to change the product owner of
     * @param newPo String to change the product owner to
     */
    public ChangeBacklogProductOwner(Backlog backlog, String newPo) {
        this.backlog = backlog;
        this.newPo = newPo;
        this.oldPo = backlog.getProductOwner();
    }

    /**
     * Undo the action, setting the name to the old product owner
     */
    @Override
    public void undo() {
        backlog.setProductOwner(oldPo);
    }

    /**
     * Redo the action, setting the name to the new product owner
     */
    @Override
    public void redo() {
        backlog.setProductOwner(newPo);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.BACKLOG, backlog, "backlogPoCombobox");

        return message;
    }

}
