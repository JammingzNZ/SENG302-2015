package seng302.group6.Model.Command;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;

/**
 * Created by Josh on 11/07/2015.
 */
public class AddingNewCommand implements Command {

    private Command command;
    private Boolean addingNew;

    /**
     * Wrap a command in an AddingNewCommand, allowing it to be indicated whether this is a command for a new item
     * @param command Command to wrap
     * @param addingNew Whether it is a new item or not
     */
    public AddingNewCommand(Command command, Boolean addingNew) {
        this.command = command;
        this.addingNew = addingNew;
    }

    /**
     * Perform the wrapped command's undo action
     */
    @Override
    public void undo() {
        command.undo();
    }

    /**
     * Perform the wrapped command's redo action
     */
    @Override
    public void redo() {
        command.redo();
    }

    /**
     * Get the wrapped command's message with a specified addingNew property
     * @return the wrapped command's message with a specified addingNew property
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage cmdMsg = command.getMessage();
        cmdMsg.setAddingNew(addingNew);
        return cmdMsg;
    }
}
