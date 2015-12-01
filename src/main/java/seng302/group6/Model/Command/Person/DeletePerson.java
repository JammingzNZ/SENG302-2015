package seng302.group6.Model.Command.Person;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for deleting a person
 * Created by Jaln Rodger on 2/05/2015.
 */
public class DeletePerson implements Command {

    private Person person;

    public DeletePerson(Person person) {
        super();

        this.person = person;
    }


    /**
     * Undo the action of delete person
     */
    public void undo() {
        Workspace.addPerson(person);
    }


    /**
     * Redo the action of delete person
     */
    public void redo() {
        Workspace.removePerson(person.uid());
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.PERSON, person);

        return message;
    }
}