package seng302.group6.Model.Command.Person;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for removing a skill from a person
 * Created by Josh on 1/05/2015.
 */
public class RemoveSkill implements Command {

    private Person person;
    private String skill;

    /**
     * Create a Removeskill command for removing a skill from a person
     * @param person Person to remove the skill from
     * @param skill Skill to be removed
     */
    public RemoveSkill(Person person, String skill) {
        this.person = person;
        this.skill = skill;
    }

    /**
     * Undo the action, adding the skill
     */
    @Override
    public void undo() {
        person.addSkill(skill);
    }

    /**
     * Redo the action, removing the skill
     */
    @Override
    public void redo() {
        person.removeSkill(skill);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PERSON, person, "skillsList");
        message.setTab("Skills");

        return message;
    }
}
