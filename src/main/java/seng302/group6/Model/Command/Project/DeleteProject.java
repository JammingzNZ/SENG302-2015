package seng302.group6.Model.Command.Project;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for deleting a project
 * Created by Josh Norton on 17/03/2015.
 */
public class DeleteProject implements Command {

    private Project project;

    public DeleteProject(Project project) {
        super();

        this.project = project;
    }

    /**
     * Undo the action of delete project
     */
    public void undo()
    {
        Workspace.addProject(project);
        Backlog backlog = Workspace.getBacklog(this.project.getBacklog());
        if (backlog != null) {
            backlog.allocateToProject(this.project.uid());
        }
    }

    /**
     * Redo the action of delete project
     */
    public void redo()
    {
        Workspace.removeProject(this.project.uid());
        Backlog backlog = Workspace.getBacklog(this.project.getBacklog());
        if (backlog != null) {
            backlog.forgetProject(this.project.uid());
        }
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Delete", ItemType.PROJECT, project);

        return message;
    }
}
