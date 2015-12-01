package seng302.group6.Model.Command.Skill;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemType;

import java.util.HashMap;

/**
 * Command class which allows undo/redo functionality for creating a skill
 * Created by Josh Norton on 20/03/2015.
 */
public class CreateSkill implements Command {

    private Skill skill;
    private HashMap<String, Skill> skills;

    /**
     * Create a CreateSkill command for adding/removing the skill to/from the given skills hashmap.
     * @param skills A hashmap to add the person to
     * @param skill Skill to be added to the hashmap
     */
    public CreateSkill(HashMap<String, Skill> skills, Skill skill) {
        super();

        this.skill = skill;
        this.skills = skills;
    }

    /**
     * Undo the action, removing the skill from the skills hashmap.
     */
    public void undo() {
        skills.remove(skill.uid());
    }

    /**
     * Redo the action, adding the skill to the skills hashmap.
     */
    public void redo() {
        skills.put(skill.uid(), skill);
    }


    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.SKILL, skill);

        return message;
    }

}
