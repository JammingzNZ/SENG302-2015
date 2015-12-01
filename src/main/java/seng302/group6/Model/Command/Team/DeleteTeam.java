package seng302.group6.Model.Command.Team;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Person;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for deleting a team
 * Created by Jaln Rodger on 2/05/2015.
 */
public class DeleteTeam implements Command {

    private Team team;

    /**
     * Constructor for the command class of DeleteTeam
     * @param team the team to be deleted
     */
    public DeleteTeam(Team team) {
        super();
        this.team = team;
    }

    /**
     * Undo the action, undoing the deletion of a team
     */
    public void undo() {
        Workspace.addTeam(team);
        //Makes it so that all the people in the team join it
        for (int i = 0; i < team.getPeople().size(); i++)
        {
            String personID = team.getPeople().get(i);
            Person person = Workspace.getPerson(personID);
            person.joinedTeam();
        }
    }

    /**
     * Redo the action, redong the deletion of a team
     */
    public void redo() {
        Workspace.removeTeam(team.uid());
        //Makes it so that all the people in the team leave it
        for (int i = 0; i < team.getPeople().size(); i++)
        {
            String personID = team.getPeople().get(i);
            Person person = Workspace.getPerson(personID);
            person.leftTeam();
        }
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.TEAM, team);

        return message;
    }
}
