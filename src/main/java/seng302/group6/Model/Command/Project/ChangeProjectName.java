package seng302.group6.Model.Command.Project;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the project name
 * Created by Josh on 1/05/2015.
 */
public class ChangeProjectName implements Command {

    private Project project;
    private String oldName;
    private String newName;

    /**
     * Create a ChangeProjectName command for changing the description of a project
     * @param project Project to change the name of
     * @param newName String to change the name to
     */
    public ChangeProjectName(Project project, String newName) {
        this.project = project;
        this.newName = newName;
        this.oldName = project.getLongName();
    }

    /**
     * Undo the action, setting the description to the old description
     */
    @Override
    public void undo() {
        project.setLongName(oldName);
    }

    /**
     * Redo the action, setting the description to the new description
     */
    @Override
    public void redo() {
        project.setLongName(newName);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PROJECT, project, "projectName");

        return message;
    }
}
