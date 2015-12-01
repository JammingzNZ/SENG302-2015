package seng302.group6.Model.Command.Project;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing teh project description
 * Created by Josh on 1/05/2015.
 */
public class ChangeProjectDescription implements Command {

    private Project project;
    private String oldDescription;
    private String newDescription;

    /**
     * Create a ChangeDescription command for changing the description of a project
     * @param project Project to change the description of
     * @param newDescription String to change the description to
     */
    public ChangeProjectDescription(Project project, String newDescription) {
        this.project = project;
        this.newDescription = newDescription;
        this.oldDescription = project.getDescription();
    }

    /**
     * Undo the action, setting the description to the old description
     */
    @Override
    public void undo() {
        project.setDescription(oldDescription);
    }

    /**
     * Redo the action, setting the description to the new description
     */
    @Override
    public void redo() {
        project.setDescription(newDescription);
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PROJECT, project, "projectDescription");

        return message;
    }
}
