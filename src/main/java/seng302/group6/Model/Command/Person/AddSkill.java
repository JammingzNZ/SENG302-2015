package seng302.group6.Model.Command.Person;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for adding a skill to a person
 * Created by Josh on 1/05/2015.
 */
public class AddSkill implements Command {

    private Person person;
    private String skill;

    /**
     * Create an AddSkill command for adding a skill to a person
     * @param person Person to add the skill to
     * @param skill Skill to be added
     */
    public AddSkill(Person person, String skill) {
        this.person = person;
        this.skill = skill;
    }

    /**
     * Undo the action, removing the skill
     */
    @Override
    public void undo() {
        person.removeSkill(skill);
    }

    /**
     * Redo the action, adding the skill
     */
    @Override
    public void redo() {
        person.addSkill(skill);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PERSON, person, "availableList");
        message.setTab("Skills");

        return message;
    }
}
