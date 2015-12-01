package seng302.group6.Model.Command.Team;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Team;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for removing a member from the team
 * Created by Josh on 1/05/2015.
 */
public class RemoveMember implements Command {

    private Team team;
    private String person;
    private String role = "";

    /**
     * Create a RemoveMember command for adding a person to a team
     * @param team Team to add the person to
     * @param person Person to be added
     */
    public RemoveMember(Team team, String person) {
        this.team = team;
        this.person = person;

        if(team.getDevelopers().contains(person)){
            role = "Developer";
        }
        String sm = team.getScrumMaster();
        if (sm != null) {
            if (team.getScrumMaster().equals(person)) {
                role = "Scrum Master";
            }
        }
        String po = team.getProductOwner();
        if (po != null) {
            if (team.getProductOwner().equals(person)) {
                role = "Product Owner";
            }
        }
    }

    /**
     * Undo the action, removing the person
     */
    @Override
    public void undo() {
        team.addPerson(person);
        if (role == "Developer") {
            team.addDeveloper(person);
        }
        else if (role == "Scrum Master") {
            team.setScrumMaster(person);
        }
        else if (role == "Product Owner") {
            team.setProductOwner(person);
        }
    }

    /**
     * Redo the action, adding the person
     */
    @Override
    public void redo() {
        team.removePerson(person);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.TEAM, team, "teamList");
        message.setTab("People");

        return message;
    }
}
