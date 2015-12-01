package seng302.group6.Model.Command;

/**
 * Interface to implement a Command type of action - either undo or redo.
 * @author Greg COpePerson. But lets pretend Josh Norton
 *
 */
public interface Command {


    /**
     * Undoes an action
     */
    public void undo();


    /**
     * Redoes an action
     */
    public void redo();


    /**
     * Get a message indicating the attributes of a the command
     * @return CommandMessage object containing attributes of the command
     */
    public CommandMessage getMessage();

}
