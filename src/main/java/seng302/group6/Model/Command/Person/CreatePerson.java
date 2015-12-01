package seng302.group6.Model.Command.Person;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemType;

import java.util.HashMap;

/**
 * Command class which allows undo/redo functionality for Creating a new person
 * Created by Jaln and Josh on 20/03/15.
 */
public class CreatePerson implements Command {

    private HashMap<String, Person> people;
    private Person person;

    /**
     * Create a CreatePerson command for adding/removing the person to/from the given people hashmap.
     * @param people A hashmap to add the person to
     * @param person Person to be added to the hashmap
     */
    public CreatePerson(HashMap<String, Person> people, Person person) {
        super();

        this.person = person;
        this.people = people;
    }

    /**
     * Undo the action, removing the person from the people hashmap.
     */
    public void undo() {
        people.remove(person.uid());
    }

    /**
     * Redo the action, adding the person to the people hashap.
     */
    public void redo() {
        people.put(person.uid(), person);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.PERSON, person);

        return message;
    }
}
