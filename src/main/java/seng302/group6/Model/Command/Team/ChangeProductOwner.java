package seng302.group6.Model.Command.Team;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the PO of a team
 * Created by Josh on 1/05/2015.
 */
public class ChangeProductOwner implements Command {

    private Team team;
    private String oldPO;
    private String newPO;

    /**
     * Create a ChangeProductOwner command for changing the product owner of a team
     * @param team Team to change the product owner of
     * @param newPO Person to set the product owner to
     */
    public ChangeProductOwner(Team team, String newPO) {
        this.team = team;
        this.newPO = newPO;
        this.oldPO = team.getProductOwner();
    }

    /**
     * Undo the action, setting the product owner to the old product owner
     */
    @Override
    public void undo() {
        team.setProductOwner(oldPO);
    }

    /**
     * Redo the action, setting the product owner to the new product owner
     */
    @Override
    public void redo() {
        team.setProductOwner(newPO);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.TEAM, team, "poComboBox");
        message.setTab("People");

        return message;
    }
}
