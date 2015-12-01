package seng302.group6.Model.Command.Project;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemType;

import java.time.LocalDate;

/**
 * Command class which allows undo/redo functionality for allocating a dev team to a project
 * Created by Simon on 11/05/15
 */
public class AllocateDevTeam implements Command {

    private Project project;
    private String team;
    private LocalDate start;
    private LocalDate end;

    /**
     * Create an AddDevTeam command for adding a dev team to a project
     * @param project project to add the dev team to
     * @param team team to be added
     * @param start date to start the allocation
     * @param end date to end the allocation
     */
    public AllocateDevTeam(Project project, String team,
                           LocalDate start, LocalDate end) {
        this.project = project;
        this.team = team;
        this.start = start;
        this.end = end;
    }

    /**
     * Undo the action, removing the team
     */
    @Override
    public void undo() {
        project.removeDevTeam(team, start, end);
    }

    /**
     * Redo the action, adding the team
     */
    @Override
    public void redo() {
        project.addDevTeam(team, start, end);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PROJECT, project, "allocatedTeamsList");
        message.setTab("Teams");

        return message;
    }
}
