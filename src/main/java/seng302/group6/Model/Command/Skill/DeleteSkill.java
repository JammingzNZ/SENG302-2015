package seng302.group6.Model.Command.Skill;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Skill;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for deleting a skill
 * Created by Jaln Rodger on 2/05/2015.
 */
public class DeleteSkill implements Command {

    private Skill skill;

    /**
     * constructor for teh commande class of DeleteSkill
     * @param skill The skill to be deleted
     */
    public DeleteSkill(Skill skill) {
        super();
        this.skill = skill;
    }

    /**
     * Undo the action, removing the skill from the skills hashmap.
     */
    public void undo() {
        Workspace.addSkill(skill);
    }

    /**
     * Redo the action, removing the skill from the skills hashmap.
     */
    public void redo() {
        Workspace.removeSkill(skill.uid());
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.SKILL, skill);

        return message;
    }
}
