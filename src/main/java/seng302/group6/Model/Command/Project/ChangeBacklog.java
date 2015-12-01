package seng302.group6.Model.Command.Project;

import seng302.group6.Model.Command.Command;
import seng302.group6.Model.Command.CommandMessage;
import seng302.group6.Model.ItemClasses.Backlog;
import seng302.group6.Model.ItemClasses.Project;
import seng302.group6.Model.ItemClasses.Workspace;
import seng302.group6.Model.ItemType;

/**
 * Command class which allows undo/redo functionality for changing the backlog of a project
 * Created by dan on 22/07/15.
 */
public class ChangeBacklog implements Command{

    private Project project;
    private String oldBacklog;
    private String newBacklog;

    /**
     * Create a ChangeBacklog command for changing the backlog of a project
     * @param project project to change the backlog of
     * @param newBacklog backlog uid that is being allocated to the project
     */
    public ChangeBacklog(Project project, String newBacklog) {
        this.project = project;
        this.newBacklog = newBacklog;
        this.oldBacklog = project.getBacklog();
    }

    /**
     * Undo the action, setting the backlog to the old backlog
     */
    @Override
    public void undo()
    {
        project.setBacklog(oldBacklog);
        Backlog backlog = Workspace.getBacklog(oldBacklog);

        if (backlog != null) {
            backlog.forgetProject(project.uid());
        }
    }

    /**
     * Redo the action, setting the backlog to the new backlog
     */
    @Override
    public void redo()
    {
        project.setBacklog(newBacklog);
        Backlog backlog = Workspace.getBacklog(newBacklog);
        if (backlog != null) {
            backlog.allocateToProject(project.uid());
        }
    }

    /**
     * Return a message indicating properties of the command
     * @return a message indicating properties of the command
     */
    @Override
    public CommandMessage getMessage() {
        CommandMessage message = new CommandMessage("Change", ItemType.PROJECT, project, "backlogCombo");

        return message;
    }



}
