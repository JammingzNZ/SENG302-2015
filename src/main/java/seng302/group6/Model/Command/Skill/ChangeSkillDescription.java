package seng302.group6.Model.Command.Skill;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the desription of a skill
 * Created by Josh on 1/05/2015.
 */
public class ChangeSkillDescription implements Command {

    private Skill skill;
    private String oldDescription;
    private String newDescription;

    /**
     * Create a ChangeDescription command for changing the description of a skill
     * @param skill Skill to change the description of
     * @param newDescription String to change the description to
     */
    public ChangeSkillDescription(Skill skill, String newDescription) {
        this.skill = skill;
        this.newDescription = newDescription;
        this.oldDescription = skill.getDescription();
    }

    /**
     * Undo the action, setting the description to the old description
     */
    @Override
    public void undo() {
        skill.setDescription(oldDescription);
    }

    /**
     * Redo the action, setting the description to the new description
     */
    @Override
    public void redo() {
        skill.setDescription(newDescription);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.SKILL, skill, "skillDescription");

        return message;
    }
}
