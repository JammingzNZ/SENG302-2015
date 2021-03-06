package seng302.group6.Model.Command.Team;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for adding a developer to a team
 * Created by Josh on 1/05/2015.
 */
public class AddDeveloper implements Command {

    private Team team;
    private String person;

    /**
     * Create an AddDeveloper command for adding a person to a team's developer list
     * @param team Team to add the person to
     * @param person Person to be added
     */
    public AddDeveloper(Team team, String person) {
        this.team = team;
        this.person = person;
    }

    /**
     * Undo the action, removing the person
     */
    @Override
    public void undo() {
        team.removeDeveloper(person);
    }

    /**
     * Redo the action, adding the person
     */
    @Override
    public void redo() {
        team.addDeveloper(person);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.TEAM, team, "availableMembersList");
        message.setTab("People");

        return message;
    }
}
