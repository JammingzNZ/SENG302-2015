package seng302.group6.Model.Command.Team;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing teh team description of a team
 * Created by Josh on 1/05/2015.
 */
public class ChangeTeamDescription implements Command {

    private Team team;
    private String oldDescription;
    private String newDescription;

    /**
     * Create a ChangeDescription command for changing the description of a team
     * @param team Team to change the description of
     * @param newDescription String to change the description to
     */
    public ChangeTeamDescription(Team team, String newDescription) {
        this.team = team;
        this.newDescription = newDescription;
        this.oldDescription = team.getDescription();
    }

    /**
     * Undo the action, setting the description to the old description
     */
    @Override
    public void undo() {
        team.setDescription(oldDescription);
    }

    /**
     * Redo the action, setting the description to the new description
     */
    @Override
    public void redo() {
        team.setDescription(newDescription);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.TEAM, team, "teamDescription");

        return message;
    }
}
