package seng302.group6.Model.Command.Team;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemType;

import java.util.HashMap;

/**
 * Command class which allows undo/redo functionality for creating a team
 * Created by Jaln on 26/3/2015
 */
public class CreateTeam implements Command {

    private HashMap<String, Team> teams;
    private Team team;

    /**
     * Create a CreateTeam command for adding/removing the team to/from the given teams hashmap.
     * @param teams A hashmap to add the person to
     * @param team Team to be added to the hashmap
     */
    public CreateTeam(HashMap<String, Team> teams, Team team) {
        super();

        this.team = team;
        this.teams = teams;
    }

    /**
     * Undo the action, removing the team from the teams hashmap.
     */
    public void undo() {
        teams.remove(team.uid());
    }

    /**
     * Redo the action, adding the team to the teams hashmap.
     */
    public void redo() {
        teams.put(team.uid(), team);
    }


    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Create", ItemType.TEAM, team);

        return message;
    }

}
