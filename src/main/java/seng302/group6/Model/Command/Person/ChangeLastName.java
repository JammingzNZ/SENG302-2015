package seng302.group6.Model.Command.Person;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing a persons last name
 * Created by Josh on 1/05/2015.
 */
public class ChangeLastName implements Command {

    private Person person;
    private String oldLastName;
    private String newLastName;

    /**
     * Create a ChangeDescription command for changing the last name of a skill
     * @param person Person to change the last name of
     * @param newLastName String to change the last name to
     */
    public ChangeLastName(Person person, String newLastName) {
        this.person = person;
        this.newLastName = newLastName;
        this.oldLastName = person.getLastName();
    }

    /**
     * Undo the action, setting the last name to the old last name
     */
    @Override
    public void undo() {
        person.setLastName(oldLastName);
    }

    /**
     * Redo the action, setting the last name to the new last name
     */
    @Override
    public void redo() {
        person.setLastName(newLastName);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PERSON, person, "personLastName");

        return message;
    }
}
