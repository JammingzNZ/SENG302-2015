package seng302.group6.Model.Command.Team;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the SM of a team
 * Created by Josh on 1/05/2015.
 */
public class ChangeScrumMaster implements Command {

    private Team team;
    private String oldSM;
    private String newSM;

    /**
     * Create a ChangeScrumMaster command for changing the scrum master of a team
     * @param team Team to change the scrum master of
     * @param newSM Person to set the scrum master to
     */
    public ChangeScrumMaster(Team team, String newSM) {
        this.team = team;
        this.newSM = newSM;
        this.oldSM = team.getScrumMaster();
    }

    /**
     * Undo the action, setting the scrum master to the old scrum master
     */
    @Override
    public void undo() {
        team.setScrumMaster(oldSM);
    }

    /**
     * Redo the action, setting the scrum master to the new scrum master
     */
    @Override
    public void redo() {
        team.setScrumMaster(newSM);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.TEAM, team, "smComboBox");
        message.setTab("People");

        return message;
    }
}
