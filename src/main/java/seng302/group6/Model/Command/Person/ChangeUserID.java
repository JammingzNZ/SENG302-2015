package seng302.group6.Model.Command.Person;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing a persons UserID
 * Created by Josh on 1/05/2015.
 */
public class ChangeUserID implements Command {

    private Person person;
    private String oldUserID;
    private String newUserID;

    /**
     * Create a ChangeDescription command for changing the user ID of a skill
     * @param person Person to change the user ID of
     * @param newUserID String to change the user ID to
     */
    public ChangeUserID(Person person, String newUserID) {
        this.person = person;
        this.newUserID = newUserID;
        this.oldUserID = person.getUserID();
    }

    /**
     * Undo the action, setting the user ID to the old user ID
     */
    @Override
    public void undo() {
        person.setUserID(oldUserID);
    }

    /**
     * Redo the action, setting the user ID to the new user ID
     */
    @Override
    public void redo() {
        person.setUserID(newUserID);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PERSON, person, "personID");

        return message;
    }
}
