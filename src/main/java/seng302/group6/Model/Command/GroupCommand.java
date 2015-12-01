package seng302.group6.Model.Command;

import java.util.ArrayList;

/**
 * Create a GroupCommand command, allowing multiple commands to be executed at once.
 * Created by Josh on 21/04/2015.
 */
public class GroupCommand implements Command {

    private ArrayList<Command> commands;
    private CommandMessage message;

    /**
     * Create a GroupCommand command, allowing multiple commands to be executed at once.
     */
    public GroupCommand() {
        commands = new ArrayList<>();
    }

    /**
     * Undo all grouped commands, in reverse order.
     */
    @Override
    public void undo() {
        for (int i = commands.size(); i > 0; i--) {
            commands.get(i-1).undo();
        }
    }

    /**
     * Redo all grouped commands.
     */
    @Override
    public void redo() {
        for (int i = 0; i < commands.size(); i++) {
            commands.get(i).redo();
        }
    }

    /**
     * Add a command to the grouped command list.
     * @param command Command to add
     */
    public void addCommand(Command command) {
        commands.add(command);
    }

    /**
     * Returns whether the command list is empty or not
     * @return Boolean indicating whether the command list is empty or not
     */
    public boolean isEmpty() {
        return commands.isEmpty();
    }


    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage()
    {
        return message;
    }

    /**
     * Set the message indicating properties of the command
     * @param message is the message indicating properties of the command
     */
    public void setMessage(CommandMessage message)
    {
        this.message = message;
    }
}
