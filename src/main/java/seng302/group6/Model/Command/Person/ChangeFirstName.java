package seng302.group6.Model.Command.Person;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing a persons first name
 * Created by Josh on 1/05/2015.
 */
public class ChangeFirstName implements Command {

    private Person person;
    private String oldFirstName;
    private String newFirstName;

    /**
     * Create a ChangeDescription command for changing the first name of a skill
     * @param person Person to change the first name of
     * @param newFirstName String to change the first name to
     */
    public ChangeFirstName(Person person, String newFirstName) {
        this.person = person;
        this.newFirstName = newFirstName;
        this.oldFirstName = person.getFirstName();
    }

    /**
     * Undo the action, setting the first name to the old first name
     */
    @Override
    public void undo() {
        person.setFirstName(oldFirstName);
    }

    /**
     * Redo the action, setting the first name to the new first name
     */
    @Override
    public void redo() {
        person.setFirstName(newFirstName);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PERSON, person, "personFirstName");

        return message;
    }
}
